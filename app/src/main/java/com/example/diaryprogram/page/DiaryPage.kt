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
import androidx.compose.ui.platform.LocalContext
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
import coil.compose.rememberAsyncImagePainter
import com.example.diaryprogram.R
import java.util.Calendar

//해야함
@Composable
fun DiaryPage(navHostController: NavHostController, userID: Long, diaryID: Long) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance() // 서버로 받아옴
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    var title by remember { mutableStateOf("") } // 서버로 받아옴
    val userInput by remember { mutableStateOf("") } //서버로 받아옴
    val customfont = FontFamily(Font(R.font.nanumbarunpenb))
    //val diary_location by remember { mutableStateOf(38.00,38.00) } // 서버로 받아옴
    var address by remember { mutableStateOf("주소를 가져오는 중...") }
    val enums by remember { mutableStateOf("PRIVATE") }// 서버로 받아옴
    val selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) } // 서버로 받아옴

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
                if(enums=="PRIVATE"){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
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
                        IconButton(
                            onClick = { navHostController.navigate("edit/{$diaryID}") },
                            modifier = Modifier.size(50.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.editor),
                                contentDescription = "수정",
                                modifier = Modifier.size(50.dp)
                            )
                        }

                    }
                }
                else{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
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
            }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(color = colorResource(R.color.dark_daisy))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                    ) {
                        // 제목 입력 필드
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .border(
                                    width = 2.dp,
                                    color = Color.Transparent
                                ),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = title,
                                style = TextStyle(
                                    color = colorResource(R.color.letter_daisy),
                                    fontSize = 20.sp,
                                    fontFamily = customfont
                                ),
                                modifier = Modifier.fillMaxSize()
                            )
                        }


                        HorizontalDivider(color = Color.White, thickness = 1.dp)

                        Text(
                            text = "${year}/${month}/${day} $dayOfWeekString",
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            fontFamily = customfont,
                            color = Color.White
                        )

                        HorizontalDivider(color = Color.White, thickness = 1.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth().height(24.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "위치: $address",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontFamily = customfont
                            )
                        }

                        HorizontalDivider(color = Color.White, thickness = 1.dp)

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .border(
                                    width = 2.dp,
                                    color = Color.Gray,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(8.dp),
                            contentAlignment = Alignment.TopStart
                        ) {
                            Text(
                                text = userInput,
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 20.sp
                                ),
                                modifier = Modifier.fillMaxSize()
                            )
                        }

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
                                            .size(100.dp)
                                    )
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.height(108.dp))
                        }



                    }
                }

            }

        }
    }

