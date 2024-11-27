package com.example.diaryprogram.api

import android.content.Context
import android.widget.Toast
import com.example.diaryprogram.data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 사용자 API
object UserApi {
    // 사용자 회원가입
    fun signUp(signUpRequestDto: SignUpRequestDto) {
        ApiClient.apiService.signUp(signUpRequestDto).enqueue(object : Callback<ResponseDto> {
            override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                if (response.isSuccessful) {
                    println("Sign-up successful: ${response.body()?.statusMessage}")
                } else {
                    println("Error during sign-up: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                println("Network error: ${t.message}")
            }
        })
    }

    // 사용자 로그인
    fun signIn(
        apiService: ApiService, // Retrofit API 인터페이스
        loginRequestDto: LoginRequestDto, // 로그인 요청 데이터
        onSuccess: (ResponseDto?) -> Unit, // 성공 시 콜백
        onError: (String) -> Unit // 실패 시 콜백
    ) {
        apiService.signIn(loginRequestDto).enqueue(object : Callback<ResponseDto> {
            override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                if (response.isSuccessful) {
                    onSuccess(response.body())
                    println("Sign-in successful: ${response.body()?.statusMessage}")
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    onError("Sign-in failed: $errorMessage")
                    println("Sign-in failed: $errorMessage")
                }
            }

            override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                val errorMessage = t.message ?: "Unknown network error"
                onError("Network error: $errorMessage")
                println("Network error: $errorMessage")
            }
        })
    }


}