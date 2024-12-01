package com.example.diaryprogram.page

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.diaryprogram.R
import com.example.diaryprogram.appbar.AppBar
import com.google.android.gms.maps.model.LatLng
import java.util.Calendar

//해야함
@Composable
fun DiaryPage(navHostController: NavHostController, userID: Long, diaryID: Long) {

    val option = 1 // api에서 받아올거임
    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    /*LaunchedEffect(diary_location) {
        // 좌표가 변경될 때 동까지만 가져오기
        address = getAddressFromLatLng(context, diary_location.latitude, diary_location.longitude)
    }*/

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF070301), Color(0xFF886B5F)), // 시작과 끝 색상
                        start = Offset(0f, 0f),   // 시작 지점
                        end = Offset(0f, 3000f) // 끝 지점
                    ) // 그라데이션 세팅
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navHostController.popBackStack() },
                        modifier = Modifier.size(50.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.whiteback),
                            contentDescription = "백버튼"
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(675.dp)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(color = colorResource(R.color.dark_daisy))
                ){

                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                    ){
                        Text(
                            text = "제목", // api 연동
                            textAlign = TextAlign.Start,
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.nanumbarunpenb)),
                            color = Color.White,
                            modifier = Modifier.height(40.dp)
                        )
                        HorizontalDivider(color = Color.White, thickness = 1.dp)

                        Text(
                            text = "날짜",
                            textAlign = TextAlign.Start,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.nanumbarunpenb)),
                            color = Color.White
                        )
                        HorizontalDivider(color = Color.White, thickness = 1.dp)

                        Text(
                            text = "주소",
                            textAlign = TextAlign.Start,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.nanumbarunpenb)),
                            color = Color.White
                        )
                        HorizontalDivider(color = Color.White, thickness = 1.dp)

                        Text(
                            text = "....", // api 연동 후, 받아올 값
                            textAlign = TextAlign.Start,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.nanumbarunpenb)),
                            color = Color.White
                        )
                        // 이미지 받아와서 보여주게 api 연동
                        if (selectedImageUris.isNotEmpty()) {
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp) // 이미지 간 간격 설정
                            ) {
                                items(selectedImageUris) { uri ->
                                    Image(
                                        painter = rememberAsyncImagePainter(model = uri),
                                        contentDescription = "Selected Image",
                                        modifier = Modifier
                                            .size(80.dp)
                                    )
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.height(108.dp))
                        }

                    }
                }


            }

            AppBar(modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp),
                navHostController = navHostController,
                option=option// 이후 api 연동해서 마무리
            )
        }
    }

