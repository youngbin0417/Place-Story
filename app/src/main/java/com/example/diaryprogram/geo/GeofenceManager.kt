package com.example.diaryprogram.geo

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.diaryprogram.utils.CUSTOM_INTENT_GEOFENCE
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

class GeofenceManager(private val context: Context) {

    private val TAG = "GeofenceManager"
    private val geofenceClient = LocationServices.getGeofencingClient(context)
    val geofenceList = mutableMapOf<String, Geofence>()

    // PendingIntent for geofence transitions
    private val geofencingPendingIntent: PendingIntent by lazy {
        PendingIntent.getBroadcast(
            context,
            0,
            Intent(CUSTOM_INTENT_GEOFENCE),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun addGeofence(
        id: String,
        latitude: Double,
        longitude: Double,
        radius: Float = 1000f, // 1km
        expirationDuration: Long = Geofence.NEVER_EXPIRE
    ) {
        geofenceList[id] = Geofence.Builder()
            .setRequestId(id)
            .setCircularRegion(latitude, longitude, radius)
            .setExpirationDuration(expirationDuration)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()
    }

    fun removeGeofence(id: String) {
        geofenceList.remove(id)
    }

    @SuppressLint("MissingPermission")
    fun registerGeofences() {
        if (geofenceList.isEmpty()) {
            Log.w(TAG, "No geofences to register.")
            return
        }
        geofenceClient.addGeofences(createGeofencingRequest(), geofencingPendingIntent)
            .addOnSuccessListener {
                Log.d(TAG, "Geofences registered successfully.")
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to register geofences: ${exception.message}", exception)
            }
    }

    fun clearGeofences() {
        geofenceClient.removeGeofences(geofencingPendingIntent)
            .addOnSuccessListener {
                Log.d(TAG, "All geofences cleared successfully.")
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to clear geofences: ${exception.message}", exception)
            }
        geofenceList.clear()
    }

    private fun createGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(geofenceList.values.toList())
        }.build()
    }
}
