package com.example.diaryprogram.geo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.diaryprogram.api.DiaryApi.fetchUserDiary
import com.example.diaryprogram.notification.NotificationHelper
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent == null || geofencingEvent.hasError()) {
            val errorMessage = geofencingEvent?.errorCode ?: "Unknown error"
            Log.e("GeofenceReceiver", "Error: $errorMessage")
            return
        }

        val geofenceTransition = geofencingEvent.geofenceTransition
        val triggeringGeofences = geofencingEvent.triggeringGeofences

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Log.i("GeofenceReceiver", "Entered geofence area.")

            triggeringGeofences?.forEach { geofence ->
                val geofenceId = geofence.requestId
                val diaryId = geofenceId.removePrefix("Diary_").toLongOrNull()

                if (diaryId != null) {
                    fetchDiaryAndShowNotification(context, userId = 2L, diaryId = diaryId)
                } else {
                    Log.e("GeofenceReceiver", "Invalid geofence ID: $geofenceId")
                }
            }
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Log.i("GeofenceReceiver", "Exited geofence area.")
        } else {
            Log.e("GeofenceReceiver", "Unknown geofence transition type: $geofenceTransition")
        }
    }

    private fun fetchDiaryAndShowNotification(context: Context, userId: Long, diaryId: Long) {
        fetchUserDiary(
            userId = userId,
            diaryId = diaryId,
            onSuccess = { diary ->
                val notificationHelper = NotificationHelper(context)
                notificationHelper.showNotification(
                    title = "일기 알림",
                    message = "근처에서 작성한 일기가 있어요",
                    diaryId = diaryId
                )
            },
            onFailure = { error ->
                Log.e("GeofenceReceiver", "Failed to fetch diary: ${error.message}")
            }
        )
    }
}
