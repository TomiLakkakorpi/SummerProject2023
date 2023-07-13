package com.example.databasetest.fragments.add

import android.app.*
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.databasetest.*
import com.example.databasetest.databinding.FragmentListBinding
import com.example.databasetest.model.Task
import com.example.databasetest.viewmodel.TaskViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class AddFragment : Fragment() {

    //Intializing some varaiables and values
    private var _binding: FragmentListBinding? = null
    private lateinit var mTaskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        //Calling insertion function when "add" button is pressed
        view.addScreenAdd.setOnClickListener {
            insertDataToDatabase()
        }

        //Opening time picker menu when button is pressed
        view.addScreenTimePicker.setOnClickListener {
            openTimePicker()
        }

        //Opening date picker menu when button is pressed
        view.addScreenDatePicker.setOnClickListener {
            openDatePicker()
        }

        //Changing from addFragment to ListFragment when "cancel" button is pressed
        view.addScreenCancel.setOnClickListener {
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
            Toast.makeText(requireContext(), "Tehtävää ei tallennettu", Toast.LENGTH_SHORT).show()
        }

        //Initializing categoryfilter dropdown menu
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val categories = resources.getStringArray(R.array.categories)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categories)
        view.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView).setAdapter(arrayAdapter)

        return view
    }

    var notificationDay = ""
    var notificationMonth = ""
    var notificationYear = ""

    //Function for date picker
    private fun openDatePicker() {

        //Initializing our calendar
        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener{view, year, month, dayOfMonth ->
           myCalendar.set(Calendar.YEAR, year)
           myCalendar.set(Calendar.MONTH, month)
           myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
           updateLabel(myCalendar)
        }

        //Getting date values for notification scheduler
        notificationDay = Calendar.DAY_OF_MONTH.toString()
        notificationMonth = Calendar.MONTH.toString()
        notificationYear = Calendar.YEAR.toString()

        DatePickerDialog(requireContext(), datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
        myCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun updateLabel(myCalendar: Calendar) {
        val myFormat = "dd/MM/yy"
        val sdf = SimpleDateFormat(myFormat)
        etAddScreenDate.setText(sdf.format(myCalendar.time))
    }

    //Initializing some variables for time picker
    private var pickerHour = ""
    private var pickerMinute = ""
    var timeString = "00:00"

    //Function for time picker
    private fun openTimePicker() {

        //Setting the clock format
        val clockFormat = TimeFormat.CLOCK_24H

        //Initializing the time picker
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Valitse Aika")
            .build()
        picker.show(childFragmentManager, "TAG")

        //Getting time values from the picker when the user presses the "ok" buttton (positive button) and setting the values to timeString variable
        picker.addOnPositiveButtonClickListener {
            pickerHour = picker.hour.toString()
            pickerMinute = picker.minute.toString()
            timeString = "$pickerHour:$pickerMinute"

            //Adding a "0" to minutes if minutes has only one digit, then setting the time value to the edittext for the user to see
            if (timeCheck3(timeString) || timeCheck4(timeString)) {
                val showTime = "$pickerHour:0$pickerMinute"
                etAddScreenTime.setText(showTime)
            } else {
                etAddScreenTime.setText(timeString)
            }
        }
    }

    //Function for inserting data to database
    private fun insertDataToDatabase() {

        //Getting values from "add screen" edittext fields
        val header = etAddScreenHeader.text.toString()
        val details = etAddScreenDetails.text.toString()
        val category = autoCompleteTextView.text.toString()
        val dateString = etAddScreenDate.text.toString()

        //Getting values for notifyDay and notifyHour from the checkboxes
        val notifyDay: Boolean = addCheckboxDayBefore.isChecked
        val notifyHour: Boolean = addCheckBoxHourBefore.isChecked

        //Splitting date and time values into an array
        val valuesArrayList = dateString.split("/")
        val valuesArrayList2 = timeString.split(":")

        //Getting values for day, month and year from the date values array
        val day = valuesArrayList[0]
        val month = valuesArrayList[1]
        val year = valuesArrayList[2]

        //Getting values for hour and minute from the time values array
        val hour = valuesArrayList2[0]
        val minute = valuesArrayList2[1]

        //Setting date values for the 4 scenarios
        val regularDate = "$year/$month/$day"                       //normal date (example 10/10/23)
        val dayMissingZeroDate = "$year/$month/0$day"               //day has only one digit (example 1/10/23) so we add 0 in front of it
        val monthMissingZeroDate = "$year/0$month/$day"             //month has only one digit (example 10/1/23) so we add 0 in front of it
        val dayAndMonthMissingZeroDate = "$year/0$month/0$day"      //both day and month have only one digit (example 1/1/23) so we add 0 in front of both of them

        //Setting time values for the 4 possible scenarios
        val regularTime = "$hour:$minute"                           //Normal time (example 10:00)
        val hourMissingZeroTime = "0$hour:$minute"                  //Hour value is only one digit (example 9:00) so we add a 0 in front of it
        val minuteMissingZeroTime = "$hour:0$minute"
        val bothHourAndMinuteMissingZeroTime = "0$hour:0$minute"

        //Initializing variables
        var dayName = ""
        var date = ""
        var time = ""
        val status = false

        //Getting the day value based on the given due date
        val getTaskDayName = LocalDate.of(year.toInt(), month.toInt(), day.toInt()).dayOfWeek.toString()

        if (getTaskDayName == "MONDAY")     { dayName = "Maanantai" }
        if (getTaskDayName == "TUESDAY")    { dayName = "Tiistai" }
        if (getTaskDayName == "WEDNESDAY")  { dayName = "Keskiviikko" }
        if (getTaskDayName == "THURSDAY")   { dayName = "Torstai" }
        if (getTaskDayName == "FRIDAY")     { dayName = "Perjantai" }
        if (getTaskDayName == "SATURDAY")   { dayName = "Lauantai" }
        if (getTaskDayName == "SUNDAY")     { dayName = "Sunnuntai" }

            //Checking if header field is empty
            if (checkHeader(header))
            {
                //Checking if time field is empty
                if (checkTime(timeString))
                {
                    //Checking if the given time input matches any of the accepted formats
                    if (timeCheck1(timeString) || timeCheck2(timeString) || timeCheck3(timeString) || timeCheck4(timeString))
                    {
                        //Checking if date field is empty
                        if (checkDate(dateString))
                        {
                            //Checking if the given date input matches any of the accepted formats
                            if (dateCheck1(dateString) || dateCheck2(dateString) || dateCheck3(dateString) || dateCheck4(dateString))
                            {
                                //Checking if category field is empty
                                if (checkCategory(category))
                                {
                                    //Normal date 11/11/23
                                    if (dateCheck1(dateString)) { date = regularDate }

                                    //Date like 1/11/23
                                    if (dateCheck2(dateString)) { date = dayMissingZeroDate }

                                    //Date like 11/1/23
                                    if (dateCheck3(dateString)) { date = monthMissingZeroDate }

                                    //Date like 1/1/23
                                    if (dateCheck4(dateString)) { date = dayAndMonthMissingZeroDate }

                                    //Normal time 10:00
                                    if (timeCheck1(timeString)) { time = regularTime }

                                    //Time with only 1 digit in hour field (1:00)
                                    if (timeCheck2(timeString)) { time = hourMissingZeroTime }

                                    //Time with only 1 digit in minute field (1:0) (Timepicker assigns only one digit to minute value if the value is for example 12:00)
                                    if (timeCheck3(timeString)) { time = minuteMissingZeroTime }

                                    //Time with 1 digit in both hour and minute field
                                    if (timeCheck4(timeString)) { time = bothHourAndMinuteMissingZeroTime}

                                    //If the given date isn't a valid date, telling the user about it with a toast message
                                    if (!isValidDate(dateString)) {
                                        Toast.makeText(requireContext(), "Syötä oikea päivämäärä", Toast.LENGTH_SHORT).show()
                                    }

                                    //If the given time isn't a valid time, telling the user about it with a toast message
                                    //if (!isValidTime(timeString)) {
                                    //     Toast.makeText(requireContext(), "Syötä oikea kellonaika", Toast.LENGTH_SHORT).show()
                                    //}

                                    //Adding a task with the given values and changing back to listfragment
                                    val task = Task(0, header, time, date, dayName, details, category, status, notifyDay, notifyHour)
                                    mTaskViewModel.addTask(task)

                                    createNotificationChannel()
                                    scheduleNotification()

                                    Toast.makeText(requireContext(), "Tehtävä tallennettu", Toast.LENGTH_SHORT).show()
                                    findNavController().navigate(R.id.action_addFragment_to_listFragment)

                                    //Different toast messages for different errors
                                } else {Toast.makeText(requireContext(), "Valitse kategoria", Toast.LENGTH_SHORT).show()}
                            } else {Toast.makeText(requireContext(), "Syötä päivämäärä muodossa päivä/kuukausi/vuosi", Toast.LENGTH_SHORT).show()}
                        } else {Toast.makeText(requireContext(), "Syötä päivämäärä", Toast.LENGTH_SHORT).show()}
                    } else {Toast.makeText(requireContext(), "Syötä aika muodossa tunnit:minuutit", Toast.LENGTH_SHORT).show()}
                } else {Toast.makeText(requireContext(), "Syötä kellonaika", Toast.LENGTH_SHORT).show()}
            } else {Toast.makeText(requireContext(), "Syötä Otsikko", Toast.LENGTH_SHORT).show()}
    }

    private fun scheduleNotification() {
        val intent = Intent(context, Notification::class.java)
        val title = etAddScreenHeader.text.toString()
        val message = "Muistutus tehtävän" + etAddScreenHeader.text.toString() + "takarajasta"
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()

        when {
            alarmManager.canScheduleExactAlarms() -> {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    pendingIntent
                )
                Toast.makeText(requireContext(), "Ajastus aloitettu, ilmoitus $time kuluttua", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(requireContext(), "Tehtävän muistutusta ei ajastettu koska oikeutta ei ole annettu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getTime(): Long {
        timeString = etAddScreenTime.text.toString()
        val dateString = etAddScreenDate.text.toString()

        val valuesArrayListNotif = timeString.split(":")
        val valuesArrayListNotif2 = dateString.split("/")

        val hour = valuesArrayListNotif[0]
        val minute = valuesArrayListNotif[1]
        val day = valuesArrayListNotif2[0]
        val month = valuesArrayListNotif2[1]
        val year = "20" + valuesArrayListNotif2[2]

        val dateAndTimeNow = LocalDateTime.now()
        val dueDateAndTime = LocalDateTime.of(year.toInt(), month.toInt(), day.toInt(), hour.toInt(), minute.toInt())

        return dateAndTimeNow.until(dueDateAndTime, ChronoUnit.MILLIS)
    }

    private fun createNotificationChannel() {
        val name = "To Do Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = requireActivity().getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Checking that header field isn't empty
    private fun checkHeader(header: String): Boolean {
        return !(TextUtils.isEmpty(header))
    }

    //Checking that time field isn't empty
    private fun checkTime(time: String): Boolean {
        return !(TextUtils.isEmpty(time))
    }

    //Checking that date field isn't empty
    private fun checkDate(date: String): Boolean {
        return !(TextUtils.isEmpty(date))
    }

    //Checking that category field isn't empty
    private fun checkCategory(category: String): Boolean {
        return !(TextUtils.isEmpty(category))
    }

    //Checking input and matching for times like 11:11
    private fun timeCheck1(str: String): Boolean {
        val regex = Regex("\\d{2}:\\d{2}")
        return str.matches(regex)
    }

    //Checking input and matching for times like 1:11
    private fun timeCheck2(str: String): Boolean {
        val regex = Regex("\\d:\\d{2}")
        return str.matches(regex)
    }

    private fun timeCheck3(str: String): Boolean {
        val regex = Regex("\\d{2}:\\d")
        return str.matches(regex)
    }

    private fun timeCheck4(str: String): Boolean {
        val regex = Regex("\\d:\\d")
        return str.matches(regex)
    }

    //Checking input and matching for dates like 11/11/23
    private fun dateCheck1(str: String): Boolean {
        val regex = Regex("\\d{2}/\\d{2}/\\d{2}")
        return str.matches(regex)
    }

    //Checking input and matching for dates like 1/11/23
    private fun dateCheck2(str: String): Boolean {
        val regex = Regex("\\d/\\d{2}/\\d{2}")
        return str.matches(regex)
    }

    //Checking input and matching for dates like 11/1/23
    private fun dateCheck3(str: String): Boolean {
        val regex = Regex("\\d{2}/\\d/\\d{2}")
        return str.matches(regex)
    }

    //Checking input and matching for dates like 1/1/23
    private fun dateCheck4(str: String): Boolean {
        val regex = Regex("\\d/\\d/\\d{2}")
        return str.matches(regex)
    }

    //Function to check if the given date is a valid date
    fun isValidDate(date: String): Boolean {
        val dateFormat = SimpleDateFormat("dd/MM/yy")
        dateFormat.isLenient = false

        return try {
            dateFormat.parse(date)
            true
        } catch (e: Exception) {
            false
        }
    }
}