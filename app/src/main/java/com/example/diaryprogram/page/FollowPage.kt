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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.diaryprogram.R
import com.example.diaryprogram.api.ApiClient.apiService
import com.example.diaryprogram.api.UserApi.loadFollowList
import com.example.diaryprogram.appbar.AppBar
import com.example.diaryprogram.component.profileBox
import com.example.diaryprogram.data.FollowListResponseDto
import com.google.ai.client.generativeai.type.content

//api 연동 해야함
@Composable
fun FollowPage(navHostController: NavHostController, userId: Long) {
    var followList by remember { mutableStateOf<List<FollowListResponseDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val followStates = remember { mutableStateMapOf<Long, Boolean>() }
    followList.forEach {
        followStates[it.userIds] = true // 초기값 설정 (팔로우 상태)
    }

    DisposableEffect(userId) {
        isLoading = true
        loadFollowList(apiService, userId) { loadedList ->
            followList = loadedList
            isLoading = false
        }
        onDispose { }
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
                    onClick = { navHostController.navigate("profile") },
                    modifier = Modifier.size(50.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.whiteback),
                        contentDescription = "백버튼",
                        modifier = Modifier.size(50.dp)
                    )
                }
                Spacer(modifier = Modifier.width(90.dp))

                Text(
                    text = "팔로잉",
                    color = Color.White,
                    fontSize = 25.sp,
                    fontFamily = FontFamily(Font(R.font.nanumbarunpenb))
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            when {
                isLoading -> {
                    // 로딩 상태 표시
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color.White
                        )
                    }
                }

                followList.isEmpty() -> {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {

                        // 리스트 비어 있을 때
                        Text(
                            text = "아직 팔로우한 친구가 없어요.\n다른 사람의 공개 일기를 보러 가볼까요?",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.nanumbarunpenb)),
                        )
                    }
                }

                else -> {
                    // 리스트가 있을 때 LazyColumn 표시
                    LazyColumn(
                        modifier = Modifier
                            .width(300.dp)
                            .align(Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(followList) { followInfo ->
                            val isFollowing = followStates[followInfo.userIds] ?: true
                            profileBox(
                                navHostController,
                                userId,
                                followInfo,
                                isFollowing=isFollowing,
                                onFollowChange = { newIsFollowing ->
                                    // 리스트를 업데이트
                                    followStates[followInfo.userIds] = newIsFollowing

                                }
                            )
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



