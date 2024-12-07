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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.diaryprogram.R
import com.example.diaryprogram.api.ApiClient.apiService
import com.example.diaryprogram.api.DiaryApi.fetchAllDiaries
import com.example.diaryprogram.api.DiaryApi.likeDiary
import com.example.diaryprogram.appbar.AppBar
import com.example.diaryprogram.component.DiaryBox
import com.example.diaryprogram.data.DiaryResponseDto

@Composable
fun BrowseFollowDiaryPage(navHostController: NavHostController, userId: Long) {
    val diaryListState = remember { mutableStateOf<List<DiaryResponseDto>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }
    val totalPage = remember { mutableStateOf(0) }
    var currentPage by remember { mutableStateOf(0) }
    val isLikedMap = remember { mutableStateMapOf<Long, Boolean>() }
    diaryListState.value.forEach { diary ->
        if (isLikedMap[diary.diaryId] == null) {
            isLikedMap[diary.diaryId] = diary.isLiked
        }
    }

    // 다이어리 데이터 로드
    fun loadPage(page: Int) {
        isLoading.value = true
        fetchAllDiaries(
            userId = userId,
            page = page,
            size = 5,
            onSuccess = { content, page, totalPages ->
                Log.d("ResponseCheck", "Content size: ${content.size}, ${totalPages}, ${page}")
                diaryListState.value = content
                currentPage = page
                totalPage.value = totalPages
                isLoading.value = false
            },
            onFailure = { error ->
                Log.e("BrowseMineDiaryPage", "Failed to fetch diaries: ${error.message}")
                isLoading.value = false
            }
        )
    }


    // 페이지 로드 초기화
    LaunchedEffect(key1 = userId) {
        loadPage(currentPage)
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
            Spacer(modifier = Modifier.height(20.dp))

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
            Spacer(modifier = Modifier.height(10.dp))

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
                                option = 0,
                                isClicked = isLikedMap[diary.diaryId] ?: false,
                                onDiaryClick = { diaryId, isClicked ->
                                    // isClicked 상태를 업데이트
                                    isLikedMap[diaryId] = isClicked
                                    // 네비게이션 시 isClicked 상태를 전달
                                    navHostController.navigate("followdiary/$diaryId/$isClicked")
                                },
                                onLikeToggle = { diaryId, newIsLiked ->
                                    // 다이어리의 좋아요 상태를 서버에 업데이트하고 Map을 업데이트
                                    likeDiary(
                                        apiService = apiService,
                                        userId = userId,
                                        diaryId = diaryId,
                                        onSuccess = {
                                            isLikedMap[diaryId] = newIsLiked
                                        },
                                        onFailure = { throwable ->

                                        }
                                    )
                                }
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 125.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                (0 until totalPage.value).forEach { page ->
                    Button(
                        onClick = {
                            if (page != currentPage) {
                                loadPage(page)
                            }
                        },
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(30.dp), // 크기 조정
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (page == currentPage) Color.White else colorResource(R.color.letter_daisy),
                            contentColor = if (page == currentPage) Color.Black else Color.White
                        )
                    ) {
                        Text(
                            text = "${page + 1}", // 버튼에 페이지 번호 표시
                            color = if (page == currentPage) Color.Black else Color.White,
                            fontSize = 12.sp
                        )
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

