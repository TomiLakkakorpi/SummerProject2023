package com.example.databasetest.fragments.list

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.databasetest.R
import com.example.databasetest.alarm.AlarmItem
import com.example.databasetest.alarm.AndroidAlarmScheduler
import com.example.databasetest.model.Task
import kotlinx.android.synthetic.main.custom_row.view.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.Collections.emptyList

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var taskList = emptyList<Task>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        //Creating notification channel
        val channel = NotificationChannel(
            "ToDoChannel",
            "To Do Reminders",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = parent.context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        return MyViewHolder(LayoutInflater.from(parent.context).inflate((R.layout.custom_row), parent, false))
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //Getting current task from task list
        val currentItem = taskList[position]

        //Getting current task´s date & time values
        val dateValues = currentItem.date.split("/")
        val timeValues = currentItem.time.split(":")

        //Getting values from date arraylist and time arraylist
        val year = dateValues[0]
        val month = dateValues[1]
        val day = dateValues[2]

        val hour = timeValues[0]
        val minute = timeValues[1]

        //If date is for example 01/01/23 drop first 0 from both day and month and then show the date in the list
        if (month.startsWith("0") && day.startsWith("0")) {
            val newMonth = month.drop(1)
            val newDay = day.drop(1)
            val dateValue = "$newDay/$newMonth/$year"
            holder.itemView.tvTaskDate.text = dateValue
        }

        //If date is for example 11/11/23 show the date as it is and then show the date in the list
        if (!month.startsWith("0") && !day.startsWith("0")) {
            val dateValue = "$day/$month/$year"
            holder.itemView.tvTaskDate.text = dateValue
        }

        //If date is for example 01/11/23 drop the fist 0 from day and then show the date in the list
        if (day.startsWith("0") && !month.startsWith("0")) {
            val newDay = day.drop(1)
            val dateValue = "$newDay/$month/$year"
            holder.itemView.tvTaskDate.text = dateValue
        }

        //If date is for example 11/01/23 drop the first 0 from month and then show the date in the list
        if (month.startsWith("0") && !day.startsWith("0")) {
            val newMonth = month.drop(1)
            val dateValue = "$day/$newMonth/$year"
            holder.itemView.tvTaskDate.text = dateValue
        }

        //If time is for example 09:00 drop the first 0 from hour and then show the date in the list
        if(hour.startsWith("0") && !minute.startsWith("0")) {
            val newHour = hour.drop(1)
            val timeValue = "$newHour:$minute"
            holder.itemView.tvTaskTime.text = "\u2022 " + timeValue
        } else {
            val timeValue = "$hour:$minute"
            holder.itemView.tvTaskTime.text = "\u2022 " + timeValue
        }

        //Setting header, dayName, details and category from db to the list
        holder.itemView.tvTaskHeader.text = currentItem.header
        holder.itemView.tvTaskDay.text = currentItem.dayName
        holder.itemView.tvCategory.text = currentItem.category

        //Showing a bullet point in front of details textview if it isn't empty
        if (!currentItem.details.isBlank()) {
            holder.itemView.tvTaskDetails.text = "\u2022 " + currentItem.details
        } else {
            holder.itemView.tvTaskDetails.text = currentItem.details
        }

        //If user clicks on the task, open update fragment for that task.
        holder.itemView.taskMainConstraint.setOnClickListener{
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }

        //Setting the task´s checkbox based on its status in the database (status true = checkbox checked)
        if (currentItem.status) {
            holder.itemView.taskCheckbox.setChecked(true)
        } else {
            holder.itemView.taskCheckbox.setChecked(false)
        }

        //Getting local date & time for scheduling notifications
        val currentTime = LocalDateTime.now()

        //Getting due date & time from the user given date & time
        val correctYear = "20$year"
        val dueTime = LocalDateTime.of(correctYear.toInt(), month.toInt(), day.toInt(), hour.toInt(), minute.toInt())

        //Getting the time difference between local time and due time
        val resultSeconds = currentTime.until(dueTime, ChronoUnit.SECONDS)
        val thisContext = holder.itemView.getContext()

        //Notification 15 minutes before due time
        if (currentItem.notifyMinutes && resultSeconds > 900) {
            scheduleAlarm(resultSeconds-900, "1:${currentItem.header}:${currentItem.id}", thisContext)
        }

        //Notification one hour before due time
        if (currentItem.notifyHour && resultSeconds > 3600) {
            scheduleAlarm(resultSeconds-3600, "2:${currentItem.header}:${currentItem.id}", thisContext)
        }

       //Notification one day before due time
       if (currentItem.notifyDay && resultSeconds > 86400) {
           scheduleAlarm(resultSeconds-86400, "3:${currentItem.header}:${currentItem.id}", thisContext)
       }

        //Setting the tasks background color based on category
        if (currentItem.category == "Liikunta")     { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.category1) }
        if (currentItem.category == "Terveys")      { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.category2) }
        if (currentItem.category == "Koulu")        { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.category3) }
        if (currentItem.category == "Työ")          { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.category4) }
        if (currentItem.category == "Tärkeä")       { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.category5) }
        if (currentItem.category == "Harrastus")    { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.category6) }
        if (currentItem.category == "Askare")       { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.category7) }
        if (currentItem.category == "Tapaaminen")   { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.category8) }
        if (currentItem.category == "Pelit")        { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.category9) }
        if (currentItem.category == "Jääkiekko")    { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.category10) }
        if (currentItem.category == "Formula 1")    { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.category11) }
        if (currentItem.category == "eSports")      { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.category12) }
        if (currentItem.category == "Muu")          { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.categoryRest) }
    }

    //Function to schedule alarm
    private fun scheduleAlarm(seconds: Long, message: String, context: Context) {
        val alarmItem: AlarmItem?
        val scheduler by lazy { AndroidAlarmScheduler(context) }

        alarmItem = AlarmItem(
            time = LocalDateTime.now()
                .plusSeconds(seconds),
            message = message
        )
        alarmItem.let(scheduler::schedule)
    }

    //Function to set task´s data to the task list
    fun setData(task: List<Task>){
        this.taskList = task
        notifyDataSetChanged()
    }
}