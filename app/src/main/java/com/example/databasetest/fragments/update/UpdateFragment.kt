package com.example.databasetest.fragments.update

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.databasetest.R
import com.example.databasetest.alarm.AlarmItem
import com.example.databasetest.alarm.AndroidAlarmScheduler
import com.example.databasetest.databinding.FragmentListBinding
import com.example.databasetest.model.Task
import com.example.databasetest.viewmodel.TaskViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class UpdateFragment : Fragment() {

    //Intializing some varaiables and values
    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var mTaskViewModel: TaskViewModel
    private var _binding: FragmentListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Initializing viewmodel and view
        val view = inflater.inflate(R.layout.fragment_update, container, false)
        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        //Getting date values from the database, splitting them by delimiter / and putting the values in an array
        val dateValues = args.currentTask.date
        val valuesArrayListDate = dateValues.split("/")
        val year = valuesArrayListDate[2]
        val month = valuesArrayListDate[1]
        val day = valuesArrayListDate[0]

        //Getting time values from the database, splitting them by delimiter : and putting the values in an array
        val timeValues = args.currentTask.time
        val valuesArrayListTime = timeValues.split(":")
        val hour = valuesArrayListTime[0]
        val minute = valuesArrayListTime[1]

        //Checking if day and month both start with 0 and dropping first index from the value before setting the value in the edittext field
        if (day.startsWith("0") && (month.startsWith("0"))) {
            val newDay = day.drop(1)
            val newMonth = month.drop(1)
            val dateValue1 = "$newDay/$newMonth/$year"
            view.etEditScreenDate.setText(dateValue1)
        }

        //Checking if neither of day or month start with 0 before setting the value in the edittext field
        if (!day.startsWith("0") && (!month.startsWith("0"))) {
            val dateValue1 = "$day/$month/$year"
            view.etEditScreenDate.setText(dateValue1)
        }

        //Checking if only day starts with 0 and dropping first index from the value before setting the value in the edittext field
        if (day.startsWith("0") && (!month.startsWith("0"))) {
            val newDay = day.drop(1)
            val dateValue1 = "$newDay/$month/$year"
            view.etEditScreenDate.setText(dateValue1)
        }

        //Checking if only month starts with 0 and dropping first index from the value before setting the value in the edittext field
        if (!day.startsWith("0") && (month.startsWith("0"))) {
            val newMonth = month.drop(1)
            val dateValue1 = "$day/$newMonth/$year"
            view.etEditScreenDate.setText(dateValue1)
        }

        //Checking if hour value starts with 0 and dropping the first index value if it does, else showing the time as it is in database
        if (hour.startsWith("0") && !minute.startsWith("0")) {
            val newHour = hour.drop(1)
            val timeValue = "$newHour:$minute"
            view.etEditScreenTime.setText(timeValue)
        } else {
            val timeValue = "$hour:$minute"
            view.etEditScreenTime.setText(timeValue)
        }

        //Setting the values from database to the edittext fields
        view.etEditScreenHeader.setText(args.currentTask.header)
        view.etEditScreenDetails.setText(args.currentTask.details)
        view.updateAutoCompleteTextView.setText(args.currentTask.category)

        //Setting rating to update screen´s rating bar with the value from database
        view.updateScreenRatingBar.rating = args.currentTask.importance.toFloat()

        //Setting the task status checkbox based on its value in database
        if (args.currentTask.status) {
            view.checkBox.setChecked(true)
        } else {
            view.checkBox.setChecked(false)
        }

        //Setting the minutes checkbox in update screen based on its value in database
        if (args.currentTask.notifyMinutes) {
            view.updateCheckboxMinutesBefore.setChecked(true)
        } else {
            view.updateCheckboxMinutesBefore.setChecked(false)
        }

        //Setting the hour checkbox in update screen based on its value in database
        if (args.currentTask.notifyHour) {
            view.updateCheckBoxHourBefore.setChecked(true)
        } else {
            view.updateCheckBoxHourBefore.setChecked(false)
        }

        //Setting the day checkbox in update screen based on its value in database
        if (args.currentTask.notifyDay) {
            view.updateCheckBoxDayBefore.setChecked(true)
        } else {
            view.updateCheckBoxDayBefore.setChecked(false)
        }

        //Calling update function when "update" button is pressed
        view.updateScreenUpdate.setOnClickListener {
            updateItem()
        }

        //Calling deletion function when "delete" button is pressed
        view.updateScreenDelete.setOnClickListener {
            deleteTask()
        }

        //Opening time picker menu when button is pressed
        view.updateScreenTimePicker.setOnClickListener {
            openTimePicker()
        }

        //Opening date picker menu when button is pressed
        view.updateScreenDatePicker.setOnClickListener {
            openDatePicker()
        }

        //Getting importance value from database with args
        importance = args.currentTask.importance

        //Increasing importance when "plus" button is clicked
        view.updateScreenIncreaseImportance.setOnClickListener {
            if (importance in 0..2) {
                importance++
                updateScreenRatingBar.rating = importance.toFloat()
            } else {
                Toast.makeText(requireContext(), "Korkein sallittu tärkeys on 3 tähteä!", Toast.LENGTH_SHORT).show()
            }
        }

        //Decreasing importance when "minus" button is clicked
        view.updateScreenDecreaseImportance.setOnClickListener {
            if (importance in 1..3) {
                importance--
                updateScreenRatingBar.rating = importance.toFloat()
            } else {
                Toast.makeText(requireContext(), "Matalin sallittu tärkeys on 0 tähteä!", Toast.LENGTH_SHORT).show()
            }
        }

        //Changing from updateFragment to listFragment when "cancel" button is pressed
        view.updateScreenCancel.setOnClickListener {
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            //Toast.makeText(requireContext(), "Tehtävää ei päivitetty", Toast.LENGTH_SHORT).show()
        }

        //Initializing categoryfilter dropdown menu
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val categories = resources.getStringArray(R.array.categories)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categories)
        view.findViewById<AutoCompleteTextView>(R.id.updateAutoCompleteTextView).setAdapter(arrayAdapter)

        return view
    }

    //task importance value
    private var importance = 0

    //Function for date picker
    private fun openDatePicker() {

        //Initializing our calendar
        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener{ view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(myCalendar)
        }

        DatePickerDialog(requireContext(), datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(
            Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun updateLabel(myCalendar: Calendar) {
        val myFormat = "dd/MM/yy"
        val sdf = SimpleDateFormat(myFormat)
        etEditScreenDate.setText(sdf.format(myCalendar.time))
    }

    //Initializing some variables for time picker
    private var pickerHour = ""
    private var pickerMinute = ""
    var timeString2 = "00:00"

    //Function for time picker
    private fun openTimePicker() {
        //Setting the clock format
        val clockFormat = TimeFormat.CLOCK_24H

        //Getting current task time values
        val taskTimeValue = args.currentTask.time.split(":")
        val setHour = taskTimeValue[0]
        val setMinute = taskTimeValue[1]

        //Initializing the time picker
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(setHour.toInt())
            .setMinute(setMinute.toInt())
            .setTitleText("Valitse Aika")
            .build()
        picker.show(childFragmentManager, "TAG")

        //Getting time values from the picker when the user presses the "ok" buttton (positive button) and setting the values to timeString variable
        picker.addOnPositiveButtonClickListener {
            pickerHour = picker.hour.toString()
            pickerMinute = picker.minute.toString()
            timeString2 = "$pickerHour:$pickerMinute"

            //Adding a "0" to minutes if minutes has only one digit, then setting the time value to the edittext for the user to see
            if (timeCheck1(timeString2)) {
                val showTime = "$pickerHour:$pickerMinute"
                etEditScreenTime.setText(showTime)
            }

            if (timeCheck2(timeString2)) {
                val showTime2 = "0$pickerHour:$pickerMinute"
                etEditScreenTime.setText(showTime2)
            }

            if (timeCheck3(timeString2)) {
                val showTime3 = "$pickerHour:0$pickerMinute"
                etEditScreenTime.setText(showTime3)
            }

            if (timeCheck4(timeString2)) {
                val showTime4 = "0$pickerHour:0$pickerMinute"
                etEditScreenTime.setText(showTime4)
            }
        }
    }

    //Function for updating a task
    private fun updateItem(){

        //Getting values from "edit screen" edittext fields
        val header = etEditScreenHeader.text.toString()
        val dateString = etEditScreenDate.text.toString()
        val timeString = etEditScreenTime.text.toString()
        val details = etEditScreenDetails.text.toString()
        val category = updateAutoCompleteTextView.text.toString()

        //Getting values for notifyDay and notifyHour from the checkboxes
        val notifyMinutes: Boolean = updateCheckboxMinutesBefore.isChecked
        val notifyHour: Boolean = updateCheckBoxHourBefore.isChecked
        val notifyDay: Boolean = updateCheckBoxDayBefore.isChecked

        //Splitting date and time values into an array
        val valuesArrayListDate2 = dateString.split("/")
        val valuesArrayListTime2 = timeString.split(":")

        //Getting values for day, month and year from the date values array
        val day2 = valuesArrayListDate2[0]
        val month2 = valuesArrayListDate2[1]
        val year2 = valuesArrayListDate2[2]

        //Getting values for hour and minute from the time values array
        val hour = valuesArrayListTime2[0]
        val minute = valuesArrayListTime2[1]

        //Setting time values for the 2 possible scenarios
        val regularTime = "$hour:$minute"                           //Normal time (example 10:00)
        val hourMissingZeroTime = "0$hour:$minute"                  //Hour value is only one digit (example 9:00) so we add a 0 in front of it
        val minuteMissingZeroTime = "$hour:0$minute"
        val bothHourAndMinuteMissingZeroTime = "0$hour:0$minute"

        //Initializing variables
        var dayName = ""
        val date = "$day2/$month2/$year2"
        var time = ""
        val status: Boolean

        //Getting the day value based on the given due date
        val getTaskDayName = LocalDate.of(year2.toInt(), month2.toInt(), day2.toInt()).dayOfWeek.toString()

        //Changing the day name value from English to Finnish
        if (getTaskDayName == "MONDAY")     { dayName = "Maanantai" }
        if (getTaskDayName == "TUESDAY")    { dayName = "Tiistai" }
        if (getTaskDayName == "WEDNESDAY")  { dayName = "Keskiviikko" }
        if (getTaskDayName == "THURSDAY")   { dayName = "Torstai" }
        if (getTaskDayName == "FRIDAY")     { dayName = "Perjantai" }
        if (getTaskDayName == "SATURDAY")   { dayName = "Lauantai" }
        if (getTaskDayName == "SUNDAY")     { dayName = "Sunnuntai" }

        //Checking if header field is empty
        if (checkHeader(header) && checkTime(timeString) && checkDate(dateString) && checkCategory(category))
        {
            if (!isDateInThePast(dateString, timeString)) {
                //Normal time 10:00
                if (timeCheck1(timeString)) { time = regularTime }

                //Time with only 1 digit in hour field (1:00)
                if (timeCheck2(timeString)) { time = hourMissingZeroTime }

                //Time with only 1 digit in minute field (1:0) (Timepicker assigns only one digit to minute value if the value is for example 12:00)
                if (timeCheck3(timeString)) { time = minuteMissingZeroTime }

                //Time with 1 digit in both hour and minute field
                if (timeCheck4(timeString)) { time = bothHourAndMinuteMissingZeroTime }

                //Setting the status variable based on the checkbox state
                status = checkBox.isChecked

                //updating the task with the given values and changing back to listfragment
                val updatedTask = Task(args.currentTask.id, header, time, date, dayName, details, category, status, notifyMinutes, notifyHour, notifyDay, importance)
                mTaskViewModel.updateTask(updatedTask)
                //Toast.makeText(requireContext(), "Tehtävä päivitetty", Toast.LENGTH_SHORT).show()

                //Navigating back to list fragment
                findNavController().navigate(R.id.action_updateFragment_to_listFragment)

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

    //Function for deleting a single task
    private fun deleteTask(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Kyllä")
        {_, _ ->
            mTaskViewModel.deleteTask(args.currentTask)
            cancelAlarm(args.currentTask.id)
            //Toast.makeText(requireContext(), "Tehtävä poistettu", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("Ei")
        {_, _ -> }
        builder.setTitle("Poista ${args.currentTask.header}?")
        if (args.currentTask.status) {
            builder.setMessage("Haluatko varmasti poistaa tehtävän ${args.currentTask.header}?")
        } else {
            builder.setMessage("Haluatko varmasti poistaa tehtävän ${args.currentTask.header} vaikka sitä ei ole merkitty tehdyksi?")
        }
        builder.create().show()
    }
    var alarmItem: AlarmItem? = null
    val scheduler by lazy { AndroidAlarmScheduler(requireContext()) }

    private fun cancelAlarm(id: Int) {
        alarmItem = AlarmItem(
            message = id.toString(),
            time = LocalDateTime.now()
        )
        alarmItem?.let(scheduler::cancel)
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