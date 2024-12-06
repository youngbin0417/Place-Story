package com.example.diaryprogram.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.diaryprogram.MainActivity
import com.example.diaryprogram.R

class NotificationHelper(private val context: Context) {
    fun showNotification(title: String, message: String, diaryId: Long) {
        Log.d("NotificationHelper", "Showing notification: $title - $message for diaryId: $diaryId")
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "diary_channel",
                "Diary Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("navigateTo", "geodiary/$diaryId")
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "diary_channel")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.diaryicon)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(diaryId.toInt(), notification)
    }
}
