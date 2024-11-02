package com.example.diaryprogram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import com.example.diaryprogram.screen.MapScreen
import com.example.diaryprogram.ui.theme.DiaryProgramTheme
import com.google.android.gms.maps.model.LatLng


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiaryProgramTheme {
                // Scaffold 내부에서 GeoFencingScreen을 호출하고 contentPadding을 전달
                Surface(
                    modifier =Modifier.fillMaxSize(),
                    color = Color.White
                ){
                    //val navController =rememberNavController()
                    //NavGraph(navController=navController)
                    val location = LatLng(37.7749, -122.4194) // 일단 예시로 캘리포니아 잡음
                    MapScreen(location)
                }
            }
        }
    }
}




