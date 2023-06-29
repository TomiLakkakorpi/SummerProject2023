package com.example.databasetest.fragments.add

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.databasetest.R
import com.example.databasetest.databinding.FragmentListBinding
import com.example.databasetest.model.Task
import com.example.databasetest.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*

class AddFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private lateinit var mTaskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        view.addScreenAdd.setOnClickListener {
            insertDataToDatabase()
        }

        view.addScreenCancel.setOnClickListener {
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
            Toast.makeText(requireContext(), "Tehtävää ei tallennettu", Toast.LENGTH_SHORT).show()
        }

        _binding = FragmentListBinding.inflate(inflater, container, false)
        val categories = resources.getStringArray(R.array.categories)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categories)
        view.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView).setAdapter(arrayAdapter)

        return view
    }

    private fun insertDataToDatabase() {
        val header = etAddScreenHeader.text.toString()
        val time = etAddScreenTime.text.toString()
        val dateTest = etAddScreenDate.text.toString()
        val dayName = etAddScreenDay.text.toString()
        val details = etAddScreenDetails.text.toString()
        val category = autoCompleteTextView.text.toString()

            if (checkHeader(header))
            {
                if (checkTime(time))
                {
                    var timeString = etAddScreenTime.text.toString()
                    if (timeCheck1(timeString) || timeCheck2(timeString))
                    {
                        if (checkDate(dateTest))
                        {
                            var dateString = etAddScreenDate.text.toString()
                            if (dateCheck1(dateString) || dateCheck2(dateString) || dateCheck3(dateString) || dateCheck4(dateString))
                            {
                                if (checkDayName(dayName))
                                {
                                    if (checkCategory(category))
                                    {
                                        val dateValues = etAddScreenDate.text.toString()
                                        val valuesArrayList = dateValues.split("/")

                                        val timeValues = etAddScreenTime.text.toString()
                                        val valuesArrayList2 = timeValues.split(":")

                                        //Normal date 11/11/23
                                        if (dateCheck1(dateString)) {
                                            val day = valuesArrayList[0]
                                            val month = valuesArrayList[1]
                                            val year = valuesArrayList[2]
                                            val date = "$year/$month/$day"

                                            //Normal time 10:00
                                            if (timeCheck1(timeString)) {
                                                val hour = valuesArrayList2[0]
                                                val minute = valuesArrayList2[1]
                                                val time1 = "$hour:$minute"
                                                val task = Task(0, header, time1, date, dayName, details, category, status = false)
                                                mTaskViewModel.addTask(task)
                                                Toast.makeText(requireContext(), "Tehtävä tallennettu", Toast.LENGTH_SHORT).show()
                                                findNavController().navigate(R.id.action_addFragment_to_listFragment)
                                            }

                                            //Time with only 1 digit in hour field (1:00)
                                            if (timeCheck2(timeString)) {
                                                val newHour = "0" + valuesArrayList2[0]
                                                val minute = valuesArrayList2[1]
                                                val time2 = "$newHour:$minute"
                                                val task = Task(0, header, time2, date, dayName, details, category, status = false)
                                                mTaskViewModel.addTask(task)
                                                Toast.makeText(requireContext(), "Tehtävä tallennettu", Toast.LENGTH_SHORT).show()
                                                findNavController().navigate(R.id.action_addFragment_to_listFragment)
                                            }
                                        }

                                        //Date like 1/11/23
                                        if (dateCheck2(dateString)) {
                                            val day = "0" + valuesArrayList[0]
                                            val month = valuesArrayList[1]
                                            val year = valuesArrayList[2]
                                            val date = "$year/$month/$day"

                                            //Normal time 10:00
                                            if (timeCheck1(timeString)) {
                                                val hour = valuesArrayList2[0]
                                                val minute = valuesArrayList2[1]
                                                val time1 = "$hour:$minute"
                                                val task = Task(0, header, time1, date, dayName, details, category, status = false)
                                                mTaskViewModel.addTask(task)
                                                Toast.makeText(requireContext(), "Tehtävä tallennettu", Toast.LENGTH_SHORT).show()
                                                findNavController().navigate(R.id.action_addFragment_to_listFragment)
                                            }

                                            //Time with only 1 digit in hour field (1:00)
                                            if (timeCheck2(timeString)) {
                                                val newHour = "0" + valuesArrayList2[0]
                                                val minute = valuesArrayList2[1]
                                                val time2 = "$newHour:$minute"
                                                val task = Task(0, header, time2, date, dayName, details, category, status = false)
                                                mTaskViewModel.addTask(task)
                                                Toast.makeText(requireContext(), "Tehtävä tallennettu", Toast.LENGTH_SHORT).show()
                                                findNavController().navigate(R.id.action_addFragment_to_listFragment)
                                            }
                                        }

                                        //Date like 11/1/23
                                        if (dateCheck3(dateString)) {
                                            val day = valuesArrayList[0]
                                            val month = "0" + valuesArrayList[1]
                                            val year = valuesArrayList[2]
                                            val date = "$year/$month/$day"

                                            //Normal time 10:00
                                            if (timeCheck1(timeString)) {
                                                val hour = valuesArrayList2[0]
                                                val minute = valuesArrayList2[1]
                                                val time1 = "$hour:$minute"
                                                val task = Task(0, header, time1, date, dayName, details, category, status = false)
                                                mTaskViewModel.addTask(task)
                                                Toast.makeText(requireContext(), "Tehtävä tallennettu", Toast.LENGTH_SHORT).show()
                                                findNavController().navigate(R.id.action_addFragment_to_listFragment)
                                            }

                                            //Time with only 1 digit in hour field (1:00)
                                            if (timeCheck2(timeString)) {
                                                val newHour = "0" + valuesArrayList2[0]
                                                val minute = valuesArrayList2[1]
                                                val time2 = "$newHour:$minute"
                                                val task = Task(0, header, time2, date, dayName, details, category, status = false)
                                                mTaskViewModel.addTask(task)
                                                Toast.makeText(requireContext(), "Tehtävä tallennettu", Toast.LENGTH_SHORT).show()
                                                findNavController().navigate(R.id.action_addFragment_to_listFragment)
                                            }
                                        }

                                        //Date like 1/1/23
                                        if (dateCheck4(dateString)) {
                                            val day = "0" + valuesArrayList[0]
                                            val month = "0" + valuesArrayList[1]
                                            val year = valuesArrayList[2]
                                            val date = "$year/$month/$day"

                                            //Normal time 10:00
                                            if (timeCheck1(timeString)) {
                                                val hour = valuesArrayList2[0]
                                                val minute = valuesArrayList2[1]
                                                val time1 = "$hour:$minute"
                                                val task = Task(0, header, time1, date, dayName, details, category, status = false)
                                                mTaskViewModel.addTask(task)
                                                Toast.makeText(requireContext(), "Tehtävä tallennettu", Toast.LENGTH_SHORT).show()
                                                findNavController().navigate(R.id.action_addFragment_to_listFragment)
                                            }

                                            //Time with only 1 digit in hour field (1:00)
                                            if (timeCheck2(timeString)) {
                                                val newHour = "0" + valuesArrayList2[0]
                                                val minute = valuesArrayList2[1]
                                                val time2 = "$newHour:$minute"
                                                val task = Task(0, header, time2, date, dayName, details, category, status = false)
                                                mTaskViewModel.addTask(task)
                                                Toast.makeText(requireContext(), "Tehtävä tallennettu", Toast.LENGTH_SHORT).show()
                                                findNavController().navigate(R.id.action_addFragment_to_listFragment)
                                            }
                                        }
                                    } else {Toast.makeText(requireContext(), "Valitse kategoria", Toast.LENGTH_SHORT).show()}
                                } else {Toast.makeText(requireContext(), "Syötä päivä esim. Maanantai", Toast.LENGTH_SHORT).show()}
                            } else {Toast.makeText(requireContext(), "Syötä päivämäärä muodossa päivä/kuukausi/vuosi", Toast.LENGTH_SHORT).show()}
                        } else {Toast.makeText(requireContext(), "Syötä päivämäärä", Toast.LENGTH_SHORT).show()}
                    } else {Toast.makeText(requireContext(), "Syötä aika muodossa tunnit:minuutit", Toast.LENGTH_SHORT).show()}
                } else {Toast.makeText(requireContext(), "Syötä kellonaika", Toast.LENGTH_SHORT).show()}
            } else {Toast.makeText(requireContext(), "Syötä Otsikko", Toast.LENGTH_SHORT).show()}
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
        val regex = Regex("\\d{1}:\\d{2}")
        return str.matches(regex)
    }

    //Checking input and matching for dates like 11/11/23
    private fun dateCheck1(str: String): Boolean {
        val regex = Regex("\\d{2}/\\d{2}/\\d{2}")
        return str.matches(regex)
    }

    //Checking input and matching for dates like 1/11/23
    private fun dateCheck2(str: String): Boolean {
        val regex = Regex("\\d{1}/\\d{2}/\\d{2}")
        return str.matches(regex)
    }

    //Checking input and matching for dates like 11/1/23
    private fun dateCheck3(str: String): Boolean {
        val regex = Regex("\\d{2}/\\d{1}/\\d{2}")
        return str.matches(regex)
    }

    //Checking input and matching for dates like 1/1/23
    private fun dateCheck4(str: String): Boolean {
        val regex = Regex("\\d{1}/\\d{1}/\\d{2}")
        return str.matches(regex)
    }
}