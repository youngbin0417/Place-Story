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
import androidx.navigation.NavHostController
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

        // 알림 Intent로 전달된 diaryId 처리
        LaunchedEffect(navController) {
            val diaryId = intent?.getLongExtra("diaryId", -1L)
            if (diaryId != null && diaryId != -1L) {
                Log.d("Navigation", "Navigating to geodiary/$diaryId from notification")
                navController.navigate("geodiary/$diaryId") {
                    popUpTo("main") { inclusive = false } // 'main'까지 백스택 유지
                }
            }
        }

        // 권한 요청 및 서비스 실행 로직
        PermissionAndServiceLogic(navController)
    }

    @Composable
    fun PermissionAndServiceLogic(navController: NavHostController) {
        val context = LocalContext.current

        // 권한 확인 및 Foreground Service 실행 로직 (생략하지 않고 기존 로직 유지)
        // 알림 권한 및 위치 권한 요청 처리
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

        val locationPermissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val allGranted = permissions.values.all { it }
            if (allGranted) {
                Log.d("PermissionCheck", "All permissions granted. Requesting notification permission.")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    startMyForegroundServiceWithDelay()
                }
            } else {
                Toast.makeText(context, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        LaunchedEffect(Unit) {
            if (checkPermissions()) {
                Log.d("PermissionCheck", "Permissions already granted. Starting foreground service.")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    startMyForegroundServiceWithDelay()
                }
            } else {
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

        NavGraph(navController = navController)
    }

    private fun startMyForegroundServiceWithDelay() {
        if (!isServiceRunning(MyForegroundService::class.java)) {
            window.decorView.postDelayed({
                startMyForegroundService()
            }, 1000) // 1초 지연
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
