package com.todoapp

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class item_todo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_todo)

        //Muuttujien jne alustaminen
        val OpenDetails: Button = findViewById(R.id.buTaskExpand)
        val HideDetails:Button = findViewById(R.id.buTaskHide)
        var Details:TextView = findViewById(R.id.tvTaskDetails)
        val CategoryBarShort:ConstraintLayout = findViewById(R.id.taskCategoryBarShort)
        val CategoryBarLong:ConstraintLayout = findViewById(R.id.taskCategoryBarLong)

        //Piilotetaan lisätiedot sekä "piilota lisätiedot" näppäin kun sovellus avataan
        HideDetails.setVisibility(View.INVISIBLE)
        Details.setVisibility(View.INVISIBLE)
        CategoryBarLong.setVisibility(View.INVISIBLE)

        //Kun "laajenna lisätiedot" näppäin painetaan, lisätiedot teksti ja "piilota lisätiedot" näppäin avataan näkyviin käyttäjälle.
        //"laajenna lisätiedot" näppäin piilotetaan
        OpenDetails.setOnClickListener {
            OpenDetails.setVisibility(View.INVISIBLE)
            HideDetails.setVisibility(View.VISIBLE)
            Details.setVisibility(View.VISIBLE)
            CategoryBarLong.setVisibility(View.VISIBLE)
            CategoryBarShort.setVisibility(View.INVISIBLE)

            //Hae ja lisää tallennettu lisätietojen teksti textview komponenttiin vasta tässä
        }

        //Kun "piilota lisätiedot" näppäin painetaan, lisätiedot teksti ja "piilota lisätiedot" näppäin piilotetaan käyttäjältä.
        // "laajenna lisätiedot" näppäin palautetaan näkyviin
        HideDetails.setOnClickListener {
            HideDetails.setVisibility(View.INVISIBLE)
            OpenDetails.setVisibility(View.VISIBLE)
            Details.setVisibility(View.INVISIBLE)
            CategoryBarLong.setVisibility(View.INVISIBLE)
            CategoryBarShort.setVisibility(View.VISIBLE)
        }
    }
}