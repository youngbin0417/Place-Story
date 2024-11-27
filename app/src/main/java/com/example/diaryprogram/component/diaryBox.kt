package com.example.diaryprogram.component

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.diaryprogram.data.DiaryResponseDto

@Composable
fun DiaryBox(navController: NavHostController, diaryInfo: DiaryResponseDto) {
    Box(modifier = Modifier
        .width(360.dp).height(110.dp)
        .clickable(onClick = {
            // 네비게이션 사용
            navController.navigate("other_profile_page/${diaryInfo.diaryIds}")
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
            if (diaryInfo.diaryImages.isNullOrEmpty()) {
                // diaryImages가 null이거나 빈 리스트일 때 처리
                Image(
                    painter = painterResource(R.drawable.empty), // Use Coil to load URL images
                    contentDescription = "no picture",
                    modifier = Modifier
                        .size(50.dp)
                )
            }
            else{
                val firstImage = diaryInfo.diaryImages.first()
                Image(
                    painter = rememberAsyncImagePainter(model = firstImage.url), // Use Coil to load URL images
                    contentDescription = "${diaryInfo.diaryIds}'s diary picture",
                    modifier = Modifier
                        .size(50.dp)
                )
            }
            Column {
                Text(
                    text = "${diaryInfo.diaryTitles}",
                    fontFamily = FontFamily(Font(R.font.nanumbarunpenb)),
                    fontSize = 18.sp
                )
                Text(
                    text = "${diaryInfo.date}",
                    fontFamily = FontFamily(Font(R.font.nanumbarunpenb)),
                    fontSize = 12.sp
                )
            }

        }
    }
}