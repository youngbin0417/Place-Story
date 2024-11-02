package com.example.diaryprogram.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.diaryprogram.data.MarkerData
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

/**
 * Screen은 page를 구성하는 부분중에 여러번 쓰이는 것으로, 재사용하기 위해 따로 뺐음
 */

// 지도 띄우는 함수
@Composable
fun MapScreen(initialPosition: LatLng) { // screen 사용시에 Latlng라는 위도, 경도 값으로 위치 넘김
    var zoom by remember { mutableFloatStateOf(15f)}
    var markers by remember { mutableStateOf(listOf<MarkerData>()) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition, zoom)
    } // 현재 위치를 초기값으로


    Box{
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ){

        }

    }
}