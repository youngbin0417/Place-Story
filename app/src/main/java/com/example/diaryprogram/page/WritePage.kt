package com.example.diaryprogram.page

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.example.diaryprogram.api.DiaryApi.createDiary
import com.example.diaryprogram.data.DiaryRequestDto
import com.example.diaryprogram.data.DiaryStatus
import com.example.diaryprogram.geo.getAddressFromLatLng
import com.google.android.gms.location.Geofence
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate
import java.util.Calendar
// 프론트 완료
@Composable
fun WritePage(navHostController: NavHostController, initialPosition: LatLng,
              userId: Long) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    var title by remember { mutableStateOf("") }
    var userInput by remember { mutableStateOf("") }
    val customfont = FontFamily(Font(R.font.nanumbarunpenb))
    var diarylocation by remember { mutableStateOf(initialPosition) }
    var diaryPeriod by remember { mutableStateOf(0) }
    var diaryStatus by rememberSaveable  { mutableStateOf(DiaryStatus.PRIVATE) }
    var showSearchLocation by remember { mutableStateOf(false) }
    var address by remember { mutableStateOf("주소를 가져오는 중...") }
    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var selectedEnum by remember { mutableStateOf(0) }
    val geofenceHelper = GeofenceHelper(context) // GeofenceHelper 인스턴스 생성

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

    LaunchedEffect(diarylocation) {
        // 좌표가 변경될 때 동까지만 가져오기
        address = getAddressFromLatLng(context, diarylocation.latitude, diarylocation.longitude)
    }

    if (showSearchLocation) {
        // SearchLocation 화면 표시
        SearchLocation(
            initialPosition = initialPosition,
            onBack = { position ->
                diarylocation = position // 선택된 위치 저장
                showSearchLocation = false // SearchLocation 닫기
            }
        )
    }
    else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
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

                    Text(
                        text = "일기 작성하기",
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
                            if (title==""){
                                Toast.makeText(context,"제목을 입력해주세요", Toast.LENGTH_SHORT).show()
                            }
                            else if (userInput==""){
                                Toast.makeText(context,"본문을 입력해주세요", Toast.LENGTH_SHORT).show()
                            }
                            else{
                            createDiary(
                                userId = userId,
                                diaryRequestDto =
                                DiaryRequestDto(
                                    latitude = diarylocation.latitude,
                                    longitude = diarylocation.longitude,
                                    title = title,
                                    content = userInput,
                                    date = LocalDate.now(),
                                    diaryStatus = diaryStatus
                                ),
                                imageUris = selectedImageUris,
                                contentResolver = context.contentResolver,
                                onSuccess = { response ->
                                    val diaryId = response?.data // 서버에서 반환된 Diary ID 추출
                                    if (diaryId != null) {
                                    Toast.makeText(context, "Diary created successfully!", Toast.LENGTH_SHORT).show()

                                    // 지오펜싱 등록
                                    if (geofenceHelper.hasLocationPermission()) {
                                        geofenceHelper.addGeofence(
                                            diaryId = diaryId,
                                            latitude = diarylocation.latitude,
                                            longitude = diarylocation.longitude,
                                            radius = 1000f, // 반경 1km
                                            expirationDuration = Geofence.NEVER_EXPIRE,
                                            pendingIntent = geofenceHelper.getGeofencePendingIntent(context),
                                            onCompleteListener = { task ->
                                                if (task.isSuccessful) {
                                                    Log.d("GeofenceHelper", "지오펜싱 등록 성공")
                                                } else {
                                                    Log.e("GeofenceHelper", "지오펜싱 등록 실패: ${task.exception}")
                                                }
                                            }
                                        )
                                    } else {
                                        Toast.makeText(context, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                                    }}else {
                                        Toast.makeText(context, "Diary ID가 없습니다.", Toast.LENGTH_SHORT).show()
                                    }

                                },
                                onError = { errorMessage ->
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                }
                            )
                                navHostController.navigate("main")

                            }
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
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                    ) {
                        // 제목 입력 필드
                        BasicTextField(
                            value = title,
                            onValueChange = { title = it },
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
                                    if (title.isEmpty()) {
                                        Text(
                                            text = "제목을 작성해주세요",
                                            style = TextStyle(
                                                color = colorResource(R.color.letter_daisy),
                                                fontSize = 20.sp,
                                                fontFamily = customfont
                                            )
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )

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
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "위치: $address",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontFamily = customfont
                            )
                            IconButton(
                                onClick = {
                                    showSearchLocation = true
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.emptylocationicon),
                                    contentDescription = "위치 버튼"
                                )
                            }
                        }

                        HorizontalDivider(color = Color.White, thickness = 1.dp)

                        Spacer(modifier = Modifier.height(16.dp))

                        // 사용자 입력 필드
                        BasicTextField(
                            value = userInput,
                            onValueChange = { userInput = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)
                                .border(
                                    width = 2.dp,
                                    color = Color.Gray,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(8.dp),
                            textStyle = TextStyle(
                                color = Color.White,
                                fontSize = 20.sp,
                                fontFamily = customfont
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            decorationBox = { innerTextField ->
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.TopStart
                                ) {
                                    if (userInput.isEmpty()) {
                                        Text(
                                            text = "이번 여정은 별이 빛나는 것처럼\n무척이나 아름다웠어...",
                                            style = TextStyle(
                                                color = colorResource(R.color.letter_daisy),
                                                fontSize = 15.sp,
                                                fontFamily = customfont
                                            )
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )

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
                                        diaryPeriod=1
                                    }
                                    .background(
                                        color = if (diaryPeriod == 1) colorResource(R.color.box_daisy) else Color.Transparent,
                                        shape = RoundedCornerShape(5.dp)
                                    )                                    .padding(horizontal = 4.dp, vertical = 2.dp),
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
                                        diaryPeriod=2
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
                                        diaryPeriod=3
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
                                        diaryPeriod=4
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
                                        diaryStatus=DiaryStatus.PUBLIC
                                        selectedEnum=1
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
                                        selectedEnum=2
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
                                        selectedEnum=3
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
}

@Composable
fun MultiGalleryPickerButton(onImagesSelected: (List<Uri>) -> Unit) {
    // 갤러리에서 여러 이미지를 선택하기 위한 Launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris ->
            onImagesSelected(uris) // 선택된 이미지들의 URI 리스트를 콜백으로 전달
        }
    )

    Box(
        modifier = Modifier
            .width(30.dp)
            .height(30.dp)
            .clickable { launcher.launch("image/*") } // 갤러리 열기 (이미지 MIME 타입 지정)
    ) {
        Icon(
            painter = painterResource(R.drawable.galleryicon),
            contentDescription = "갤러리",
            tint = Color.Unspecified
        )
    }
}

