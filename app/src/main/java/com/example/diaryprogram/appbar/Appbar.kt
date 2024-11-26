package com.example.diaryprogram.appbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.diaryprogram.R

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    option: Int //0 메인, 1 일기작성, 2 나의일기, 3 공개일기, 4 이웃일기, 5 프로필
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp) // 위아래 여백
            .background(Color.Transparent), // 배경색 (갈색)
        horizontalArrangement = Arrangement.SpaceAround, // 아이템 간격 고르게
        verticalAlignment = Alignment.CenterVertically // 세로 중앙 정렬
    ) {
        // 각 아이템 추가
        if (option==1){
            AppBarItem(
                iconResId = R.drawable.selectedbuttonwritediary,
                onClick = { navHostController.navigate("write") }
            )
        }
        else{
            AppBarItem(
                iconResId = R.drawable.buttonwritediary,
                onClick = { navHostController.navigate("write") }
            )
        }

        if (option==2){
            AppBarItem(
                iconResId = R.drawable.selectedbuttonmydiarys,
                onClick = { navHostController.navigate("browseMine") }
            )
        }
        else {
            AppBarItem(
                iconResId = R.drawable.buttonmydiarys,
                onClick = { navHostController.navigate("browseMine") }
            )
        }

        if (option==3){
            AppBarItem(
                iconResId = R.drawable.selectedbuttonpublic,
                onClick = { navHostController.navigate("browsePublic") }
            )
        }
        else {
            AppBarItem(
                iconResId = R.drawable.buttonpublic,
                onClick = { navHostController.navigate("browsePublic") }
            )
        }

        if (option==4){
            AppBarItem(
                iconResId = R.drawable.selectedbuttonfollowers,
                onClick = { navHostController.navigate("browseFollows") }
            )

        }
        else {
            AppBarItem(
                iconResId = R.drawable.buttonfollowers,
                onClick = { navHostController.navigate("browseFollow") }
            )
        }

        if (option==5){
            AppBarItem(
                iconResId = R.drawable.selectedbuttonmyprofile,
                onClick = { navHostController.navigate("profile") }
            )
        }
        else {
            AppBarItem(
                iconResId = R.drawable.buttonmyprofile,
                onClick = { navHostController.navigate("profile") }
            )
        }
    }
}

@Composable
fun AppBarItem(iconResId: Int, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier.size(50.dp), // 아이콘 크기
            tint = Color.White // 아이콘 색상
        )
    }
}

