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
    private val binding get() = _binding!!

    private lateinit var mTaskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        view.buAddScreenAdd.setOnClickListener {
            insertDataToDatabase()
        }

        view.buAddScreenCancel.setOnClickListener {
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
            //Toast.makeText(requireContext(), "Tehtävää ei lisätty", Toast.LENGTH_LONG).show()
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
        val date = etAddScreenDate.text.toString()
        val details = etAddScreenDetails.text.toString()
        val category = autoCompleteTextView.text.toString()

        if (inputCheck(header,time, date, details, category)) {

            val task = Task(0, header, time, date, details, category)

            mTaskViewModel.addTask(task)
            Toast.makeText(requireContext(), "Tehtävä lisätty", Toast.LENGTH_LONG).show()

            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "Täytä kaikki kentät", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(header: String, time: String, date: String, details: String, category: String): Boolean{
        return !(TextUtils.isEmpty(header) && TextUtils.isEmpty(time) && TextUtils.isEmpty(date) && TextUtils.isEmpty(details) && TextUtils.isEmpty(category))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}