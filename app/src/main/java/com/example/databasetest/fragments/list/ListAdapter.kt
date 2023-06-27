package com.example.databasetest.fragments.list

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.databasetest.R
import com.example.databasetest.model.Task
import kotlinx.android.synthetic.main.custom_row.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Collections.emptyList

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>(){

    private var taskList = emptyList<Task>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate((R.layout.custom_row), parent, false))
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = taskList[position]
        val dateValues = currentItem.date
        val valuesArrayList = dateValues.split("/")

        val year = valuesArrayList[0]
        val month = valuesArrayList[1]
        val day = valuesArrayList[2]
        val dateValue = "$day/$month/$year"

        holder.itemView.tvTaskHeader.text = currentItem.header
        holder.itemView.tvTaskTime.text = currentItem.time
        holder.itemView.tvTaskDate.text = dateValue
        holder.itemView.tvTaskDay.text = currentItem.dayName
        holder.itemView.tvTaskDetails.text = currentItem.details
        holder.itemView.tvCategory.text = currentItem.category

        holder.itemView.taskMainConstraint.setOnClickListener{
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }

        if (currentItem.status) {
            holder.itemView.taskCheckbox.setChecked(true)
        } else {
            holder.itemView.taskCheckbox.setChecked(false)
        }

        if (currentItem.category == "Liikunta") {
            holder.itemView.taskMainConstraint.setBackgroundResource(R.color.liikunta)
        }
        if (currentItem.category == "Hyvinvointi") {
            holder.itemView.taskMainConstraint.setBackgroundResource(R.color.hyvinvointi)
        }
        if (currentItem.category == "Terveys") {
            holder.itemView.taskMainConstraint.setBackgroundResource(R.color.terveys)
        }
        if (currentItem.category == "Koulu") {
            holder.itemView.taskMainConstraint.setBackgroundResource(R.color.koulu)
        }
        if (currentItem.category == "Työ") {
            holder.itemView.taskMainConstraint.setBackgroundResource(R.color.tyo)
        }
        if (currentItem.category == "Harrastus") {
            holder.itemView.taskMainConstraint.setBackgroundResource(R.color.harrastus)
        }
        if (currentItem.category == "Askare") {
            holder.itemView.taskMainConstraint.setBackgroundResource(R.color.askare)
        }
        if (currentItem.category == "Tapaaminen") {
            holder.itemView.taskMainConstraint.setBackgroundResource(R.color.tapaaminen)
        }
        if (currentItem.category == "Muu") {
            holder.itemView.taskMainConstraint.setBackgroundResource(R.color.muu)
        }
    }

    /*
    @SuppressLint("ResourceType")
    fun changeBarColor(holder: MyViewHolder, @ColorRes color: Int) {
        val id = ContextCompat.getDrawable(
            context, R.id.addFragment
        ) as LayerDrawable

        val gradientDrawable = id.findDrawableByLayerId(R.id.taskCategoryBar) as GradientDrawable

        gradientDrawable.setColor(
            ContextCompat.getColor(
                context,
                color
            )
        )

        holder.itemView.background = id
    } */

    fun setData(task: List<Task>){
        this.taskList = task
        notifyDataSetChanged()
    }
}