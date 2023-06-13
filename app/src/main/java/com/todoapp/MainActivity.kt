package com.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Avaa "lisää tehtävä" näkymän
        val openAddTaskActivity = findViewById<Button>(R.id.buHomeScreenAdd)
        openAddTaskActivity.setOnClickListener{
            val Intent = Intent(this,AddTaskActivity::class.java)
            startActivity(Intent)
        }

        // Avaa varmistusnäkymän tehtävien poistamiselle
        val openConfirmationActivity = findViewById<Button>(R.id.buHomeScreenDelete)
        openConfirmationActivity.setOnClickListener{
            val Intent = Intent(this,ConfirmationActivity::class.java)
            startActivity(Intent)
        }
    }
}