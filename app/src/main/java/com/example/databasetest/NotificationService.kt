package com.example.databasetest

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class NotificationService(

) {
    companion object {
        private const val CHANNEL_ID = "To Do Reminders"

        private val context: Context = TODO()
        private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        fun showNotification(title: String, text: String) {
            val activityIntent = Intent(context, MainActivity::class.java)
            val activityPendingIntent = PendingIntent.getActivity(
                context,
                1,
                activityIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(activityPendingIntent)
                .build()

            notificationManager.notify(
                1, notification
            )
        }
    }
}