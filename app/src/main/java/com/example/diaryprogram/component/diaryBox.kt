package com.example.diaryprogram.component

import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate


@Composable
fun DiaryBox(
    navController: NavHostController,
    userId: Long,
    diaryInfo: DiaryResponseDto,
    option: Int,
    onDiaryClick: (Long) -> Unit
) {
    val context = LocalContext.current
    val formattedDate = diaryInfo.date?.toString() ?: "Unknown Date"
    var isClicked by remember { mutableStateOf(false) }
    var address by remember { mutableStateOf("...") }
    var diarylocation by remember {
        mutableStateOf(
            LatLng(
                diaryInfo.latitude ?: 0.0, // null 처리
                diaryInfo.longitude ?: 0.0 // null 처리
            )
        )
    }

    LaunchedEffect(diarylocation) {
        address = getAddressFromLatLng(
            context,
            diarylocation.latitude,
            diarylocation.longitude
        )
    }

    Box(
        modifier = Modifier
            .width(360.dp)
            .height(110.dp)
            .clickable(onClick = {
                diaryInfo.diaryId?.let { onDiaryClick(it) } // diaryId null 체크
            })
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(colorResource(R.color.light_daisy))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 프로필 이미지 처리
            if (diaryInfo.profileImage?.url.isNullOrEmpty()) {
                Image(
                    painter = painterResource(R.drawable.profile),
                    contentDescription = "no picture",
                    modifier = Modifier.size(50.dp)
                )
            } else {
                val firstImage = diaryInfo.profileImage?.url ?: ""
                Image(
                    painter = rememberAsyncImagePainter(model = firstImage),
                    contentDescription = "${diaryInfo.diaryId}'s diary picture",
                    modifier = Modifier.size(50.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                // 제목 null 처리
                Text(
                    text = diaryInfo.diaryTitles ?: "Untitled", // null 처리
                    fontFamily = FontFamily(Font(R.font.nanumbarunpenb)),
                    fontSize = 16.sp
                )
                Text(
                    text = address,
                    fontFamily = FontFamily(Font(R.font.nanumbarunpenr)),
                    fontSize = 10.sp
                )
                Text(
                    text = formattedDate,
                    fontFamily = FontFamily(Font(R.font.nanumbarunpenr)),
                    fontSize = 10.sp
                )
            }

            Column {

                if (isClicked) {
                    IconButton(
                        onClick = { isClicked = false },
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
                            isClicked = true
                            diaryInfo.diaryId?.let {
                                likeDiary(
                                    apiService = apiService,
                                    userId = userId,
                                    diaryId = it
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
