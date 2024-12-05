package com.com.example.diaryprogram.geo

import com.example.diaryprogram.geo.GeofenceBroadcastReceiver

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener

class GeofenceHelper(private val context: Context) {
    private val geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)

    /**
     * 위치 권한 확인 메서드
     */
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

    /**
     * 권한 요청 메서드
     * @param activity 권한 요청을 처리할 Activity
     * @param requestCode 요청 코드
     */
    fun requestPermissions(activity: AppCompatActivity, requestCode: Int) {
        val permissions = mutableListOf(Manifest.permission.ACCESS_FINE_LOCATION)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

        ActivityCompat.requestPermissions(activity, permissions.toTypedArray(), requestCode)
    }

    /**
     * 지오펜싱 추가 메서드
     * @param diaryId 일기 ID
     * @param latitude 위도
     * @param longitude 경도
     * @param radius 반경 (미터)
     * @param expirationDuration 유효 기간 (밀리초)
     * @param pendingIntent 지오펜싱 이벤트를 처리할 PendingIntent
     * @param onCompleteListener 완료 리스너
     */
    fun addGeofence(
        diaryId: Long,
        latitude: Double,
        longitude: Double,
        radius: Float,
        expirationDuration: Long,
        pendingIntent: PendingIntent,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        if (!hasLocationPermission()) {
            throw SecurityException("위치 권한이 필요합니다.")
        }

        val geofenceId = "Diary_$diaryId" // Geofence ID 생성
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

    /**
     * 지오펜싱 제거 메서드
     * @param geofenceId 제거할 지오펜싱 ID
     * @param onCompleteListener 완료 리스너
     */
    fun removeGeofence(geofenceId: String, onCompleteListener: OnCompleteListener<Void>) {
        geofencingClient.removeGeofences(listOf(geofenceId))
            .addOnCompleteListener(onCompleteListener)
    }

    /**
     * Geofence 이벤트를 처리하기 위한 PendingIntent 생성
     * @param context Context
     * @return 생성된 PendingIntent
     */
    fun getGeofencePendingIntent(): PendingIntent {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }
}
