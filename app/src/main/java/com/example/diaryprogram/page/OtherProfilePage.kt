package com.example.diaryprogram.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.diaryprogram.appbar.AppBar
import com.example.diaryprogram.data.UserProfileResponseDto

// api 연동 해야함
@Composable
fun OtherProfilePage(navController: NavHostController, userIds: Long) {
    val customfont = FontFamily(Font(R.font.nanumbarunpenr))

    // 상태 변수: 서버에서 가져온 데이터 저장
    var userProfile by remember { mutableStateOf<UserProfileResponseDto?>(null) }
    var isFollowing by rememberSaveable { mutableStateOf(true) } // 기존 코드 유지
    var isLoading by remember { mutableStateOf(true) } // 로딩 상태

    // 서버에서 데이터 가져오기
    LaunchedEffect(userIds) {
        isLoading = true
        loadUserProfile(apiService, userIds) { profile ->
            userProfile = profile
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF070301), Color(0xFF886B5F)),
                    start = Offset(0f, 0f),
                    end = Offset(0f, 3000f)
                )
            )
    ) {
        if (isLoading) {
            // 로딩 중
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "로딩 중...", color = Color.White, fontSize = 18.sp)
            }
        } else {
            userProfile?.let { profile ->
                Column(modifier = Modifier.padding(30.dp)) {
                    Spacer(modifier = Modifier.height(20.dp))

                    // 상단 백버튼
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.size(50.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.whiteback),
                                contentDescription = "백버튼",
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Box(
                        modifier = Modifier
                            .width(361.dp)
                            .height(443.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(color = colorResource(id = R.color.light_daisy))
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(20.dp))

                            // 프로필 이미지
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(profile.profileImage?.url),
                                    contentDescription = "프로필 사진",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // 사용자 이름
                            Text(
                                text = profile.name,
                                color = Color.White,
                                fontFamily = customfont
                            )

                            Spacer(modifier = Modifier.height(6.dp))
                            HorizontalDivider(color = Color.White, thickness = 1.dp)
                            Spacer(modifier = Modifier.height(20.dp))

                            // 좋아요, 일기 수, 팔로잉 수
                            Row {
                                Box(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .width(80.dp)
                                        .height(80.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(color = colorResource(id = R.color.box_daisy))
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.heart),
                                            contentDescription = "총 좋아요",
                                            modifier = Modifier.size(40.dp)
                                        )
                                        Text(
                                            text = "${profile.totalLikesCount}",
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontFamily = customfont
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .width(80.dp)
                                        .height(80.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(color = colorResource(id = R.color.box_daisy))
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "작성한 일기",
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontFamily = customfont
                                        )
                                        Text(
                                            text = "${profile.totalDiaryCount}",
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontFamily = customfont
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .width(80.dp)
                                        .height(80.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(color = colorResource(id = R.color.box_daisy))
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "팔로잉",
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontFamily = customfont
                                        )
                                        Text(
                                            text = "${profile.totalFollowCount}",
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontFamily = customfont
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // 팔로우/팔로잉 버튼 (기존 코드 유지)
                            if (isFollowing) {
                                Button(
                                    onClick = { isFollowing = false },
                                    modifier = Modifier.width(300.dp).height(45.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colorResource(R.color.dark_daisy)
                                    )
                                ) {
                                    Text(text = "팔로잉", color = Color.White)
                                }
                            } else {
                                Button(
                                    onClick = { isFollowing = true },
                                    modifier = Modifier.width(300.dp).height(45.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White
                                    )
                                ) {
                                    Text(text = "팔로우", color = colorResource(R.color.dark_daisy))
                                }
                            }
                        }
                    }
                }
            } ?: run {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "유저 정보를 불러오지 못했습니다.",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }
        }

        // 하단 AppBar
        AppBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp),
            navHostController = navController,
            option = 4
        )
    }
}