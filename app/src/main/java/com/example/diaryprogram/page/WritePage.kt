package com.example.diaryprogram.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.diaryprogram.R
import com.google.android.gms.maps.model.LatLng
import java.util.Calendar

@Composable
fun WritePage(navHostController: NavHostController, initialPosition: LatLng) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    var title by remember { mutableStateOf("") }
    var userInput by remember { mutableStateOf("") }
    val customfont = FontFamily(Font(R.font.nanumbarunpenb))
    var diary_location by remember { mutableStateOf("") }

    val dayOfWeekString = when(dayOfWeek) {
        Calendar.SUNDAY -> "SUN"
        Calendar.MONDAY -> "MON"
        Calendar.TUESDAY -> "TUE"
        Calendar.WEDNESDAY -> "WED"
        Calendar.THURSDAY -> "THU"
        Calendar.FRIDAY -> "FRI"
        Calendar.SATURDAY -> "SAT"
        else -> "ERROR"
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            Brush.linearGradient(
                colors = listOf(Color(0xFF070301), Color(0xFF886B5F)),
                start = Offset(0f, 0f),
                end = Offset(0f, 3000f)
            ) // 그라데이션 세팅
        )) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), // Row 양쪽 여백 추가
                verticalAlignment = Alignment.CenterVertically // 세로 중앙 정렬
            ) {
                // 왼쪽 백버튼
                IconButton(
                    onClick = { navHostController.navigate("main") },
                    modifier = Modifier.size(50.dp) // 버튼 크기 고정
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.whiteback),
                        contentDescription = "백버튼"
                    )
                }

                // 중앙 텍스트
                Text(
                    text = "일기 작성하기",
                    fontSize = 20.sp,
                    fontFamily = customfont,
                    color = Color.White,
                    modifier = Modifier
                        .weight(1f) // 텍스트를 중앙에 배치하기 위해 가중치 부여
                        .padding(horizontal = 16.dp), // 양쪽 버튼과 간격 확보
                    textAlign = TextAlign.Center // 텍스트 중앙 정렬
                )

                // 오른쪽 등록 버튼
                IconButton(
                    onClick = { /*서버에 일기 올리는 함수*/ },
                    modifier = Modifier.size(50.dp) // 버튼 크기 고정
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.check),
                        contentDescription = "등록 버튼"
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .border(width = 1.dp, color = Color.Transparent)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(color = colorResource(R.color.dark_daisy))
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .heightIn(1000.dp)
                ) {
                    BasicTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .border(
                                width = 2.dp,
                                color = Color.Transparent
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next // 키보드에서 "다음" 버튼을 표시
                        ),
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 20.sp
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (title.isEmpty()) {
                                    Text(
                                        text = "제목을 작성해주세요",
                                        style = TextStyle(color = colorResource(R.color.letter_daisy),
                                            fontSize = 20.sp,
                                            fontFamily = customfont)
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    HorizontalDivider(
                        color = Color.White,
                        thickness = 1.dp
                    )

                    Text(
                        text = "${year}/${month}/${day} $dayOfWeekString",
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontFamily = customfont,
                        color = Color.White
                    )

                    HorizontalDivider(
                        color = Color.White,
                        thickness = 1.dp
                    )

                    Row {
                        Text(
                            text = "위치: ",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            fontFamily = customfont,
                        )
                        IconButton(
                            onClick = { navHostController.navigate("searchLocation") },
                            modifier = Modifier

                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.locationicon),
                                contentDescription = "select location button"
                            )
                        }
                    }

                    HorizontalDivider(
                        color = Color.White,
                        thickness = 1.dp
                    )


                    Spacer(modifier = Modifier.height(16.dp))

                    BasicTextField(
                        value = userInput,
                        onValueChange = { userInput = it },
                        modifier = Modifier
                            .height(500.dp)
                            .fillMaxWidth()
                            .border(
                                width = 2.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(12.dp) // 모서리를 둥글게 설정
                            )
                            .padding(8.dp), // 내부 여백 추가
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 20.sp
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done // 키보드에서 "다음" 버튼을 표시
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(
                                        start = 8.dp,
                                        end = 8.dp
                                    ), // 여백을 통해 내부 텍스트와 경계 사이 공간 확보
                                contentAlignment = Alignment.TopStart,
                            ) {
                                if (userInput.isEmpty()) {
                                    Text(
                                        text = "오늘은...",
                                        style = TextStyle(color = Color.Gray, fontSize = 20.sp),
                                        modifier = Modifier

                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                }
            }
        }

    }
}
/*
* title
location (위치 정보) 자동
diary status (공개 / 팔로워만 / 나만)
Date (날짜) 자동
period ( 매번 / 알림이 뜨고 1년 후 / 알림이 뜨고 3년 후 / 알림 없음 )
images (사진들)
content (줄 글) 완료
등록
* */