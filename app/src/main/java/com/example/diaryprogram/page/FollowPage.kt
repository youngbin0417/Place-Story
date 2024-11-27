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
import com.example.diaryprogram.appbar.AppBar
import com.example.diaryprogram.component.profileBox
import com.example.diaryprogram.data.FollowListResponseDto

//api 연동 해야함
@Composable
fun FollowPage(navHostController: NavHostController) {
    var followlist by remember { mutableStateOf<FollowListResponseDto?>(null) }

    // 데이터 로드
    LaunchedEffect(Unit) {
        followlist = loadFollowList() // 데이터를 비동기로 로드
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
                Spacer(modifier = Modifier.width(110.dp))

                Text(
                    text = "팔로잉",
                    color = Color.White,
                    fontSize = 25.sp,
                    fontFamily = FontFamily(Font(R.font.nanumbarunpenb))
                )
            }
            Spacer(modifier = Modifier.height(30.dp))

            // LazyColumn으로 팔로우 리스트 표시
            if (followlist == null) {
                LoadingPage()
            } else if (followlist!!.followInfos.isEmpty()) {
                // 리스트가 비어있을 때
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "아직 팔로우한 친구가 없어요.\n다른 사람의 공개 일기를 보러 가볼까요?",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.nanumbarunpenb))
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(followlist!!.followInfos) { followInfo ->
                        profileBox(navHostController,followInfo)
                    }
                }
            }
        }

        // AppBar 아래 정렬
        AppBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp),
            navHostController = navHostController,
            option = 4
        )
    }
}

// 예제 데이터 로드 함수
suspend fun loadFollowList(): FollowListResponseDto {
    // 서버 또는 로컬에서 데이터를 가져오는 비동기 로직 구현
    return FollowListResponseDto(
        followInfos = listOf(
            FollowListResponseDto.FollowInfo(
                userIds = 1L,
                followNames = "홍길동",
                profileImage = null,
                isFollowing = true
            ),
            FollowListResponseDto.FollowInfo(
                userIds = 2L,
                followNames = "김철수",
                profileImage = null,
                isFollowing = false
            )
        )
    )
}
