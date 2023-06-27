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
import android.view.Menu
import kotlinx.android.synthetic.main.custom_row.view.*
import kotlinx.android.synthetic.main.fragment_add.*

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
        val dateValue = ("$day/$month/$year")

        view.etEditScreenDate.setText(dateValue)
        view.etEditScreenHeader.setText(args.currentTask.header)
        view.etEditScreenTime.setText(args.currentTask.time)
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
            Toast.makeText(requireContext(),"Tehtävää ${args.currentTask.header} ei päivitetty", Toast.LENGTH_SHORT).show()
        }

        _binding = FragmentListBinding.inflate(inflater, container, false)
        val categories = resources.getStringArray(R.array.categories)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categories)
        view.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2).setAdapter(arrayAdapter)

        return view
    }

    private fun updateItem(){
        val dateValues = etEditScreenDate.text.toString()
        val valuesArrayList = dateValues.split("/")

        val day = valuesArrayList[0]
        val month = valuesArrayList[1]
        val year = valuesArrayList[2]

        val header = etEditScreenHeader.text.trim().toString()
        val time = etEditScreenTime.text.trim().toString()
        val date = year + "/" + month + "/" + day
        val dayName = etEditScreenDay.text.trim().toString()
        val details = etEditScreenDetails.text.trim().toString()
        val category = autoCompleteTextView2.text.trim().toString()

        if (inputCheck(header, time, date, dayName, category)){
            if (checkBox.isChecked) {
                val updatedTask = Task(args.currentTask.id, header, time, date, dayName, details, category, status = true)
                mTaskViewModel.updateTask(updatedTask)
            } else {
                val updatedTask = Task(args.currentTask.id, header, time, date, dayName, details, category, status = false)
                mTaskViewModel.updateTask(updatedTask)
            }
            Toast.makeText(requireContext(), "Tehtävä ${args.currentTask.header} päivitettiin", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "Täytä kaikki kentät", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(header: String, time: String, date: String, dayName: String, category: String): Boolean{
        return !(TextUtils.isEmpty(header) && TextUtils.isEmpty(time) && TextUtils.isEmpty(date) && TextUtils.isEmpty(dayName) && TextUtils.isEmpty((category)))
    }

    private fun deleteTask(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Kyllä")
        {_, _ ->
            mTaskViewModel.deleteTask(args.currentTask)
            Toast.makeText(requireContext(), "Tehtävä ${args.currentTask.header} poistettiin", Toast.LENGTH_SHORT).show()
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
}