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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.diaryprogram.R
import com.example.diaryprogram.api.DiaryApi.deleteDiary
import com.example.diaryprogram.api.DiaryApi.fetchUserDiary
import com.example.diaryprogram.appbar.AppBar
import com.example.diaryprogram.data.UserDiaryResponseDto
import com.example.diaryprogram.geo.getAddressFromLatLng
import com.example.diaryprogram.util.utils


//해야함
@Composable
fun MyDiaryPage(navHostController: NavHostController, userID: Long, diaryID: Long, geo:Int) {
    val context = LocalContext.current
    var diaryDetails by remember { mutableStateOf<UserDiaryResponseDto?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var address by remember { mutableStateOf("") } // 주소 초기값은 빈 문자열로 설정
    var isLiked by remember { mutableStateOf(false) } // 좋아요 상태 관리


    LaunchedEffect(diaryDetails) {
        // diaryDetails가 null이 아닐 때만 주소 변환 로직 실행
        diaryDetails?.let { details ->
            address = getAddressFromLatLng(context, details.latitude, details.longitude)
        }
    }

    DisposableEffect(diaryID) {
        fetchUserDiary(
            userId = userID,
            diaryId = diaryID,
            onSuccess = { diary ->
                diaryDetails = diary
                isLoading = false
                isLiked=false
            },
            onFailure = { throwable ->
                errorMessage = throwable.message
                isLoading = false
            }
        )
        onDispose { }
    }



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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            if (geo==0) {
                                navHostController.popBackStack()
                            }
                            else{
                                navHostController.navigate("main")
                            }
                                  },
                        modifier = Modifier.size(50.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.whiteback),
                            contentDescription = "백버튼"
                        )
                    }

                    Button(
                        onClick = {
                            deleteDiary(
                                userId = userID,
                                diaryId = diaryID,
                                onSuccess = { response ->
                                    println("삭제 성공: $response")
                                    navHostController.navigate("main")
                                },
                                onFailure = { throwable ->
                                    println("삭제 실패: ${throwable.message}")
                                }
                            )
                        },
                        modifier = Modifier
                            .width(70.dp)
                            .height(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.new_red),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "삭제",
                            fontFamily = FontFamily(Font(R.font.nanumbarunpenb)),
                            fontSize = 11.sp,
                            color = Color.White
                        )
                    }


                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(color = colorResource(R.color.dark_daisy))
                ){

                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                    ){
                        Row (modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween){
                            Text(
                                text = diaryDetails?.title ?: "제목 없음",
                                textAlign = TextAlign.Start,
                                fontSize = 20.sp,
                                fontFamily = FontFamily(Font(R.font.nanumbarunpenb)),
                                color = Color.White,
                                modifier = Modifier.height(40.dp)
                            )
                            IconButton(
                                onClick = {
                                    ///수정 버튼 클릭 이벤트
                                },
                                modifier = Modifier.size(30.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.editor),
                                    contentDescription = "수정 버튼"
                                )
                            }
                        }
                        HorizontalDivider(color = Color.White, thickness = 1.dp)

                        Text (
                            text = "${diaryDetails?.date}" ?: "날짜 없음",
                            textAlign = TextAlign.Start,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.nanumbarunpenb)),
                            color = Color.White
                        )
                        HorizontalDivider(color = Color.White, thickness = 1.dp)

                        Row (modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically)
                        {
                            Text(
                                text = "주소: ${address}",
                                textAlign = TextAlign.Start,
                                fontSize = 13.sp,
                                fontFamily = FontFamily(Font(R.font.nanumbarunpenb)),
                                color = Color.White
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.locationicon),
                                contentDescription = "위치",
                                modifier = Modifier.size(18.dp),
                                tint = Color.Unspecified
                            )
                        }
                        HorizontalDivider(color = Color.White, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = diaryDetails?.content ?: "내용 없음",
                            textAlign = TextAlign.Start,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.nanumbarunpenb)),
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        )
                        // 이미지 받아와서 보여주게 api 연동
                        if (diaryDetails?.images?.isNotEmpty() == true) {
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(88.dp)
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(diaryDetails!!.images) { image ->
                                    utils.DisplayImage(base64String = image.url, size = 80.dp) // Base64 문자열을 DisplayImage 함수로 전달
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.height(108.dp))
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(
                                onClick = { // 자기꺼는 좋아요 못함
                                     },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Image(
                                    painter = painterResource(
                                        id = if (isLiked) R.drawable.heart else R.drawable.heart
                                    ),
                                    contentDescription = "좋아요 버튼",
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Text(
                                text = "${diaryDetails?.likesCount}명이 좋아요를 눌렀어요",
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.nanumbarunpenb)),
                                color = Color.White
                            )
                        }
                    }
                }


            }

            AppBar(modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp),
                navHostController = navHostController,
                option=2
            )
        }
    }

