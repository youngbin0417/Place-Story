package com.example.diaryprogram.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import coil.compose.rememberAsyncImagePainter
import com.example.diaryprogram.R
import com.example.diaryprogram.api.ApiClient.apiService
import com.example.diaryprogram.api.UserApi.loadUserProfile
import com.example.diaryprogram.data.UserProfileResponseDto
import com.example.diaryprogram.util.utils

// 프로필 편집 화면으로 해야함
@Composable
fun SettingPage(navHostController: NavHostController,userId:Long) {
    var userProfile by remember { mutableStateOf<UserProfileResponseDto?>(null) }

    // LaunchedEffect를 사용하여 한 번만 API 호출 실행
    LaunchedEffect(userId) {
        loadUserProfile(apiService, userId) { profile ->
            userProfile = profile
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            Brush.linearGradient(
                colors = listOf(Color(0xFF070301), Color(0xFF886B5F)),
                start = Offset(0f, 0f),
                end = Offset(0f, 3000f)
            ) // 그라데이션 세팅
        )
    ){
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { navHostController.popBackStack() },
                    modifier = Modifier.size(50.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.whiteback),
                        contentDescription = "백버튼",
                        modifier = Modifier.size(50.dp)
                    )
                }
                Text(
                    text = "프로필 편집",
                    color = Color.White,
                    fontSize = 25.sp,
                    fontFamily = FontFamily(Font(R.font.nanumbarunpenb))
                )
                IconButton(
                    onClick = { /*프로필 업데이트 함수*/
                        navHostController.navigate("profile")
                    },
                    modifier = Modifier.size(50.dp)
                ){
                    Image(
                        painter = painterResource(id = R.drawable.check),
                        contentDescription = "등록 버튼"
                    )
                }
            }
        }

        Column (modifier = Modifier.padding(30.dp)
            .align(Alignment.CenterStart)
        ) {


            Box(modifier = Modifier
                .width(361.dp)
                .height(443.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(color = colorResource(id = R.color.light_daisy))
                .align(Alignment.CenterHorizontally)
            ) {
                // 이미지 연동 후, 수정
                Column(modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row (modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center){
                        Spacer(modifier = Modifier.width(20.dp))
                        userProfile?.profileImage?.let { image ->
                            if (image.url == "default.jpg"){
                                Image(
                                    painter = painterResource(id = R.drawable.profile),
                                    contentDescription = "Default Profile Icon",
                                    modifier = Modifier.size(100.dp)
                                        .clip(CircleShape)
                                )
                            }
                            else {
                                utils.DisplayCircleImage(
                                    base64String = image.url,
                                    size = 100.dp
                                )
                            }
                        } ?: Image(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "Default Profile Icon",
                            modifier = Modifier.size(100.dp)
                                .clip(CircleShape)
                        )

                        Box(
                            modifier = Modifier
                                .size(30.dp) // 경계를 포함한 전체 크기
                                .offset(x = (-20).dp, y = (-2).dp) // 위치 조정
                                .clip(CircleShape) // 경계를 둥글게 클리핑
                                .clickable {
                                    // 사진 업로드 함수
                                }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.edit), // 알림 아이콘 리소스
                                contentDescription = "Notification Button",
                                modifier = Modifier
                                    .fillMaxSize() // 아이콘이 Box 내부 크기에 맞게 채워짐
                                    .padding(3.dp), // 경계와 아이콘 사이 간격
                                tint = Color.White // 아이콘 색상
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(60.dp))


                    userProfile?.let {
                        Text(
                            text = it.name,
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.nanumbarunpenb)),
                            fontSize = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    HorizontalDivider(
                        color = Color.White,
                        thickness = 1.dp
                    )

                }
            }
        }
    }
}