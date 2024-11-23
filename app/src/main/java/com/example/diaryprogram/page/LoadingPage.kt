package com.example.diaryprogram.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diaryprogram.R

@Composable
fun LoadingPage() {
    Box(modifier = Modifier
        .size(200.dp)
        .background(
            Brush.linearGradient(
                colors = listOf(Color(0xFF070301), Color(0xFF886B5F)), // 시작과 끝 색상
                start = Offset(0f, 0f),   // 시작 지점
                end = Offset(0f, 3000f) // 끝 지점
            )
        )
    ){
        Column(
            modifier = Modifier
                .fillMaxSize(), // 화면 전체 크기 사용
            verticalArrangement = Arrangement.Center, // 세로 방향 중앙 정렬
            horizontalAlignment = Alignment.CenterHorizontally // 가로 방향 중앙 정렬
        ) {
            Image(
                painter = painterResource(id = R.drawable.loading),
                contentDescription = "로딩 화면",
                modifier = Modifier.size(200.dp)
            )
            Text(
                text = "PLACE STORY",
                color = Color.White,
                fontSize = 25.sp,
                fontFamily = FontFamily(Font(R.font.nanumbarunpenb))
            )
        }

    }
}