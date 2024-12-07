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
        Log.d(
            "NotificationHelper",
            "Showing notification: title=$title, message=$message, diaryId=$diaryId"
        )

        // 알림 매니저 가져오기
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 알림 채널 생성 (Android O 이상)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "diary_channel",
                "Diary Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for Diary Program"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 클릭 시 이동할 Intent 설정
        val notificationIntent = Intent(context, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            putExtra("diaryId", diaryId)
        }

        // PendingIntent 생성
        val pendingIntent = PendingIntent.getActivity(
            context,
            diaryId.toInt(), // 고유한 RequestCode
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 알림 생성
        val notification = NotificationCompat.Builder(context, "diary_channel")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.diaryicon) // 알림 아이콘
            .setContentIntent(pendingIntent) // 알림 클릭 시 실행
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // 알림 클릭 시 자동 삭제
            .build()

        // 알림 표시
        notificationManager.notify(diaryId.toInt(), notification)
    }
}

