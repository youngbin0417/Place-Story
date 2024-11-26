package com.example.diaryprogram.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.diaryprogram.R
import com.example.diaryprogram.data.MarkerData
import com.example.diaryprogram.viewmodel.LocationViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun SearchLocation(initialPosition: LatLng, viewModel: LocationViewModel, // ViewModel 주입
                   onBack: () -> Unit) {
    val context = LocalContext.current
    var zoom by remember { mutableFloatStateOf(17f) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition, zoom)
    } // 현재 위치를 초기값으로

    var markerPosition by remember { mutableStateOf(initialPosition) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                // 사용자가 지도를 클릭하면 해당 위치로 마커 업데이트
                markerPosition = latLng
                // 클릭한 위치로 카메라 이동
                cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, zoom)
            }
        ) {
            // markerPosition이 null이 아닐 때만 Marker를 추가합니다.
            markerPosition?.let { position ->
                Marker(
                    state = MarkerState(position = position),
                    title = "Selected Location",
                    snippet = "Lat: ${position.latitude}, Lng: ${position.longitude}",
                )
            }
        }

        IconButton(
            onClick = {
                markerPosition.let { position ->
                    viewModel.setSelectedLocation(markerPosition) // ViewModel에 위치 저장
                    onBack()                 }
            },
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
                .align(Alignment.TopEnd)
                .padding(top = 20.dp)
                .border(16.dp, color = androidx.compose.ui.graphics.Color.Transparent)
        ) {
            Image(
                painter = painterResource(id = R.drawable.blckcheck),
                contentDescription = "Back Button",
                modifier = Modifier.width(40.dp).height(40.dp)
            )
        }
    }
}