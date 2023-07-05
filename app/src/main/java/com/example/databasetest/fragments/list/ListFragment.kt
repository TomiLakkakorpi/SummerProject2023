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
import com.example.databasetest.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.custom_row.*
import kotlinx.android.synthetic.main.custom_row.view.*
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import java.time.LocalDate

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
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


        view.floatingActionButtonAdd.setOnClickListener{
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        view.floatingActionButtonDelete.setOnClickListener {
            deleteAllTasks()
        }

        _binding = FragmentListBinding.inflate(inflater, container, false)
        val categories = resources.getStringArray(R.array.categories2)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categories)
        view.findViewById<AutoCompleteTextView>(R.id.CategoryFilter).setAdapter(arrayAdapter)

        return view
    }

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