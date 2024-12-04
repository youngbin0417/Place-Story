package com.example.diaryprogram.geo

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener

class GeofenceHelper(private val context: Context) {
    private val geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)

    // 권한이 있는지 확인하는 메서드
    fun hasLocationPermission(): Boolean {
        val fineLocationPermission = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val backgroundLocationPermission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }

        return fineLocationPermission && backgroundLocationPermission
    }

    // 권한 요청 메서드 (Activity가 필요함)
    fun requestPermissions(activity: AppCompatActivity, requestCode: Int) {
        val permissions = mutableListOf(Manifest.permission.ACCESS_FINE_LOCATION)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

        requestPermissions(activity, permissions.toTypedArray(), requestCode)
    }

    // 지오펜싱 추가
    fun addGeofence(
        diaryId: Long, // 일기 ID 추가
        latitude: Double,
        longitude: Double,
        radius: Float,
        expirationDuration: Long,
        pendingIntent: PendingIntent,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        val geofenceId = "Diary_$diaryId" // 일기 ID를 포함한 Geofence ID 생성
        // 권한 확인
        if (!hasLocationPermission()) {
            throw SecurityException("위치 권한이 필요합니다.")
        }

        val geofence = Geofence.Builder()
            .setRequestId(geofenceId)
            .setCircularRegion(latitude, longitude, radius)
            .setExpirationDuration(expirationDuration)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        // 권한 확인 및 지오펜싱 추가
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            throw SecurityException("위치 권한이 필요합니다.")
        }

        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
            .addOnCompleteListener(onCompleteListener)
    }

    // 지오펜싱 제거
    fun removeGeofence(geofenceId: String, onCompleteListener: OnCompleteListener<Void>) {
        geofencingClient.removeGeofences(listOf(geofenceId))
            .addOnCompleteListener(onCompleteListener)
    }

    fun getGeofencePendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }
}