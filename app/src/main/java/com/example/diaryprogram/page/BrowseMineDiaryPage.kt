package com.example.diaryprogram.page

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.diaryprogram.R
import com.example.diaryprogram.api.DiaryApi.fetchAllDiaries
import com.example.diaryprogram.appbar.AppBar
import com.example.diaryprogram.component.DiaryBox
import com.example.diaryprogram.data.DiaryResponseDto
import com.example.diaryprogram.data.DiaryStatus

// 해야함
@Composable
fun BrowseMineDiaryPage(navHostController: NavHostController, userId: Long) {
    val diaryListState = remember { mutableStateOf<List<DiaryResponseDto>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        isLoading.value = true
        fetchAllDiaries(
            userId = userId,
            diaryStatus = DiaryStatus.PRIVATE,
            page = 0,
            size = 10,
            onSuccess = { response ->
                Log.d("ResponseCheck", "Content size: ${response.content.size}")
                Log.d("ResponseCheck", "Content data: ${response.content}")
                diaryListState.value = response.content
                //isLoading.value = false // 로딩 상태 업데이트
            },
            onFailure = { error ->
                Log.e("BrowseMineDiaryPage", "Failed to fetch diaries: ${error.message}")
                Log.e("JSONMapping", "Failed to map JSON: ${error.message}")
                isLoading.value = false
            }
        )
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF070301), Color(0xFF886B5F)),
                    start = Offset(0f, 0f),
                    end = Offset(0f, 3000f)
                )
            )
    ) {
        Column {
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = { navHostController.popBackStack() },
                    modifier = Modifier.size(50.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.whiteback),
                        contentDescription = "백버튼",
                        modifier = Modifier.size(50.dp)
                    )
                }
                Spacer(modifier = Modifier.width(110.dp))
                Text(
                    text = "나의 일기",
                    fontFamily = FontFamily(Font(R.font.nanumbarunpenb)),
                    color = Color.White
                )

            }
            Spacer(modifier = Modifier.height(30.dp))
// 다이어리 목록 표시
            if (isLoading.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    items(diaryListState.value) { diary ->
                        DiaryBox(
                            navController = navHostController,
                            userId = userId,
                            diaryInfo = diary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }


        AppBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp),
            navHostController = navHostController,
            option = 2
        )
    }
}
