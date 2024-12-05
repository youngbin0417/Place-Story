package com.example.diaryprogram.geo

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.diaryprogram.MainActivity
import com.example.diaryprogram.MyForegroundService
import com.example.diaryprogram.R
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent


class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return

        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            Log.e("GeofenceBroadcastReceiver", "Error: $errorMessage")
            return
        }

        val transitionType = geofencingEvent.geofenceTransition
        val triggeredGeofences = geofencingEvent.triggeringGeofences

        if (triggeredGeofences != null) {
            for (geofence in triggeredGeofences) {
                if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) {
                    val diaryId = geofence.requestId.toLongOrNull()
                    if (diaryId != null) {
                        val message = "1km 이내에 저장된 일기가 있습니다. 눌러서 확인하세요!"
                        sendGeofenceNotification(context, diaryId, message)
                    } else {
                        Log.e("GeofenceEvent", "Invalid geofence ID: ${geofence.requestId}")
                    }
                }
            }
        }
    }

    private fun sendGeofenceNotification(context: Context, diaryId: Long, message: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("diaryId", diaryId.toString())
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            diaryId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, MyForegroundService.CHANNEL_ID)
            .setContentTitle("일기 알림")
            .setContentText(message)
            .setSmallIcon(R.drawable.diaryicon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(diaryId.toInt(), notification)
    }
}

