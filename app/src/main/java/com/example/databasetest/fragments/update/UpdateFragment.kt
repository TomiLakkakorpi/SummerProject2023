package com.example.databasetest.fragments.update

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateFormat
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.databasetest.R
import com.example.databasetest.databinding.FragmentListBinding
import com.example.databasetest.model.Task
import com.example.databasetest.viewmodel.TaskViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
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
        val year = valuesArrayListDate[0]
        val month = valuesArrayListDate[1]
        val day = valuesArrayListDate[2]

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
        view.autoCompleteTextView2.setText(args.currentTask.category)

        //Setting the task status checkbox based on its value in database
        if (args.currentTask.status) {
            view.checkBox.setChecked(true)
        } else {
            view.checkBox.setChecked(false)
        }

        //Setting the day checkbox in edit screen based on its value in database
        if (args.currentTask.notifyDay) {
            view.editCheckboxDayBefore.setChecked(true)
        } else {
            view.editCheckboxDayBefore.setChecked(false)
        }

        //Setting the hour checkbox in edit screen based on its value in database
        if (args.currentTask.notifyHour) {
            view.editCheckBoxHourBefore.setChecked(true)
        } else {
            view.editCheckBoxHourBefore.setChecked(false)
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

        //Changing from updateFragment to listFragment when "cancel" button is pressed
        view.updateScreenCancel.setOnClickListener {
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            Toast.makeText(requireContext(),"Tehtävää ei päivitetty", Toast.LENGTH_SHORT).show()
        }

        //Initializing categoryfilter dropdown menu
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val categories = resources.getStringArray(R.array.categories)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categories)
        view.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2).setAdapter(arrayAdapter)

        return view
    }

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
        val category = autoCompleteTextView2.text.toString()

        //Getting values for notifyDay and notifyHour from the checkboxes
        val notifyDay: Boolean = editCheckboxDayBefore.isChecked
        val notifyHour: Boolean = editCheckBoxHourBefore.isChecked

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

        //Setting date values for the 4 scenarios
        val regularDate = "$year2/$month2/$day2"                    //normal date (example 10/10/23)
        val dayMissingZeroDate = "$year2/$month2/0$day2"            //day has only one digit (example 1/10/23) so we add 0 in front of it
        val monthMissingZeroDate = "$year2/0$month2/$day2"          //month has only one digit (example 10/1/23) so we add 0 in front of it
        val dayAndMonthMissingZeroDate = "$year2/0$month2/0$day2"   //both day and month have only one digit (example 1/1/23) so we add 0 in front of both of them

        //Setting time values for the 2 possible scenarios
        val regularTime = "$hour:$minute"                           //Normal time (example 10:00)
        val hourMissingZeroTime = "0$hour:$minute"                  //Hour value is only one digit (example 9:00) so we add a 0 in front of it
        val minuteMissingZeroTime = "$hour:0$minute"
        val bothHourAndMinuteMissingZeroTime = "0$hour:0$minute"

        //Initializing variables
        var dayName = ""
        var date = ""
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
        if (checkHeader(header))
        {
            //Checking if time field is empty
            if (checkTime(timeString))
            {
                //Calling functions to check that the given time matches either of the accepted formats
                if (timeCheck1(timeString) || timeCheck2(timeString) || timeCheck3(timeString) || timeCheck4(timeString))
                {
                    //Checking if date field is empty
                    if (checkDate(dateString))
                    {
                        //Calling functions to check that the given date matches any of the accepted formats
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
                                if (timeCheck4(timeString)) { time = bothHourAndMinuteMissingZeroTime }

                                //Setting the status variable based on the checkbox state
                                status = checkBox.isChecked

                                //updating the task with the given values and changing back to listfragment
                                val updatedTask = Task(args.currentTask.id, header, time, date, dayName, details, category, status, notifyDay, notifyHour)
                                mTaskViewModel.updateTask(updatedTask)
                                Toast.makeText(requireContext(), "Tehtävä päivitetty", Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_updateFragment_to_listFragment)

                                //Different toast messages for different errors
                            } else {Toast.makeText(requireContext(), "Valitse kategoria", Toast.LENGTH_SHORT).show()}
                        } else {Toast.makeText(requireContext(), "Syötä päivämäärä muodossa päivä/kuukausi/vuosi", Toast.LENGTH_SHORT).show()}
                    } else {Toast.makeText(requireContext(), "Syötä päivämäärä", Toast.LENGTH_SHORT).show()}
                } else {Toast.makeText(requireContext(), "Syötä aika muodossa tunnit:minuutit", Toast.LENGTH_SHORT).show()}
            } else {Toast.makeText(requireContext(), "Syötä Kellonaika", Toast.LENGTH_SHORT).show()}
        } else {Toast.makeText(requireContext(), "Syötä otsikko", Toast.LENGTH_SHORT).show()}
    }

    //Function for deleting a single task
    private fun deleteTask(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Kyllä")
        {_, _ ->
            mTaskViewModel.deleteTask(args.currentTask)
            Toast.makeText(requireContext(), "Tehtävä poistettu", Toast.LENGTH_SHORT).show()
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

    //Checking input and matching for dates like 11/11/2023
    private fun dateCheck1(str: String): Boolean {
        val regex = Regex("\\d{2}/\\d{2}/\\d{2}")
        return str.matches(regex)
    }

    //Checking input and matching for dates like 1/11/2023
    private fun dateCheck2(str: String): Boolean {
        val regex = Regex("\\d/\\d{2}/\\d{2}")
        return str.matches(regex)
    }

    //Checking input and matching for dates like 11/1/2023
    private fun dateCheck3(str: String): Boolean {
        val regex = Regex("\\d{2}/\\d/\\d{2}")
        return str.matches(regex)
    }

    //Checking input and matching for dates like 1/1/2023
    private fun dateCheck4(str: String): Boolean {
        val regex = Regex("\\d/\\d/\\d{2}")
        return str.matches(regex)
    }
}