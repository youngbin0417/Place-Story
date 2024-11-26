package com.example.diaryprogram.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.diaryprogram.R
//해야함
@Composable
fun DiaryPage(navHostController: NavHostController) {

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        IconButton(onClick = { navHostController.navigate("browse") }) {
            Image(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "백버튼",
                modifier =Modifier.size(50.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .height(36.dp)
                .background(Color.LightGray, shape = RectangleShape)
                .padding(8.dp)
                .fillMaxWidth()
        ){
            Text(
                text = "작성자, 날짜", // 서버에서 가져올 값
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .background(Color.LightGray, shape = RectangleShape)
                .padding(8.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text(
                text = "오늘은...", // 서버에서 가져올 값
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.TopStart)
            )
        }
    }
}

