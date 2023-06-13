package com.todoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.confirmation)

        //Palataan takaisin päänäkymään
        //val returnToMainActivityCancel = findViewById<Button>(R.id.buConfirmationScreenCancel)
        //returnToMainActivityCancel.setOnClickListener {
        //    val Intent = Intent(this,MainActivity::class.java)
        //    startActivity(Intent)
        //}

        //Palataan takaisin päänäkymään
        //val returnToMainActivityDelete = findViewById<Button>(R.id.buConfirmationScreenDelete)
        //returnToMainActivityDelete.setOnClickListener {
        //    val Intent = Intent(this,MainActivity::class.java)
        //    startActivity(Intent)

            //Tähän myöhemmin valmiiden tehtävien poisto
        //}
    }
}