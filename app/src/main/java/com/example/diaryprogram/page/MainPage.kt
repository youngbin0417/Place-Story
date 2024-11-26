package com.example.diaryprogram.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.diaryprogram.R
import com.example.diaryprogram.appbar.AppBar
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
//프론트 완료
@Composable
fun MainPage(navHostController: NavHostController, currentLocation:LatLng) {
    Box(modifier = Modifier
        .size(200.dp)
        .background(
            Brush.linearGradient(
                colors = listOf(Color(0xFF070301), Color(0xFF886B5F)), // 시작과 끝 색상
                start = Offset(0f, 0f),   // 시작 지점
                end = Offset(0f, 3000f) // 끝 지점
            ) // 그라데이션 세팅
        )
    ) {
        Column(modifier = Modifier.padding(30.dp)) {
            Spacer(modifier = Modifier.height(40.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "PLACE STORY",
                    color = Color.White,
                    fontSize = 25.sp,
                    fontFamily = FontFamily(Font(R.font.nanumbarunpenb))
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(text = "나의 장소들", color = Color.White, fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.nanumbarunpenr)))
            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .width(500.dp)
                    .height(550.dp)
                    .clip(RoundedCornerShape(48.dp))
            ) {
                // GoogleMap을 Box 내부에 배치
                GoogleMap(
                    modifier = Modifier.matchParentSize(), // Box 크기와 동일하게 설정
                    cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(currentLocation, 19f) // 초기 카메라 위치
                    }
                )
                // 선택 사항: 지도 상호작용 방지를 위한 투명 레이어
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable {
                            // 지도 클릭 시 이동
                            navHostController.navigate("map")
                        }
                        .background(Color.Transparent) // 투명한 레이어로 상호작용 방지
                )
            }
        }
        AppBar(modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 30.dp),
            navHostController = navHostController,
            option=0
        )
    }
}



