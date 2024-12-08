package com.example.diaryprogram.page

import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.com.example.diaryprogram.geo.GeofenceHelper
import com.example.diaryprogram.R
import com.example.diaryprogram.api.DiaryApi.fetchUserDiary
import com.example.diaryprogram.api.DiaryApi.updateDiary
import com.example.diaryprogram.data.DiaryStatus
import com.example.diaryprogram.data.UserDiaryResponseDto
import com.example.diaryprogram.geo.getAddressFromLatLng
import com.example.diaryprogram.util.utils

@Composable
fun EditDiary(navController: NavHostController, userId: Long, diaryId: Long) {

    Log.d("EditDiaryDebug", "userId: $userId, diaryId: $diaryId")

    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var userInput by remember { mutableStateOf("") }
    val customfont = FontFamily(Font(R.font.nanumbarunpenb))
    var diaryPeriod by remember { mutableStateOf(0) }
    var diaryStatus by rememberSaveable  { mutableStateOf(DiaryStatus.PRIVATE) }
    var address by remember { mutableStateOf("주소를 가져오는 중...") }
    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var selectedEnum by remember { mutableStateOf(0) }
    val geofenceHelper = GeofenceHelper(context) // GeofenceHelper 인스턴스 생성
    var diaryDetails by remember { mutableStateOf<UserDiaryResponseDto?>(null) }
    val deletedImageUris = remember { mutableStateListOf<Uri>() } // 삭제된 이미지 URL을 저장하는 리스트

    LaunchedEffect(diaryDetails) {
        // diaryDetails가 null이 아닐 때만 주소 변환 로직 실행
        diaryDetails?.let { details ->
            address = getAddressFromLatLng(context, details.latitude, details.longitude)
            userInput = details.content
            title = details.title
        }
    }
    DisposableEffect(diaryId) {
        fetchUserDiary(
            userId = userId,
            diaryId = diaryId,
            onSuccess = { diary ->
                diaryDetails = diary
            },
            onFailure = { throwable ->

            }
        )
        onDispose { }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
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
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(50.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.whiteback),
                        contentDescription = "백버튼"
                    )
                }

                Text(
                    text = "일기 수정하기",
                    fontSize = 20.sp,
                    fontFamily = customfont,
                    color = Color.White,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )

// 오른쪽 등록 버튼
                IconButton(
                    onClick = {
                        //updateDiary(

                        //)
                    },
                    modifier = Modifier.size(50.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.check),
                        contentDescription = "등록 버튼"
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))

            Box(
                modifier = Modifier
                    .height(700.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(color = colorResource(R.color.dark_daisy))
            ){
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                ){
                    // 제목 입력 필드
                    BasicTextField(
                        value = title ?:"제목 없음",
                        onValueChange = { title=it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .border(
                                width = 2.dp,
                                color = Color.Transparent
                            ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 20.sp,
                            fontFamily = customfont
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                innerTextField()
                            }
                        }
                    )
                    HorizontalDivider(color = Color.White, thickness = 1.dp)

                    diaryDetails?.date?.let {
                        Text(
                            text = it,
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            fontFamily = customfont,
                            color = Color.White
                        )
                    }
                    HorizontalDivider(color = Color.White, thickness = 1.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "위치: $address",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontFamily = customfont
                        )
                        Image(
                            painter = painterResource(id = R.drawable.locationicon),
                            contentDescription = "위치 마크",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    HorizontalDivider(color = Color.White, thickness = 1.dp)

                    Spacer(modifier = Modifier.height(16.dp))

                    BasicTextField(
                        value = userInput ?:"내용 없음",
                        onValueChange = { userInput=it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .border(
                                width = 2.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(8.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 20.sp,
                            fontFamily = customfont
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.TopStart
                            ) {
                                innerTextField()
                            }
                        }
                    )


                    if (diaryDetails?.images?.isNotEmpty() == true) {
                        if (selectedImageUris.isNotEmpty()){
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(88.dp)
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(diaryDetails!!.images) { image ->
                                    utils.DisplayImage(
                                        base64String = image.url,
                                        size = 80.dp
                                    ) // Base64 문자열을 DisplayImage 함수로 전달
                                }
                                items(selectedImageUris) { uri ->
                                    Image(
                                        painter = rememberAsyncImagePainter(model = uri),
                                        contentDescription = "Selected Image",
                                        modifier = Modifier
                                            .size(80.dp)
                                    )
                                }
                            }
                        }
                        else {
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(88.dp)
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(diaryDetails!!.images) { image ->
                                    utils.DisplayImage(
                                        base64String = image.url,
                                        size = 80.dp
                                    ) // Base64 문자열을 DisplayImage 함수로 전달
                                }
                            }
                        }
                    }else {
                        Spacer(modifier = Modifier.height(100.dp))
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(7.dp))
                        Text(
                            text = "Period",
                            fontFamily = customfont,
                            color = Color.White,
                            fontSize = 12.sp
                        )

                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(30.dp)
                                .clickable { /* 매일알림 클릭 이벤트 */
                                    diaryPeriod = 1
                                }
                                .background(
                                    color = if (diaryPeriod == 1) colorResource(R.color.box_daisy) else Color.Transparent,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "항상 알림",
                                fontFamily = customfont,
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            text = "|",
                            fontFamily = customfont,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(30.dp)
                                .clickable { /* 1일마다 클릭 이벤트 */
                                    diaryPeriod = 2
                                }
                                .background(
                                    color = if (diaryPeriod == 2) colorResource(R.color.box_daisy) else Color.Transparent,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "1일 이후",
                                fontFamily = customfont,
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            text = "|",
                            fontFamily = customfont,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(30.dp)
                                .clickable { /* 1주마다 클릭 이벤트 */
                                    diaryPeriod = 3
                                }
                                .background(
                                    color = if (diaryPeriod == 3) colorResource(R.color.box_daisy) else Color.Transparent,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "1주 이후",
                                fontFamily = customfont,
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            text = "|",
                            fontFamily = customfont,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(30.dp)
                                .clickable { /* 1년마다 클릭 이벤트 */
                                    diaryPeriod = 4
                                }
                                .background(
                                    color = if (diaryPeriod == 4) colorResource(R.color.box_daisy) else Color.Transparent,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "1년 이후",
                                fontFamily = customfont,
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(10.dp))

                        MultiGalleryPickerButton { uris ->
                            selectedImageUris = uris // 선택된 이미지들의 URI 리스트 저장
                        }

                        Box(
                            modifier = Modifier
                                .width(70.dp)
                                .height(30.dp)
                                .clickable { /*모두공개*/
                                    diaryStatus = DiaryStatus.PUBLIC
                                    selectedEnum = 1
                                }
                                .background(
                                    color = if (selectedEnum == 1) colorResource(R.color.box_daisy) else Color.Transparent,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "모두 공개",
                                fontFamily = customfont,
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }

                        Text(
                            text = "|",
                            fontFamily = customfont,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                        Box(
                            modifier = Modifier
                                .width(70.dp)
                                .height(30.dp)
                                .clickable { /*친구공개*/
                                    selectedEnum = 2
                                    diaryStatus = DiaryStatus.FOLLOWER
                                }
                                .background(
                                    color = if (selectedEnum == 2) colorResource(R.color.box_daisy) else Color.Transparent,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "친구 공개",
                                fontFamily = customfont,
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            text = "|",
                            fontFamily = customfont,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                        Box(
                            modifier = Modifier
                                .width(70.dp)
                                .height(30.dp)
                                .clickable { /*나만보기*/
                                    selectedEnum = 3
                                    diaryStatus = DiaryStatus.PRIVATE
                                }
                                .background(
                                    color = if (selectedEnum == 3) colorResource(R.color.box_daisy) else Color.Transparent,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "나만 보기",
                                fontFamily = customfont,
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

        }

    }
}
