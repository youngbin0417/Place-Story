package com.example.diaryprogram.Page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.diaryprogram.R

@Composable
fun LoginPage(navHostController: NavHostController) {
    Column {
        Image( // 추후 수정
            painter = painterResource(id = R.drawable.test),
            contentDescription = "앱 로고 화면",
            modifier =Modifier.size(200.dp),
            Alignment.Center
        )
    }
}