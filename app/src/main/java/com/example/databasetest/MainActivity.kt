package com.example.databasetest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.databasetest.fragments.list.ListFragment
import kotlinx.android.synthetic.main.fragment_list.*

const val CHANNEL_ID = "ToDoChannel"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBarWithNavController(findNavController(R.id.fragmentContainerView))

        val testID = "1"
        val testTitle = "Title"
        val testText = "Text"

        createNotificationChannel()

        showNotification(testID)

    }

    //private val intent1 = Intent(this, MainActivity::class.java).apply {
    //    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    //}
    //val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_IMMUTABLE)

    private val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_add)
        .setContentTitle("test")
        .setContentText("test")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    //.setContentIntent(pendingIntent)
    //.setAutoCancel(true)

    private fun showNotification(id: String) {
        with(NotificationManagerCompat.from(this)) {
            notify(id.toInt(), builder.build())
        }
    }

    private fun createNotificationChannel() {
        val name = "To Do Channel"
        val descriptionText = "Channel for To Do´s"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}