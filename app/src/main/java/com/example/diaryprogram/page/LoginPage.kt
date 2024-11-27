package com.example.diaryprogram.page

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
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
    Column {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp) // 두 TextField 간격 조정
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "앱 로고 화면",
                    modifier = Modifier.size(200.dp)
                )

                TextField(
                    value = id,
                    onValueChange = { id = it },
                    label = { Text("ID") },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next // 키보드에서 "다음" 버튼을 표시
                    )// 너비 조정
                )

                TextField(
                    value = pw,
                    onValueChange = { pw = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done // 키보드에서 "다음" 버튼을 표시
                    )// 너비 조정
                )

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
}