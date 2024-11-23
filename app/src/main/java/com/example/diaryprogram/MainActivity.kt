package com.example.diaryprogram

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.navigation.compose.rememberNavController
import com.example.diaryprogram.navi.NavGraph
import com.example.diaryprogram.ui.theme.DiaryProgramTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DiaryProgramTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    PermissionRequestAndServiceStart()
                }
            }
        }
    }

    @Composable
    fun PermissionRequestAndServiceStart(
    ) {
        val context = LocalContext.current
        val navController = rememberNavController()

        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.forEach { (permission, granted) ->
                Log.d("PermissionCheck", "$permission granted: $granted")
            }

            val allGranted = permissions.values.all { it }
            if (allGranted) {
                Toast.makeText(context, "All permissions are granted", Toast.LENGTH_SHORT).show()
                Log.d("PermissionCheck", "All permissions granted. Starting foreground service.")
                startMyForegroundServiceWithDelay()
            } else {
                Toast.makeText(context, "Location permissions are required for this service", Toast.LENGTH_SHORT).show()
                Log.d("PermissionCheck", "One or more permissions were denied.")
            }
        }

        LaunchedEffect(Unit) {
            if (checkPermissions()) {
                Log.d("PermissionCheck", "Permissions already granted. Starting foreground service.")
                startMyForegroundServiceWithDelay()
            } else {
                Log.d("PermissionCheck", "Requesting permissions.")
                val permissionsToRequest = mutableListOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.FOREGROUND_SERVICE
                )
                permissionsToRequest.add(Manifest.permission.FOREGROUND_SERVICE_LOCATION)
                permissionLauncher.launch(permissionsToRequest.toTypedArray())
            }
        }
        NavGraph(navController = navController)
    }

    // 백그라운드
    private fun startMyForegroundServiceWithDelay() {
        Log.d("ServiceStart", "Preparing to start foreground service with delay.")
        window.decorView.postDelayed({
            startMyForegroundService()
        }, 1000)  // 1초 지연
    }

    private fun startMyForegroundService() {
        val serviceIntent = Intent(this, MyForegroundService::class.java)
        Log.d("ServiceStart", "Starting foreground service.")
        startForegroundService(serviceIntent)
    }

    // 권한 체크용이라 신경쓸 것 없음
    private fun checkPermissions(): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PermissionChecker.PERMISSION_GRANTED

        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PermissionChecker.PERMISSION_GRANTED

        val foregroundServicePermission =
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.FOREGROUND_SERVICE
            ) == PermissionChecker.PERMISSION_GRANTED

        val allPermissionsGranted = fineLocationPermission && coarseLocationPermission && foregroundServicePermission
        Log.d("PermissionCheck", "Foreground service permission: $foregroundServicePermission")
        Log.d("PermissionCheck", "Location permissions (fine, coarse): ($fineLocationPermission, $coarseLocationPermission)")
        return allPermissionsGranted
    }
}
