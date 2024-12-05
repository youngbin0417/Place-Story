package com.example.diaryprogram.page

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.diaryprogram.R
import com.example.diaryprogram.api.ApiClient.apiService
import com.example.diaryprogram.api.UserApi.signIn
import com.example.diaryprogram.data.LoginRequestDto
//프론트 완료
@Composable
fun LoginPage(navHostController: NavHostController) {
    val context = LocalContext.current // Context 가져오기
    var id by remember { mutableStateOf("") }
    var pw by remember { mutableStateOf("") }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF070301), Color(0xFF886B5F)), // 시작과 끝 색상
                        start = Offset(0f, 0f),   // 시작 지점
                        end = Offset(0f, 3000f) // 끝 지점
                    ) // 그라데이션 세팅
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp) // 두 TextField 간격 조정
            ) {
                Image(
                    painter = painterResource(id = R.drawable.appicon),
                    contentDescription = "앱 로고 화면",
                    modifier = Modifier.size(200.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .shadow(5.dp, RoundedCornerShape(15.dp), clip = true)
                        .background(colorResource(R.color.shadow)) // 갈색 그림자
                        .clip(RoundedCornerShape(15.dp))
                ) {
                    TextField(
                        value = id,
                        onValueChange = { id = it },
                        label = { Text("ID") },
                        modifier = Modifier.fillMaxWidth(), // TextField 자체는 full width
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .shadow(5.dp, RoundedCornerShape(15.dp), clip = true)
                        .background(colorResource(R.color.shadow)) // 갈색 그림자
                        .clip(RoundedCornerShape(15.dp))
                ) {
                    TextField(
                        value = pw,
                        onValueChange = { pw = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        )
                    )
                }

                Button(
                    onClick = {
                        val loginRequest = LoginRequestDto(username = id, password = pw)
                        signIn(
                            apiService = apiService,
                            loginRequestDto = loginRequest,
                            onSuccess = { response ->
                                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT)
                                    .show()
                            },
                            onError = { errorMessage ->
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        )
                        navHostController.navigate("main")
                    },
                        colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.daisy), // 버튼 배경색
                        contentColor = Color.White   // 버튼 텍스트 색
                    ),
                    modifier = Modifier.fillMaxWidth(0.3f) // 버튼 너비 조정
                ) {
                    Text("Log In")
                }
            }
        }

    }