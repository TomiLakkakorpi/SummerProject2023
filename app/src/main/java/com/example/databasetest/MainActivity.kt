package com.example.databasetest

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest.permission.*
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                } else {
                }
            }

        if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "Myönnä oikeus ilmoituksiin jos haluat muistutuksia tehtävien takarajasta", Toast.LENGTH_SHORT).show()
            requestPermissionLauncher.launch(POST_NOTIFICATIONS)
        }

        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (!alarmManager.canScheduleExactAlarms()) {
            Toast.makeText(this, "Myönnä oikeus ajoittaa ilmoituksia jos haluat muistutuksia tehtävien takarajasta", Toast.LENGTH_SHORT).show()
            startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
        }

        setupActionBarWithNavController(findNavController(R.id.fragmentContainerView))
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}