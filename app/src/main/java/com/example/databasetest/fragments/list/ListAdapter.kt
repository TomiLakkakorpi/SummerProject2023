package com.example.databasetest.fragments.list

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.databasetest.MainActivity
import com.example.databasetest.R
import com.example.databasetest.model.Task
import kotlinx.android.synthetic.main.custom_row.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
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
        val context = holder.itemView.context
        val currentItem = taskList[position]

        fun showNotification(title: String, text: String, id: Int) {
            val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notification = NotificationCompat.Builder(context, "ToDoChannel")
                .setContentText(text)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.appicon)
                .build()
            notificationManager.notify(id, notification)
        }

        val dateValuesNotification = currentItem.date.split("/")
        val timeValuesNotification = currentItem.time.split(":")

        val notificationYear = dateValuesNotification[0]
        val notificationMonth = dateValuesNotification[1]
        val notificationDay = dateValuesNotification[2]

        val notificationHour = dateValuesNotification[0]
        val notificationMinute = timeValuesNotification[1]

        val currentTime = LocalDateTime.now()
        val correctYear = "20$notificationYear"
        val dueTime = LocalDateTime.of(correctYear.toInt(), notificationMonth.toInt(), notificationDay.toInt(), notificationHour.toInt(), notificationMinute.toInt())
        val resultSeconds = currentTime.until(dueTime, ChronoUnit.SECONDS)
        val notifHeader = currentItem.header

        var notification1Sent = false
        var notification2Sent = false

        /*
        if (currentItem.notifyDay && !notification1Sent) {
            //Notification at due time
            GlobalScope.launch {
                delay(resultSeconds * 1000)
                val notifText = "Muistutus tehtävästä $notifHeader"
                showNotification(currentItem.header, notifText, currentItem.id)
                notification1Sent = true
            }
        }

        if (currentItem.notifyHour && !notification2Sent) {
            //Notification 1 hour before
            GlobalScope.launch {
                val thisResult = resultSeconds - 3600
                if (thisResult > 0) {
                    delay(thisResult * 1000)
                    val notifText = "Tehtävä $notifHeader tunnin kuluttua"
                    showNotification(currentItem.header, notifText, currentItem.id)
                    Toast.makeText(context, "$thisResult", Toast.LENGTH_SHORT).show()
                    notification2Sent = true
                }
            }
        }
         */

        val dateValues = currentItem.date
        val valuesArrayList = dateValues.split("/")
        val timeValues = currentItem.time
        val valuesArrayList2 = timeValues.split(":")

        //Getting values from date arraylist and time arraylist
        val year = valuesArrayList[0]
        val month = valuesArrayList[1]
        val day = valuesArrayList[2]

        val hour = valuesArrayList2[0]
        val minute = valuesArrayList2[1]

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

        //Setting the tasks background color based on category
        if (currentItem.category == "Liikunta") { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.liikunta) }
        if (currentItem.category == "Terveys") { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.terveys) }
        if (currentItem.category == "Koulu") { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.koulu) }
        if (currentItem.category == "Työ") { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.tyo) }
        if (currentItem.category == "Tärkeä") { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.tärkeä) }
        if (currentItem.category == "Harrastus") { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.harrastus) }
        if (currentItem.category == "Askare") { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.askare) }
        if (currentItem.category == "Tapaaminen") { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.tapaaminen) }
        if (currentItem.category == "Pelit") { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.pelit) }
        if (currentItem.category == "Jääkiekko") { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.Jääkiekko) }
        if (currentItem.category == "Formula 1") { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.Formula_1) }
        if (currentItem.category == "eSports") { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.eSports) }
        if (currentItem.category == "Muu") { holder.itemView.taskMainConstraint.setBackgroundResource(R.color.muu) }

        // Getting Task due date
        val reminderYear = "20$year"
        val remindYear = reminderYear.toInt()
        val remindMonth = month.toInt()
        val remindDay = day.toInt()

        //Getting task due time
        val reminderHour = hour.toInt()
        val reminderMinute = minute.toInt()

        //Date 1 day before due date & Time 1 hour before due time
        val remindDateDayBefore = LocalDate.of(remindYear, remindMonth, remindDay).minusDays(1).toString()
        val remindTimeHourBefore = LocalTime.of(reminderHour, reminderMinute).minusHours(1).toString()

        //Exact Due Date
        val remindDate = LocalDate.of(remindYear, remindMonth, remindDay).toString()
        val remindTime = LocalTime.of(reminderHour, reminderMinute).toString()

        //Getting local dates
        //These values need to be re-obtained every minute or so
        val localDate = LocalDate.now().toString()
        val localTimeNotSplit = LocalTime.now().toString()
        val localTimeValues = localTimeNotSplit.split(":")
        val localHour = localTimeValues[0]
        val localMinute = localTimeValues[1]
        val localTime = "$localHour:$localMinute"

        //These need to be checked every minute or so
        //Notification a day before
        if (localDate == remindDateDayBefore && localTime == remindTime) {
            //Call notification
        }

        //Notification an hour before
        if (localDate == remindDate && localTime == remindTimeHourBefore) {
            //Call notification
        }

        //Notify at due date and time
        if (localDate == remindDateDayBefore && localTime == remindTime) {
            //Call notification
        }
    }

    fun setData(task: List<Task>){
        this.taskList = task
        notifyDataSetChanged()
    }
}