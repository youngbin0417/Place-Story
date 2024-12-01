package com.example.diaryprogram.page

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.diaryprogram.R
import com.example.diaryprogram.api.ApiClient
import com.example.diaryprogram.appbar.AppBar
import com.example.diaryprogram.component.DiaryBox
import com.example.diaryprogram.data.DiaryResponseDto
import com.example.diaryprogram.data.DiaryStatus

@Composable
fun BrowseFollowDiaryPage(navHostController: NavHostController, userId: Long) {
    val diaryListState = remember { mutableStateOf<List<DiaryResponseDto>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }
    val totalPage = remember { mutableStateOf(1) } // 기본적으로 1페이지로 설정
    var currentPage by remember { mutableStateOf(0) }

    // 데이터 로드
    LaunchedEffect(currentPage) {
        try {
            isLoading.value = true
            val response = ApiClient.apiService.getAllDiaries(
                userId = userId,
                diaryStatus = DiaryStatus.FOLLOWER, // FOLLOWER 상태 전달
                page = currentPage,
                size = 10 // 원하는 페이지 크기 설정
            ).execute()

            if (response.isSuccessful) {
                val paginatedResponse = response.body()
                if (paginatedResponse != null) {
                    diaryListState.value = diaryListState.value + paginatedResponse.content
                    totalPage.value = paginatedResponse.totalPages
                } else {
                    println("Response body is null")
                }
            } else {
                println("Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            println("Exception: ${e.message}")
        } finally {
            isLoading.value = false
        }
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
    ){
        Column(modifier = Modifier.fillMaxSize()){

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                IconButton(
                    onClick = { navHostController.popBackStack() },
                    modifier = Modifier.size(50.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.whiteback),
                        contentDescription = "뒤로가기 버튼",
                        modifier = Modifier.size(50.dp)
                    )
                }
                Spacer(modifier = Modifier.width(70.dp))
                Text(
                    text = "팔로워 일기",
                    fontFamily = FontFamily(Font(R.font.nanumbarunpenb)),
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.height(30.dp))

            if (isLoading.value) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }else{
                if (diaryListState.value.isEmpty()) {
                    // 다이어리가 없을 때
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "표시할 다이어리가 없습니다.",
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.nanumbarunpenb)),
                            fontSize = 16.sp
                        )
                    }
                }
                else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                    ) {
                        items(diaryListState.value) { diary ->
                            DiaryBox(
                                navController = navHostController,
                                userId = userId,
                                diaryInfo = diary,
                                1,
                                onDiaryClick = { diaryId ->
                                    navHostController.navigate("mydiary/$diaryId")
                                }
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }

        }
        AppBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp),
            navHostController = navHostController,
            option = 4
        )
    }
}