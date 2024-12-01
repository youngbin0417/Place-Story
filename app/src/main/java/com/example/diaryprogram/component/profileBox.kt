package com.example.diaryprogram.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.diaryprogram.R
import com.example.diaryprogram.api.UserApi.followUser
import com.example.diaryprogram.api.UserApi.unfollowUser
import com.example.diaryprogram.data.FollowListResponseDto

@Composable
fun profileBox(navController: NavHostController, user:Long, followinfo:FollowListResponseDto,
               isFollowing: Boolean, onFollowChange: (Boolean) -> Unit) {
    var isFollowing by rememberSaveable { mutableStateOf(true) }
    Box(modifier = Modifier
        .width(300.dp).height(80.dp)
        .clickable(onClick = {
            // 네비게이션 사용
            navController.navigate("other_profile_page/${followinfo.userIds}/${if (isFollowing) "true" else "false"}")
        })
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(colorResource(R.color.light_daisy))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (followinfo.profileImage != null) {
                Image(
                    painter = rememberImagePainter(data = followinfo.profileImage.url), // Use Coil to load URL images
                    contentDescription = "${followinfo.followNames}'s profile picture",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.profile), // Use Coil to load URL images
                    contentDescription = "${followinfo.followNames}'s profile picture",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                )
            }
            Text(
                text = "${followinfo.followNames}",
                fontFamily = FontFamily(Font(R.font.nanumbarunpenb)),
                fontSize = 16.sp
            )


            if (isFollowing) {
                Button(
                    onClick = { isFollowing = false
                        unfollowUser(user,followinfo.userIds)
                        onFollowChange(false)
                        },
                    modifier = Modifier.width(100.dp).height(45.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.dark_daisy)
                    )
                ) {
                    Text(
                        text = "팔로잉",
                        fontSize = 12.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.nanumbarunpenb))
                    )
                }
            } else {
                Button(
                    onClick = { isFollowing = true
                        followUser(user,followinfo.userIds)
                        onFollowChange(true)
                    },
                    modifier = Modifier.width(100.dp).height(45.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White // 버튼 배경색 설정
                    )
                ) {
                    Text(
                        text = "팔로우",
                        fontSize = 12.sp,
                        color = colorResource(R.color.dark_daisy),
                        fontFamily = FontFamily(Font(R.font.nanumbarunpenb))
                    )
                }
            }
        }
    }
}


