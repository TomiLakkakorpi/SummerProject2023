package com.example.databasetest.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databasetest.R
import com.example.databasetest.databinding.FragmentListBinding
import com.example.databasetest.model.Task
import com.example.databasetest.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_list.view.*
import java.time.LocalDate
import java.util.*

class ListFragment : Fragment() {

    //Initializing variables
    private var _binding: FragmentListBinding? = null
    private var taskList = emptyList<Task>()
    private lateinit var mTaskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val adapter = ListAdapter()
        val recyclerView = view.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        mTaskViewModel.readAllData.observe(viewLifecycleOwner, Observer { task ->
            adapter.setData(task)
        })

        //Navigating to add fragment when add button is pressed
        view.floatingActionButtonAdd.setOnClickListener{
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        //Calling delete all tasks function when delete all button is pressed
        view.floatingActionButtonDelete.setOnClickListener {
            deleteAllTasks()
        }

        //Getting local date and seperating it into year, month and day values
        val today = LocalDate.now().toString().split("-")
        val currentYear = today[0]
        val currentMonth = today[1]
        val currentDay = today[2]

        //Getting the dayOfWeek value with the year, month & day values from above
        val getDayName = LocalDate.of(currentYear.toInt(), currentMonth.toInt(), currentDay.toInt()).dayOfWeek.toString()

        //Initializing some strings
        var dayName = ""
        var monthName = ""

        //Setting the string values
        if (currentMonth == "01")     { monthName = "Tammikuuta" }
        if (currentMonth == "02")     { monthName = "Helmikuuta" }
        if (currentMonth == "03")     { monthName = "Maaliskuuta" }
        if (currentMonth == "04")     { monthName = "Huhtikuuta" }
        if (currentMonth == "05")     { monthName = "Toukokuuta" }
        if (currentMonth == "06")     { monthName = "Kesäkuuta" }
        if (currentMonth == "07")     { monthName = "Heinäkuuta" }
        if (currentMonth == "08")     { monthName = "Elokuuta" }
        if (currentMonth == "09")     { monthName = "Syyskuuta" }
        if (currentMonth == "10")     { monthName = "Lokakuuta" }
        if (currentMonth == "11")     { monthName = "Marraskuuta" }
        if (currentMonth == "12")     { monthName = "Joulukuuta" }

        if (getDayName == "MONDAY")     { dayName = "Maanantai" }
        if (getDayName == "TUESDAY")    { dayName = "Tiistai" }
        if (getDayName == "WEDNESDAY")  { dayName = "Keskiviikko" }
        if (getDayName == "THURSDAY")   { dayName = "Torstai" }
        if (getDayName == "FRIDAY")     { dayName = "Perjantai" }
        if (getDayName == "SATURDAY")   { dayName = "Lauantai" }
        if (getDayName == "SUNDAY")     { dayName = "Sunnuntai" }

        //Combining all values into one string and displaying it in the list view
        val todaysDate = "$dayName $currentDay. $monthName"
        view.tvListScreenTodaysDate.setText(todaysDate)

        _binding = FragmentListBinding.inflate(inflater, container, false)
        val categories = resources.getStringArray(R.array.categories)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categories)
        view.findViewById<AutoCompleteTextView>(R.id.CategoryFilter).setAdapter(arrayAdapter)

        return view
    }

    //Function to delete all tasks that are marked as complete (SQL query has a check where it only deletes tasks that are marked as complete)
    private fun deleteAllTasks() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Kyllä")
        {_, _ ->
            mTaskViewModel.deleteAllTasks()
            Toast.makeText(requireContext(), "Kaikki tehdyiksi merkityt tehtävät poistettiin onnistuneesti", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Ei")
        {_, _ ->
            Toast.makeText(requireContext(),"Tehtäviä ei poistettu", Toast.LENGTH_SHORT).show()
        }
        builder.setTitle("Poista kaikki tehtävät?")
        builder.setMessage("Haluatko varmasti poistaa kaikki tehdyt tehtävät?")
        builder.create().show()
    }
}