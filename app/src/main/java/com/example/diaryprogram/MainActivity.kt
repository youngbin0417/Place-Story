package com.example.diaryprogram

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
    fun PermissionRequestAndServiceStart() {
        val context = LocalContext.current
        val navController = rememberNavController()

        // 알림 Intent로 전달된 경로 처리
        LaunchedEffect(navController) {
            val routeFromNotification = (context as? MainActivity)?.intent?.getStringExtra("navigateTo")
            if (!routeFromNotification.isNullOrEmpty() && navController.currentDestination?.route != routeFromNotification) {
                navController.navigate(routeFromNotification) {
                    popUpTo("login") { inclusive = true } // 백스택 정리
                }
            }
        }

        // 알림 권한 요청
        val notificationPermissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                Log.d("PermissionCheck", "Notification permission granted.")
                startMyForegroundServiceWithDelay()
            } else {
                Toast.makeText(context, "알림 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 위치 및 Foreground Service 권한 요청
        val locationPermissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val allGranted = permissions.values.all { it }
            if (allGranted) {
                Log.d(
                    "PermissionCheck",
                    "All permissions granted. Requesting notification permission."
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    startMyForegroundServiceWithDelay()
                }
            } else {
                Toast.makeText(context, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                Log.d("PermissionCheck", "One or more permissions were denied.")
            }
        }

        // 권한 요청 및 Foreground Service 실행
        LaunchedEffect(Unit) {
            if (checkPermissions()) {
                Log.d(
                    "PermissionCheck",
                    "Permissions already granted. Starting foreground service."
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    startMyForegroundServiceWithDelay()
                }
            } else {
                Log.d("PermissionCheck", "Requesting permissions.")
                val permissionsToRequest = mutableListOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    permissionsToRequest.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                }
                locationPermissionLauncher.launch(permissionsToRequest.toTypedArray())
            }
        }

        // 내비게이션 그래프 시작
        NavGraph(navController = navController)
    }

    private fun startMyForegroundServiceWithDelay() {
        Log.d("ServiceStart", "Preparing to start foreground service with delay.")

        if (!isServiceRunning(MyForegroundService::class.java)) {
            window.decorView.postDelayed({
                startMyForegroundService()
            }, 1000) // 1초 지연
        } else {
            Log.d("ServiceStart", "Service is already running. Skipping start.")
        }
    }

    private fun startMyForegroundService() {
        val serviceIntent = Intent(this, MyForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }



    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return manager.getRunningServices(Int.MAX_VALUE).any {
            it.service.className == serviceClass.name
        }
    }

    // 권한 확인
    private fun checkPermissions(): Boolean {
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val notificationGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true

        val backgroundLocationGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else true

        return fineLocationGranted && coarseLocationGranted && notificationGranted && backgroundLocationGranted
    }


}
