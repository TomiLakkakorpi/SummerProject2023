package com.example.databasetest.fragments.add

import android.Manifest
import android.app.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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

    //Intializing varaiables
    private var _binding: FragmentListBinding? = null
    private lateinit var mTaskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                } else {
                }
            }

        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED) {
        } else {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        //Creating notification channel
        val channel = NotificationChannel(
            "ToDoChannel",
            "To Do Reminders",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

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

        //Increasing importance when "plus" button is clicked
        view.addScreenIncreaseImportance.setOnClickListener {
            if (importance in 0..2) {
                importance++
                addScreenRatingBar.rating = importance.toFloat()
            } else {
                Toast.makeText(requireContext(), "Korkein sallittu tärkeys on 3 tähteä!", Toast.LENGTH_SHORT).show()
            }
        }

        //Decreasing importance when "minus" button is clicked
        view.addScreenDecreaseImportance.setOnClickListener {
            if (importance in 1..3) {
                importance--
                addScreenRatingBar.rating = importance.toFloat()
            } else {
                Toast.makeText(requireContext(), "Matalin sallittu tärkeys on 0 tähteä!", Toast.LENGTH_SHORT).show()
            }
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
        view.findViewById<AutoCompleteTextView>(R.id.addAutoCompleteTextView).setAdapter(arrayAdapter)

        return view
    }

    //task importance value
    private var importance = 0

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
        val category = addAutoCompleteTextView.text.toString()
        val dateString = etAddScreenDate.text.toString()

        //Getting values for notifyDay and notifyHour from the checkboxes
        val notifyMinutes: Boolean = addCheckboxNow.isChecked
        val notifyHour: Boolean = addCheckBoxHourBefore.isChecked
        val notifyDay: Boolean = addCheckBoxDayBefore.isChecked

        var day = "0"
        var month = "0"
        var year = "00"
        var hour = "0"
        var minute = "0"
        var dayName = ""

        val getTaskDayName: String

        if (checkDate(dateString)) {
            //Splitting date value into an array & getting values for day, month & year from the array
            val valuesArrayList = dateString.split("/")
            day = valuesArrayList[0]
            month = valuesArrayList[1]
            year = valuesArrayList[2]

            //Getting the day value based on the given due date
            getTaskDayName = LocalDate.of(year.toInt(), month.toInt(), day.toInt()).dayOfWeek.toString()

            if (getTaskDayName == "MONDAY")     { dayName = "Maanantai" }
            if (getTaskDayName == "TUESDAY")    { dayName = "Tiistai" }
            if (getTaskDayName == "WEDNESDAY")  { dayName = "Keskiviikko" }
            if (getTaskDayName == "THURSDAY")   { dayName = "Torstai" }
            if (getTaskDayName == "FRIDAY")     { dayName = "Perjantai" }
            if (getTaskDayName == "SATURDAY")   { dayName = "Lauantai" }
            if (getTaskDayName == "SUNDAY")     { dayName = "Sunnuntai" }
        }

        if (checkTime(timeString)) {
            //Splitting time value into an array & getting values for hour & minute from the array
            val valuesArrayList2 = timeString.split(":")
            hour = valuesArrayList2[0]
            minute = valuesArrayList2[1]
        }

        //Setting time values for the 4 possible scenarios
        val regularTime = "$hour:$minute"                           //Normal time (example 10:00)
        val hourMissingZeroTime = "0$hour:$minute"                  //Hour value is only one digit (example 9:00) so we add a 0 in front of it
        val minuteMissingZeroTime = "$hour:0$minute"
        val bothHourAndMinuteMissingZeroTime = "0$hour:0$minute"

        //Initializing variables
        val date = "$day/$month/$year"
        var time = ""
        val status = false

        //Checking if header field is empty
        if (checkHeader(header) && checkTime(timeString) &&  checkDate(dateString) && checkCategory(category))
        {
            if (!isDateInThePast(dateString, timeString)) {

                //Normal time 10:00
                if (timeCheck1(timeString)) { time = regularTime }

                //Time with only 1 digit in hour field (1:00)
                if (timeCheck2(timeString)) { time = hourMissingZeroTime }

                //Time with only 1 digit in minute field (1:0) (Timepicker assigns only one digit to minute value if the value is for example 12:00)
                if (timeCheck3(timeString)) { time = minuteMissingZeroTime }

                //Time with 1 digit in both hour and minute field
                if (timeCheck4(timeString)) { time = bothHourAndMinuteMissingZeroTime}

                //Adding a task with the given values and changing back to listfragment
                val task = Task(0, header, time, date, dayName, details, category, status, notifyMinutes, notifyHour, notifyDay, importance)
                mTaskViewModel.addTask(task)

                //Navigating back to list fragment
                findNavController().navigate(R.id.action_addFragment_to_listFragment)

            } else {
                //Displaying a toast if the selected date is in the past
                Toast.makeText(requireContext(), "Valittu aika on jo mennyt, valitse uusi aika", Toast.LENGTH_SHORT).show()
            }

        } else {
            //Displaying toast if any of the 4 required values are empty
            Toast.makeText(requireContext(), "Syötä vähintään otsikko, kellonaika, päivämäärä ja kategoria", Toast.LENGTH_SHORT).show()
        }
    }

    //Function to check whether the given date & time is in the past
    private fun isDateInThePast(dateString: String, timeString: String): Boolean {
        val now = LocalDateTime.now()

        val dateValues = dateString.split("/")
        val day = dateValues[0]
        val month = dateValues[1]
        val year = "20" + dateValues[2]

        val timeValues = timeString.split(":")
        val hour = timeValues[0]
        val minute = timeValues[1]

        val taskTime = LocalDateTime.of(year.toInt(), month.toInt(), day.toInt(), hour.toInt(), minute.toInt())
        val timeDifference = now.until(taskTime, ChronoUnit.MINUTES)
        Toast.makeText(requireContext(), "$timeDifference", Toast.LENGTH_SHORT).show()

        return if (timeDifference < 0) {
            true
        } else {
            false
        }
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
}