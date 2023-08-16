package com.example.databasetest.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databasetest.R
import com.example.databasetest.databinding.FragmentListBinding
import com.example.databasetest.model.Task
import com.example.databasetest.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.custom_row.*
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

class ListFragment : Fragment() {

    //Initializing variables
    private var _binding: FragmentListBinding? = null
    private lateinit var mTaskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Initializing views, adapters etc.
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val adapter = ListAdapter()
        val recyclerView = view.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        mTaskViewModel.readAllData.observe(viewLifecycleOwner, Observer { task ->
            task.let {adapter.setData(it) }
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

        //Setting the string values for current date
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
        if (currentDay.startsWith("0")) {
            val newCurrentDay = currentDay.drop(1)
            val todaysDate = "$dayName $newCurrentDay. $monthName"
            view.tvListScreenTodaysDate.setText(todaysDate)
        } else {
            val todaysDate = "$dayName $currentDay. $monthName"
            view.tvListScreenTodaysDate.setText(todaysDate)
        }

        //default setting for filter is show all tasks
        val presetCategoryText = "Näytä kaikki"
        view.etCategoryFilter.setText(presetCategoryText)

        //Changing to the next filter when right button is pressed
        view.floatingActionButtonNavigateRight.setOnClickListener {
            val string = view.etCategoryFilter.text.toString()
            moveCategoryRight(string)
        }

        //Changing to the next filter when left button is pressed
        view.floatingActionButtonNavigateLeft.setOnClickListener {
            val string = view.etCategoryFilter.text.toString()
            moveCategoryLeft(string)
        }

        _binding = FragmentListBinding.inflate(inflater, container, false)

        //Setting the category filter when category filter edittext is changed
        view.etCategoryFilter.addTextChangedListener {text: Editable? ->
            if (text != null) mTaskViewModel.setFilter(text.toString())
        }

        return view
    }

    //Going through the categories from top down
    private fun moveCategoryRight(string: String) {
        if (string == "Näytä kaikki") { view?.etCategoryFilter?.setText(R.string.category1) }
        if (string == "Liikunta") { view?.etCategoryFilter?.setText(R.string.category2) }
        if (string == "Terveys") { view?.etCategoryFilter?.setText(R.string.category3) }
        if (string == "Koulu") { view?.etCategoryFilter?.setText(R.string.category4) }
        if (string == "Työ") { view?.etCategoryFilter?.setText(R.string.category5) }
        if (string == "Tärkeä") { view?.etCategoryFilter?.setText(R.string.category6) }
        if (string == "Talous") { view?.etCategoryFilter?.setText(R.string.category7) }
        if (string == "Askare") { view?.etCategoryFilter?.setText(R.string.category8) }
        if (string == "Tapaaminen") { view?.etCategoryFilter?.setText(R.string.category9) }
        if (string == "Pelit") { view?.etCategoryFilter?.setText(R.string.category10) }
        if (string == "Jääkiekko") { view?.etCategoryFilter?.setText(R.string.category11) }
        if (string == "Formula 1") { view?.etCategoryFilter?.setText(R.string.category12) }
        if (string == "eSports") { view?.etCategoryFilter?.setText(R.string.category13) }
        if (string == "Matkailu") { view?.etCategoryFilter?.setText(R.string.category14) }
        if (string == "Muu") { view?.etCategoryFilter?.setText(R.string.allCategories) }
    }

    //Going through the categories from bottom up
    private fun moveCategoryLeft(string: String) {
        if (string == "Näytä kaikki") { view?.etCategoryFilter?.setText(R.string.category14) }
        if (string == "Muu") { view?.etCategoryFilter?.setText(R.string.category13) }
        if (string == "Matkailu") { view?.etCategoryFilter?.setText(R.string.category12) }
        if (string == "eSports") { view?.etCategoryFilter?.setText(R.string.category11) }
        if (string == "Formula 1") { view?.etCategoryFilter?.setText(R.string.category10) }
        if (string == "Jääkiekko") { view?.etCategoryFilter?.setText(R.string.category9) }
        if (string == "Pelit") { view?.etCategoryFilter?.setText(R.string.category8) }
        if (string == "Tapaaminen") { view?.etCategoryFilter?.setText(R.string.category7) }
        if (string == "Askare") { view?.etCategoryFilter?.setText(R.string.category6) }
        if (string == "Talous") { view?.etCategoryFilter?.setText(R.string.category5) }
        if (string == "Tärkeä") { view?.etCategoryFilter?.setText(R.string.category4) }
        if (string == "Työ") { view?.etCategoryFilter?.setText(R.string.category3) }
        if (string == "Koulu") { view?.etCategoryFilter?.setText(R.string.category2) }
        if (string == "Terveys") { view?.etCategoryFilter?.setText(R.string.category1) }
        if (string == "Liikunta") { view?.etCategoryFilter?.setText(R.string.allCategories) }
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
        builder.setMessage("Haluatko varmasti poistaa kaikki tehdyt tehtävät? ")
        builder.create().show()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        //Setting the filter again. Some issues came when changing to a different fragment and then coming back to list fragment
        val filter = view?.etCategoryFilter?.text.toString()
        mTaskViewModel.setFilter(filter)
    }
}