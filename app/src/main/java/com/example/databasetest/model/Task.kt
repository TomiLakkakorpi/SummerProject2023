package com.example.databasetest.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val header: String,
    val time: String,
    val date: String,
    val dayName: String,
    val details: String,
    val category: String,
    var status: Boolean,
    val notifyMinutes: Boolean,
    val notifyHour: Boolean,
    val notifyDay: Boolean,
    val importance: Int
): Parcelable