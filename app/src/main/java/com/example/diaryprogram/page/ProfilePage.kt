package com.example.diaryprogram.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.diaryprogram.R
import com.example.diaryprogram.appbar.AppBar

@Composable
fun ProfilePage(navHostController: NavHostController) {
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
        Column (modifier = Modifier.padding(30.dp)) {
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

            Box(modifier = Modifier
                .width(361.dp)
                .height(443.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(color = colorResource(id = R.color.light_daisy))
            ) {
                Column(modifier = Modifier.padding(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row (modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center){
                        Image(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "프로필 사진",
                            modifier = Modifier
                                .size(100.dp) // 이미지 크기
                                .clip(CircleShape) // 동그라미 모양으로 자르기
                                .border(2.dp, Color.Transparent, CircleShape) // 테두리 추가 (선택)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "사용자의 이름",
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.nanumbarunpenr))
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Divider(
                        color = Color.White, // 선 색상
                        thickness = 1.dp // 선 두께
                    )
                }
            }
        }

        AppBar(modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 40.dp),
            navHostController = navHostController,
            option=5
        )
    }
}