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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import java.time.LocalDate



//api 확인해야함
@Composable
fun DiaryBox(navController: NavHostController,userId:Long, diaryInfo: DiaryResponseDto) {
    //val year = diaryInfo.date?.year ?: "Unknown Year"
    //val formattedDate = diaryInfo.date?.toString() ?: "Unknown Date"
    var isClicked by remember {mutableStateOf(false)}

    Box(modifier = Modifier
        .width(360.dp).height(110.dp)
        .clickable(onClick = {
            // 해당 일기 상세보기
        })
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(colorResource(R.color.light_daisy))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            if (diaryInfo.profileImage?.url.isNullOrEmpty()) {
                // profileImages가 null이거나 빈 리스트일 때 처리
                Image(
                    painter = painterResource(R.drawable.profile), // Use Coil to load URL images
                    contentDescription = "no picture",
                    modifier = Modifier
                        .size(50.dp)
                )
            }
            else{
                val firstImage = diaryInfo.profileImage
                Image(
                    painter = rememberAsyncImagePainter(model = firstImage), // Use Coil to load URL images
                    contentDescription = "${diaryInfo.diaryId}'s diary picture",
                    modifier = Modifier
                        .size(50.dp)
                )
            }
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    text = diaryInfo.diaryTitles,
                    fontFamily = FontFamily(Font(R.font.nanumbarunpenb)),
                    fontSize = 16.sp
                )
                /*Text(
                    text =,
                    fontFamily = FontFamily(Font(R.font.nanumbarunpenr)),
                    fontSize = 10.sp
                )
                */
                /*Text(
                    text = formattedDate,
                    fontFamily = FontFamily(Font(R.font.nanumbarunpenr)),
                    fontSize = 10.sp
                )*/
            }

            Column {
                Image(
                    painter = painterResource(R.drawable.profile), // 이미지 api 연동 필요
                    contentDescription = "프로필",
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                )

                if (isClicked){
                    Button(
                        onClick = { isClicked = false },
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.heart),
                            contentDescription = "좋아요"
                        )
                    }
                }
                else{
                    Button(
                        onClick = {
                            isClicked = true
                            likeDiary(
                                apiService = apiService,
                                userId = userId,
                                diaryId = diaryInfo.diaryId
                            )
                                  },
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                    ){
                        Image(
                            painter = painterResource(R.drawable.emptyheart),
                            contentDescription = "좋아요"
                        )
                    }
                }


            }

        }
    }
}