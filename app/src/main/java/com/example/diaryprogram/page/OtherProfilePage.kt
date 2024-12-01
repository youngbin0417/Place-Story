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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.diaryprogram.R
import com.example.diaryprogram.appbar.AppBar
// api 연동 해야함
@Composable
fun OtherProfilePage(navController: NavHostController, userIds: Long) {
    val customfont = FontFamily(Font(R.font.nanumbarunpenr))
    // api 연동시에, userIds 통해서 서버에서 받아와서 설정할 값
    var isFollowing by rememberSaveable { mutableStateOf(true) }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            Brush.linearGradient(
                colors = listOf(Color(0xFF070301), Color(0xFF886B5F)),
                start = Offset(0f, 0f),
                end = Offset(0f, 3000f)
            ) // 그라데이션 세팅
        )
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding((16.dp)),
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
        }
        Column(modifier = Modifier.padding(30.dp)) {

            Spacer(modifier = Modifier.height(100.dp))

            Box(modifier = Modifier
                .width(361.dp)
                .height(443.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(color = colorResource(id = R.color.light_daisy))
            ) {
                Column(modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row (modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center){
                        Image( // api 연동 필요 -> 기본일때 해당 사진, 아니면 서버에서 받은 사진
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "프로필 사진",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Transparent, CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "사용자의 이름",
                        color = Color.White,
                        fontFamily = customfont
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    HorizontalDivider(
                        color = Color.White,
                        thickness = 1.dp
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Row {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .width(70.dp)
                                .height(70.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = colorResource(id = R.color.box_daisy))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Image(
                                    painter = painterResource(id = R.drawable.heart),
                                    contentDescription = "총 좋아요",
                                    modifier = Modifier.size(40.dp)
                                )

                                Text(
                                    text = "123", // api 연동 필요
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontFamily = customfont
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .width(70.dp)
                                .height(70.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = colorResource(id = R.color.box_daisy))
                                .clickable { navController.navigate("browseFollow")}
                        )
                        {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
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
                                    text = "123", // api 연동 필요
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontFamily = customfont
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .width(70.dp)
                                .height(70.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = colorResource(id = R.color.box_daisy))
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center, // 수직 중앙 정렬
                                horizontalAlignment = Alignment.CenterHorizontally // 수평 중앙 정렬
                            ) {
                                Text(
                                    text = "팔로잉",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontFamily = customfont
                                )
                                Text(
                                    text = "123", // API 연동 필요
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontFamily = customfont
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    if (isFollowing){
                        Button(onClick = { isFollowing=false },
                            modifier = Modifier.width(300.dp).height(45.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.dark_daisy)
                            )
                        ) {
                            Text(text = "팔로잉",
                                color = Color.White)
                        }
                    }
                    else {
                        Button(
                            onClick = { isFollowing=true },
                            modifier = Modifier.width(300.dp).height(45.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White // 버튼 배경색 설정
                            )
                        ) {
                            Text(text = "팔로우",
                                color = colorResource(R.color.dark_daisy)
                            )
                        }
                    }
                }
            }
        }

        AppBar(modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 40.dp),
            navHostController = navController,
            option=4
        )
    }
}