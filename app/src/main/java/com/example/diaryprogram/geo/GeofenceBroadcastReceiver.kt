package com.example.diaryprogram.geo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.diaryprogram.api.DiaryApi.fetchUserDiary
import com.example.diaryprogram.notification.NotificationHelper
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent != null) {
            if (geofencingEvent.hasError()) {
                val errorMessage = geofencingEvent.errorCode
                Log.e("GeofenceReceiver", "Error: $errorMessage")
                return
            }
        }

        val geofenceTransition = geofencingEvent?.geofenceTransition

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Log.i("GeofenceReceiver", "Entered geofence area.")
            // 알림을 띄우는 등의 작업 수행
            // Geofence ID 추출
            val triggeringGeofences = geofencingEvent.triggeringGeofences
            if (!triggeringGeofences.isNullOrEmpty()) {
                val geofenceId = triggeringGeofences[0].requestId // 첫 번째 Geofence ID 사용
                val diaryId = geofenceId.removePrefix("Diary_").toLongOrNull()

                if (diaryId != null) {
                    // 서버 API 호출
                    fetchDiaryAndShowNotification(context, userId = 2L, diaryId = diaryId)
                } else {
                    Log.e("GeofenceReceiver", "Invalid diary ID: $geofenceId")
                }
            }
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Log.i("GeofenceReceiver", "Exited geofence area.")
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
                    diaryId = diaryId // 추가
                )
            },
            onFailure = { error ->
                Log.e("GeofenceReceiver", "Failed to fetch diary: ${error.message}")
            }
        )
    }

}

