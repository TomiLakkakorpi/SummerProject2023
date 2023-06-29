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
import kotlinx.android.synthetic.main.custom_row.view.*
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var mTaskViewModel: TaskViewModel
    private var _binding: FragmentListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update, container, false)
        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        val dateValues = args.currentTask.date
        val valuesArrayList = dateValues.split("/")
        val year = valuesArrayList[0]
        val month = valuesArrayList[1]
        val day = valuesArrayList[2]

        val timeValues = args.currentTask.time
        val valuesArrayList2 = timeValues.split(":")
        val hour = valuesArrayList2[0]
        val minute = valuesArrayList2[1]

        if (day.startsWith("0")) {
            val newDay = day.drop(1)
            val dateValue1 = "$newDay/$month/$year"
            view.etEditScreenDate.setText(dateValue1)
        }

        if (month.startsWith("0")) {
            val newMonth = month.drop(1)
            val dateValue2 = "$day/$newMonth/$year"
            view.etEditScreenDate.setText(dateValue2)
        }

        if (month.startsWith("0") && day.startsWith("0")) {
            val newMonth = month.drop(1)
            val newDay = day.drop(1)
            val dateValue3 = "$newDay/$newMonth/$year"
            view.etEditScreenDate.setText(dateValue3)

        } else {
            val dateValue4 = "$day/$month/$year"
            view.etEditScreenDate.setText(dateValue4)
        }

        if(hour.startsWith("0")) {
            val newHour = hour.drop(1)
            val timeValue1 = "$newHour:$minute"
            view.etEditScreenTime.setText(timeValue1)
        } else {
            val timeValue2 = "$hour:$minute"
            view.etEditScreenTime.setText(timeValue2)
        }

        view.etEditScreenHeader.setText(args.currentTask.header)
        view.etEditScreenDay.setText(args.currentTask.dayName)
        view.etEditScreenDetails.setText(args.currentTask.details)
        view.autoCompleteTextView2.setText(args.currentTask.category)

        if (args.currentTask.status) {
            view.checkBox.setChecked(true)
        } else {
            view.checkBox.setChecked(false)
        }

        view.updateScreenUpdate.setOnClickListener {
            updateItem()
        }

        view.updateScreenDelete.setOnClickListener {
            deleteTask()
        }

        view.updateScreenCancel.setOnClickListener {
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            Toast.makeText(requireContext(),"Tehtävää ei päivitetty", Toast.LENGTH_SHORT).show()
        }

        _binding = FragmentListBinding.inflate(inflater, container, false)
        val categories = resources.getStringArray(R.array.categories)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categories)
        view.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2).setAdapter(arrayAdapter)

        return view
    }

    private fun updateItem(){
        val header = etEditScreenHeader.text.toString()
        val timeTest = etEditScreenTime.text.toString()
        val dateTest = etEditScreenDate.text.toString()
        val dayName = etEditScreenDay.text.toString()
        val details = etEditScreenDetails.text.toString()
        val category = autoCompleteTextView2.text.toString()

        if (checkHeader(header))
        {
            if (checkTime(timeTest))
            {
                if (timeCheck1(timeTest) || timeCheck2(timeTest))
                {
                    if (checkDate(dateTest))
                    {
                        if (dateCheck1(dateTest) || dateCheck2(dateTest) || dateCheck3(dateTest) || dateCheck4(dateTest))
                        {
                            if (checkDayName(dayName))
                            {
                                if (checkCategory(category))
                                {
                                    val dateValues = dateTest
                                    val valuesArrayList = dateValues.split("/")

                                    val timeValues = timeTest
                                    val valuesArrayList2 = timeValues.split(":")

                                    //Normal date 11/11/23
                                    if (dateCheck1(dateTest)) {
                                        val day = valuesArrayList[0]
                                        val month = valuesArrayList[1]
                                        val year = valuesArrayList[2]
                                        val date = "$year/$month/$day"

                                        //Normal time 10:00
                                        if (timeCheck1(timeTest)) {
                                            val hour = valuesArrayList2[0]
                                            val minute = valuesArrayList2[1]
                                            val time1 = "$hour:$minute"
                                            if (checkBox.isChecked) {
                                                val updatedTask = Task(args.currentTask.id, header, time1, date, dayName, details, category, status = true)
                                                mTaskViewModel.updateTask(updatedTask)
                                            } else {
                                                val updatedTask = Task(args.currentTask.id, header, time1, date, dayName, details, category, status = false)
                                                mTaskViewModel.updateTask(updatedTask)
                                            }
                                            Toast.makeText(requireContext(), "Tehtävä päivitetty", Toast.LENGTH_SHORT).show()
                                            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
                                        }

                                        //Time with only 1 digit in hour field (1:00)
                                        if (timeCheck2(timeTest)) {
                                            val newHour = "0" + valuesArrayList2[0]
                                            val minute = valuesArrayList2[1]
                                            val time2 = "$newHour:$minute"
                                            if (checkBox.isChecked) {
                                                val updatedTask = Task(args.currentTask.id, header, time2, date, dayName, details, category, status = true)
                                                mTaskViewModel.updateTask(updatedTask)
                                            } else {
                                                val updatedTask = Task(args.currentTask.id, header, time2, date, dayName, details, category, status = false)
                                                mTaskViewModel.updateTask(updatedTask)
                                            }
                                            Toast.makeText(requireContext(), "Tehtävä päivitetty", Toast.LENGTH_SHORT).show()
                                            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
                                        }
                                    }

                                    //Date like 1/11/23
                                    if (dateCheck2(dateTest)) {
                                        val day = "0" + valuesArrayList[0]
                                        val month = valuesArrayList[1]
                                        val year = valuesArrayList[2]
                                        val date = "$year/$month/$day"

                                        //Normal time 10:00
                                        if (timeCheck1(timeTest)) {
                                            val hour = valuesArrayList2[0]
                                            val minute = valuesArrayList2[1]
                                            val time = "$hour:$minute"
                                            if (checkBox.isChecked) {
                                                val updatedTask = Task(args.currentTask.id, header, time, date, dayName, details, category, status = true)
                                                mTaskViewModel.updateTask(updatedTask)
                                            } else {
                                                val updatedTask = Task(args.currentTask.id, header, time, date, dayName, details, category, status = false)
                                                mTaskViewModel.updateTask(updatedTask)
                                            }
                                            Toast.makeText(requireContext(), "Tehtävä päivitetty", Toast.LENGTH_SHORT).show()
                                            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
                                        }

                                        //Time with only 1 digit in hour field (1:00)
                                        if (timeCheck2(timeTest)) {
                                            val newHour = "0" + valuesArrayList2[0]
                                            val minute = valuesArrayList2[1]
                                            val time = "$newHour:$minute"
                                            if (checkBox.isChecked) {
                                                val updatedTask = Task(args.currentTask.id, header, time, date, dayName, details, category, status = true)
                                                mTaskViewModel.updateTask(updatedTask)
                                            } else {
                                                val updatedTask = Task(args.currentTask.id, header, time, date, dayName, details, category, status = false)
                                                mTaskViewModel.updateTask(updatedTask)
                                            }
                                            Toast.makeText(requireContext(), "Tehtävä päivitetty", Toast.LENGTH_SHORT).show()
                                            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
                                        }
                                    }

                                    //Date like 11/1/23
                                    if (dateCheck3(dateTest)) {
                                        val day = valuesArrayList[0]
                                        val month = "0" + valuesArrayList[1]
                                        val year = valuesArrayList[2]
                                        val date = "$year/$month/$day"

                                        //Normal time 10:00
                                        if (timeCheck1(timeTest)) {
                                            val hour = valuesArrayList2[0]
                                            val minute = valuesArrayList2[1]
                                            val time = "$hour:$minute"
                                            if (checkBox.isChecked) {
                                                val updatedTask = Task(args.currentTask.id, header, time, date, dayName, details, category, status = true)
                                                mTaskViewModel.updateTask(updatedTask)
                                            } else {
                                                val updatedTask = Task(args.currentTask.id, header, time, date, dayName, details, category, status = false)
                                                mTaskViewModel.updateTask(updatedTask)
                                            }
                                            Toast.makeText(requireContext(), "Tehtävä päivitetty", Toast.LENGTH_SHORT).show()
                                            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
                                        }

                                        //Time with only 1 digit in hour field (1:00)
                                            if (timeCheck2(timeTest)) {
                                            val newHour = "0" + valuesArrayList2[0]
                                            val minute = valuesArrayList2[1]
                                            val time = "$newHour:$minute"
                                            if (checkBox.isChecked) {
                                                val updatedTask = Task(args.currentTask.id, header, time, date, dayName, details, category, status = true)
                                                mTaskViewModel.updateTask(updatedTask)
                                            } else {
                                                val updatedTask = Task(args.currentTask.id, header, time, date, dayName, details, category, status = false)
                                                mTaskViewModel.updateTask(updatedTask)
                                            }
                                            Toast.makeText(requireContext(), "Tehtävä päivitetty", Toast.LENGTH_SHORT).show()
                                            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
                                        }
                                    }

                                    //Date like 1/1/23
                                    if (dateCheck4(dateTest)) {
                                        val day = "0" + valuesArrayList[0]
                                        val month = "0" + valuesArrayList[1]
                                        val year = valuesArrayList[2]
                                        val date = "$year/$month/$day"

                                        //Normal time 10:00
                                        if (timeCheck1(timeTest)) {
                                            val hour = valuesArrayList2[0]
                                            val minute = valuesArrayList2[1]
                                            val time = "$hour:$minute"
                                            if (checkBox.isChecked) {
                                                val updatedTask = Task(args.currentTask.id, header, time, date, dayName, details, category, status = true)
                                                mTaskViewModel.updateTask(updatedTask)
                                            } else {
                                                val updatedTask = Task(args.currentTask.id, header, time, date, dayName, details, category, status = false)
                                                mTaskViewModel.updateTask(updatedTask)
                                            }
                                            Toast.makeText(requireContext(), "Tehtävä päivitetty", Toast.LENGTH_SHORT).show()
                                            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
                                        }

                                        //Time with only 1 digit in hour field (1:00)
                                        if (timeCheck2(timeTest)) {
                                            val newHour = "0" + valuesArrayList2[0]
                                            val minute = valuesArrayList2[1]
                                            val time = "$newHour:$minute"
                                            if (checkBox.isChecked) {
                                                val updatedTask = Task(args.currentTask.id, header, time, date, dayName, details, category, status = true)
                                                mTaskViewModel.updateTask(updatedTask)
                                            } else {
                                                val updatedTask = Task(args.currentTask.id, header, time, date, dayName, details, category, status = false)
                                                mTaskViewModel.updateTask(updatedTask)
                                            }
                                            Toast.makeText(requireContext(), "Tehtävä päivitetty", Toast.LENGTH_SHORT).show()
                                            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
                                        }
                                    }
                                } else {Toast.makeText(requireContext(), "Valitse kategoria", Toast.LENGTH_SHORT).show()}
                            } else {Toast.makeText(requireContext(), "Syötä päivä esim. Maanantai", Toast.LENGTH_SHORT).show()}
                        } else {Toast.makeText(requireContext(), "$dateTest Syötä päivämäärä muodossa päivä/kuukausi/vuosi", Toast.LENGTH_SHORT).show()}
                    } else {Toast.makeText(requireContext(), "Syötä päivämäärä", Toast.LENGTH_SHORT).show()}
                } else {Toast.makeText(requireContext(), "Syötä aika muodossa tunnit:minuutit", Toast.LENGTH_SHORT).show()}
            } else {Toast.makeText(requireContext(), "Syötä Kellonaika", Toast.LENGTH_SHORT).show()}
        } else {Toast.makeText(requireContext(), "Syötä otsikko", Toast.LENGTH_SHORT).show()}
    }

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
       builder.setMessage("Haluatko varmasti poistaa tehtävän ${args.currentTask.header}?")
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

    //Checking that dayName field isn't empty
    private fun checkDayName(dayName: String): Boolean {
        return !(TextUtils.isEmpty(dayName))
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