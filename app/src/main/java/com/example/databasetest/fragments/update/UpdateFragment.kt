package com.example.databasetest.fragments.update

import android.app.AlertDialog
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
import com.example.databasetest.databinding.FragmentListBinding
import com.example.databasetest.model.Task
import com.example.databasetest.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*
import java.time.LocalDate

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

    //Function for updating a task
    private fun updateItem(){

        //Getting values from "edit screen" edittext fields
        val header = etEditScreenHeader.text.toString()
        val timeString = etEditScreenTime.text.toString()
        val dateString = etEditScreenDate.text.toString()
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
        val regularTime = "$hour:$minute"           //Normal time (example 10:00)
        val hourMissingZeroTime = "0$hour:$minute"  //Hour value is only one digit (example 9:00) so we add a 0 in front of it

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
                if (timeCheck1(timeString) || timeCheck2(timeString))
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