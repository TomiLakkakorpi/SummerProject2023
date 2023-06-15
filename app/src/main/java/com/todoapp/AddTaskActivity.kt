package com.todoapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text
import java.io.File
import java.io.FileWriter
import java.lang.Exception

class AddTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_task)

        // Palaa takaisin kotinäkymään
        val openMainActivity = findViewById<Button>(R.id.buAddScreenCancel)
        openMainActivity.setOnClickListener {
            val Intent = Intent(this,MainActivity::class.java)
            startActivity(Intent)
        }

        val categories = listOf("")
        val autoComplete : AutoCompleteTextView = findViewById(R.id.addScreenDropDownMenu)

        val adapter = ArrayAdapter(this, R.layout.list_category, categories)
        autoComplete.setAdapter(adapter)
        autoComplete.onItemClickListener = AdapterView.OnItemClickListener {
            adapterView, view, i, l ->

            val itemSelected = adapterView.getItemAtPosition(i)
            Toast.makeText(this, "Item: $itemSelected", Toast.LENGTH_SHORT)
        }

    }
}