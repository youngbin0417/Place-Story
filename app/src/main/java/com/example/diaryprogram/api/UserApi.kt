package com.example.diaryprogram.api

import android.content.Context
import android.util.Log
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
                    println("ID: ${loginRequestDto.username}")
                    println("Password: ${loginRequestDto.password}")
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

    fun loadUserProfile(
        apiService: ApiService,
        userId: Long,
        callback: (UserProfileResponseDto?) -> Unit
    ) {
        apiService.getUserProfile(userId).enqueue(object : Callback<UserProfileResponseDto> {
            override fun onResponse(
                call: Call<UserProfileResponseDto>,
                response: Response<UserProfileResponseDto>
            ) {
                if (response.isSuccessful) {
                    val userProfile = response.body()
                    if (userProfile != null) {
                        println("User profile loaded successfully: $userProfile")
                        callback(userProfile)
                    } else {
                        println("Empty response body detected.")
                        callback(null)
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    println("Error loading user profile: $errorBody")
                    Log.e("API_ERROR", "Error: ${response.code()}, ${response.errorBody()?.string()}")

                    callback(null)
                }
            }

            override fun onFailure(call: Call<UserProfileResponseDto>, t: Throwable) {
                println("Failed to load user profile: ${t.message}")
                Log.e("API_ERROR", "Request failed: ${t.message}")
                callback(null)
            }
        })
    }

    fun loadFollowList(
        apiService: ApiService,
        userId: Long,
        callback: (List<FollowListResponseDto>) -> Unit
    ) {
        apiService.getFollowingList(userId).enqueue(object : Callback<List<FollowListResponseDto>> {
            override fun onResponse(
                call: Call<List<FollowListResponseDto>>,
                response: Response<List<FollowListResponseDto>>
            ) {
                if (response.isSuccessful) {
                    val followList = response.body() ?: emptyList()
                    println("Following list loaded successfully: $followList")
                    callback(followList)
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    println("Error loading following list: $errorMessage")
                    callback(emptyList())
                }
            }

            override fun onFailure(call: Call<List<FollowListResponseDto>>, t: Throwable) {
                println("Failed to load following list: ${t.message}")
                callback(emptyList())
            }
        })
    }

    fun followUser(userId: Long, followingUserId: Long) {
        val call = ApiClient.apiService.followUser(userId, followingUserId)
        call.enqueue(object : Callback<ResponseDto> {
            override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                if (response.isSuccessful) {
                    println("Follow successful: ${response.body()?.statusMessage}")
                } else {
                    println("Follow failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                println("Follow error: ${t.message}")
            }
        })
    }

    fun unfollowUser(userId: Long, followingUserId: Long) {
        val call = ApiClient.apiService.unfollowUser(userId, followingUserId)
        call.enqueue(object : Callback<ResponseDto> {
            override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                if (response.isSuccessful) {
                    println("Unfollow successful: ${response.body()?.statusMessage}")
                } else {
                    println("Unfollow failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                println("Unfollow error: ${t.message}")
            }
        })
    }






}