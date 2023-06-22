package com.example.databasetest.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databasetest.R
import com.example.databasetest.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_list.view.*

class ListFragment : Fragment() {

    private lateinit var mTaskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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

        setHasOptionsMenu(true)

        return view
    }

    // Turha? Poistetaan ehkä
    //override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    //    inflater.inflate(R.menu.delete_menu, menu)
    //}

    private fun deleteAllTasks() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Kyllä") {_, _ ->
            mTaskViewModel.deleteAllTasks()
            Toast.makeText(requireContext(), "Kaikki tehtävät poistettiin onnistuneesti", Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("Ei") {_, _ -> }
        builder.setTitle("Poista kaikki tehtävät?")
        builder.setMessage("Haluatko varmasti poistaa kaikki tehtävät?")
        builder.create().show()
    }
}