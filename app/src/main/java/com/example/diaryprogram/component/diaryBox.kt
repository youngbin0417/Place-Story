package com.example.diaryprogram.component

import android.widget.ImageButton
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.diaryprogram.api.DiaryApi.likeDiary
import com.example.diaryprogram.data.DiaryResponseDto
import com.example.diaryprogram.geo.getAddressFromLatLng
import com.example.diaryprogram.page.BrowseMineDiaryPage
import com.example.diaryprogram.util.utils
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate


@Composable
fun DiaryBox(
    navController: NavHostController,
    userId: Long,
    diaryInfo: DiaryResponseDto,
    onDiaryClick: (Long) -> Unit,
    option: Int
) {
    val context = LocalContext.current
    var isClicked by remember { mutableStateOf(false) }
    var address by remember { mutableStateOf("...") }
    var isFollowing by remember { mutableStateOf(true) }


    LaunchedEffect(diaryInfo) {
        address = getAddressFromLatLng(
            context,
            diaryInfo.latitude,
            diaryInfo.longitude
        )
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .width(360.dp)
            .height(100.dp)
            .clickable(
                onClick = {
                    diaryInfo.diaryId?.let { onDiaryClick(it) } // diaryId null 체크
                }
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(colorResource(R.color.light_daisy))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // 프로필 이미지 처리

            diaryInfo?.profileImage?.let { image ->
                if (image.url == "default.jpg") {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Default Profile Icon",
                        modifier = Modifier.size(60.dp)
                            .clip(CircleShape)
                            .clickable {
                                if (option == 0) {
                                    navController.navigate("other_profile_page/${diaryInfo.userId}/$isFollowing")
                                } else if (option == 1) {
                                    isFollowing = false
                                    navController.navigate("other_profile_page/${diaryInfo.userId}/$isFollowing")
                                }
                            }
                    )
                } else {
                    utils.DisplayImage(base64String = image.url, size = 80.dp)
                }
            } ?: Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Default Profile Icon",
                modifier = Modifier.size(60.dp)
                    .clip(CircleShape)
                    .clickable {
                        if (option == 0) {
                            navController.navigate("other_profile_page/${diaryInfo.userId}/$isFollowing")
                        } else if (option == 1) {
                            isFollowing = false
                            navController.navigate("other_profile_page/${diaryInfo.userId}/$isFollowing")
                        }
                    }
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start,
            ) {
                // 제목 null 처리
                Text(
                    text = diaryInfo.diaryTitle ?: "Untitled", // null 처리
                    fontFamily = FontFamily(Font(R.font.nanumbarunpenb)),
                    fontSize = 16.sp
                )
                Text(
                    text = address,
                    fontFamily = FontFamily(Font(R.font.nanumbarunpenr)),
                    fontSize = 10.sp
                )
                Text(
                    text = "${diaryInfo.date}" ?: "no date",
                    fontFamily = FontFamily(Font(R.font.nanumbarunpenr)),
                    fontSize = 10.sp
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Transparent)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Column {

                    if (isClicked) {
                        IconButton(
                            onClick = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    isClicked = false // 상태 복구
                                }
                            },
                            modifier = Modifier
                                .size(50.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.heart),
                                contentDescription = "좋아요",
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    } else {
                        IconButton(
                            onClick = {
                                diaryInfo.diaryId?.let { diaryId ->
                                    likeDiary(
                                        apiService = apiService,
                                        userId = userId,
                                        diaryId = diaryId,
                                        onSuccess = {
                                            isClicked = true // 성공 시 좋아요 상태로 전환
                                        },
                                        onFailure = { throwable ->
                                            Toast.makeText(
                                                context,
                                                "좋아요 실패: ${throwable.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            isClicked = false // 실패 시 상태 복구
                                        }
                                    )
                                }
                            },
                            modifier = Modifier
                                .size(50.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.emptyheart),
                                contentDescription = "좋아요",
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }


                }
            }
        }
    }
}
