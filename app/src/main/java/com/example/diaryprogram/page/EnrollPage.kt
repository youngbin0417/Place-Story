package com.example.diaryprogram.page

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.diaryprogram.R
import java.util.Calendar

@Composable
fun EnrollPage(navHostController: NavHostController) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    var title by remember { mutableStateOf("") }
    var userInput by remember { mutableStateOf("") }


    val dayOfWeekString = when(dayOfWeek) {
        Calendar.SUNDAY -> "일요일"
        Calendar.MONDAY -> "월요일"
        Calendar.TUESDAY -> "화요일"
        Calendar.WEDNESDAY -> "수요일"
        Calendar.THURSDAY -> "목요일"
        Calendar.FRIDAY -> "금요일"
        Calendar.SATURDAY -> "토요일"
        else -> "알 수 없음"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { navHostController.navigate("main") }) {
                Image(
                    painter = painterResource(id = R.drawable.backbutton),
                    contentDescription = "백버튼",
                    modifier = Modifier.size(100.dp)
                )
            }

            Text(
                text = "${year}년 ${month}월",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(5f), // Text를 중앙에 배치
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Button(onClick = { /*일기 등록 기능 api 추가*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent, // 추후 칼라 수정
                    contentColor = Color.Red
                )
            ){
                Text(text = "등록")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .border(width = 2.dp, color = Color.Gray) // 테두리 추가
                .padding(8.dp),
            //contentAlignment = Alignment.TopStart
        ){
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .heightIn(1000.dp)
            ) {
                Row {
                    Text(
                        text = "${day}일",
                        modifier = Modifier
                            .weight(5f), // Text를 중앙에 배치
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                    Text(
                        text = "$dayOfWeekString",
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                BasicTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(12.dp) // 모서리를 둥글게 설정
                        )
                        .padding(8.dp), // 내부 여백 추가
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next // 키보드에서 "다음" 버튼을 표시
                    ),
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 20.sp
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 8.dp, end = 8.dp), // 여백을 통해 내부 텍스트와 경계 사이 공간 확보
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (title.isEmpty()) {
                                Text(
                                    text = "제목을 입력하세요",
                                    style = TextStyle(color = Color.Gray, fontSize = 20.sp)
                                )
                            }
                            innerTextField()
                        }
                    }
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
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 8.dp, end = 8.dp), // 여백을 통해 내부 텍스트와 경계 사이 공간 확보
                            contentAlignment = Alignment.TopStart
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