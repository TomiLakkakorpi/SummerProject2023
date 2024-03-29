package com.example.databasetest.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.example.databasetest.MainActivity
import com.example.databasetest.R

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        //Initializing notification manager
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //Getting the details for the notification from the intent
        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return

        //Splitting the message string into 2 values (Notification type and the message itself)
        val splitMessage = message.split(":")
        val notificationType = splitMessage[0]
        val notificationMessage = splitMessage[1]
        val taskIDminutes = splitMessage[2].toInt() + 100000
        val taskIDhour = splitMessage[2].toInt() + 200000
        val taskIDday = splitMessage[2].toInt() + 300000

        val resultIntent = Intent(context, MainActivity::class.java)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        //This notification will be 15mins before due time
        if (notificationType.toInt() == 1) {
            val notification = NotificationCompat.Builder(context, "ToDoChannel")
                .setContentTitle("Muistutus tehtävästä $notificationMessage")
                .setContentText("Tehtävälle merkitty aika on 15 minuutin päästä")
                .setSmallIcon(R.drawable.appicon)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .build()
            notificationManager.notify(taskIDminutes, notification)
        }

        //This notification will be shown one hour before the due time
        if (notificationType.toInt() == 2) {
            val notification = NotificationCompat.Builder(context, "ToDoChannel")
                .setContentTitle("Muistutus tehtävästä $notificationMessage")
                .setContentText("Tehtävälle merkitty aika on tunnin päästä!")
                .setSmallIcon(R.drawable.appicon)
                .setAutoCancel(true)
                .build()
            notificationManager.notify(taskIDhour, notification)
        }

        //This notification will be shown one day before the due time
        if (notificationType.toInt() == 3) {
            val notification = NotificationCompat.Builder(context, "ToDoChannel")
                .setContentTitle("Muistutus tehtävästä $notificationMessage")
                .setContentText("Tehtävälle merkitty aika on päivän päästä!")
                .setSmallIcon(R.drawable.appicon)
                .setAutoCancel(true)
                .build()
            notificationManager.notify(taskIDday, notification)
        }
    }
}