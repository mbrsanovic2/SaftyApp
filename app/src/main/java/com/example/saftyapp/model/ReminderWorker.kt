package com.example.saftyapp.model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.saftyapp.R
import com.example.saftyapp.SplashActivity

class ReminderWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    /**
     * Every Day Worker calls this method once
     * It Checks the last access time via preferences and if its more than 3 days sends the notification
     */
    override fun doWork(): Result {
        val prefs = applicationContext.getSharedPreferences("safty_prefs", Context.MODE_PRIVATE)
        val lastOpenTime = prefs.getLong("last_open_time", 0)
        val threeDaysMillis = 3 * 24 * 60 * 60 * 1000L

        // Checks if last open time was more than 3 days
        if (System.currentTimeMillis() - lastOpenTime >= threeDaysMillis) {
            sendNotification()
        }


        return Result.success()
    }

    // Sends the notification
    private fun sendNotification() {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "safty_reminder"

        // Creates a channel with a custom ID and name so that only one worker can exist
        val channel = NotificationChannel(
            channelId,
            "Safty Reminders",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        // Opens app (SplashActivity) when notification is clicked
        val intent = Intent(applicationContext, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Builds the notification
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Come back to Safty!")
            .setContentText("We miss you. Tap to reopen the app.")
            .setSmallIcon(R.drawable.ic_notification_safty)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}