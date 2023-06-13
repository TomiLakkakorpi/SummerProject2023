package com.todoapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class item_todo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_todo)

        //Muuttujien jne alustaminen
        val OpenDetails: Button = findViewById(R.id.buTaskExpand)
        val HideDetails:Button = findViewById(R.id.buTaskHide)
        var Details:TextView = findViewById(R.id.tvTaskDetails)

        //Piilotetaan lisätiedot sekä "piilota lisätiedot" näppäin aluksi
        HideDetails.alpha = 0.0f
        Details.alpha = 0.0f

        //Kun "laajenna lisätiedot" näppäin painetaan, lisätiedot teksti ja "piilota lisätiedot" näppäin avataan näkyviin käyttäjälle.
        //"laajenna lisätiedot" piilotetaan
        OpenDetails.setOnClickListener {
            OpenDetails.alpha = 0.0f
            HideDetails.alpha = 1.0f
            Details.alpha = 1.0f
        }

        //Kun "piilota lisätiedot" näppäin painetaan, lisätiedot teksti ja "piilota lisätiedot" näppäin piilotetaan käyttäjältä.
        // "laajenna lisätiedot" näppäin palautetaan näkyviin
        HideDetails.setOnClickListener {
            HideDetails.alpha = 0.0f
            OpenDetails.alpha = 1.0f
            Details.alpha = 0.0f
        }
    }
}