package com.example.databasetest.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.databasetest.R
import com.example.databasetest.model.Task
import com.example.databasetest.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()

    private lateinit var mTaskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        view.etEditScreenHeader.setText(args.currentTask.header)
        view.etEditScreenTime.setText(args.currentTask.time)
        view.etEditScreenDate.setText(args.currentTask.date)
        view.etEditScreenDetails.setText(args.currentTask.details)

        view.buEditScreenSave.setOnClickListener {
            updateItem()
        }

        view.buEditScreenCancel.setOnClickListener {
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }

        setHasOptionsMenu(true)

        return view
    }

    private fun updateItem(){
        val header = etEditScreenHeader.text.toString()
        val time = etEditScreenTime.text.toString()
        val date = etEditScreenDate.text.toString()
        val details = etEditScreenDetails.text.toString()
        val category = autoCompleteTextView.text.toString()

        if (inputCheck(header, time, date, details)){
            val updatedTask = Task(args.currentTask.id, header, time, date, details, category)

            mTaskViewModel.updateTask(updatedTask)

            Toast.makeText(requireContext(), "Pävitetty onnistuneesti", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "Täytä kaikki kentät", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(header: String, time: String, date: String, details: String): Boolean{
        return !(TextUtils.isEmpty(header) && TextUtils.isEmpty(time) && TextUtils.isEmpty(date) && TextUtils.isEmpty(details))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    // Turha? poistetaan ehkä
    //fun onOptionsItemsSelected(item: MenuItem): Boolean {
    //    if(item.itemId == R.id.delete_menu) {
    //        deleteTask()
    //    }
    //    return onOptionsItemSelected(item)
    //}

    // Turha? Poistetaan ehkä
    //private fun deleteTask(){
    //    val builder = AlertDialog.Builder(requireContext())
    //    builder.setPositiveButton("Kyllä") {_, _ ->
    //        mTaskViewModel.deleteTask(args.currentTask)
    //        Toast.makeText(requireContext(), "Tehtävät poistettiin onnistuneesti: ${args.currentTask.header}", Toast.LENGTH_LONG).show()
    //            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
    //    }
    //    builder.setNegativeButton("Ei") {_, _ -> }
    //    builder.setTitle("Poista ${args.currentTask.header}?")
    //   builder.setMessage("Haluatko varmasti poistaa ${args.currentTask.header}?")
    //    builder.create().show()
    //}
}