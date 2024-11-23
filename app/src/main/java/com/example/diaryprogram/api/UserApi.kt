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
    fun signIn(context: Context, loginRequestDto: LoginRequestDto) {
        ApiClient.apiService.signIn(loginRequestDto).enqueue(object : Callback<ResponseDto> {
            override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                if (response.isSuccessful) {
                    Toast.makeText(context,"Login successful: ${response.body()?.statusMessage}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Error during login: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                println("Network error: ${t.message}")
            }
        })
    }

    // 사용자 프로필 가져오기
    fun fetchProfile(userId: Long) {
        ApiClient.apiService.getUserProfile(userId).enqueue(object : Callback<UserProfileResponseDto> {
            override fun onResponse(call: Call<UserProfileResponseDto>, response: Response<UserProfileResponseDto>) {
                if (response.isSuccessful) {
                    println("Profile fetched successfully: ${response.body()}")
                } else {
                    println("Error fetching profile: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<UserProfileResponseDto>, t: Throwable) {
                println("Network error: ${t.message}")
            }
        })
    }

    // 팔로잉 목록 가져오기
    fun fetchFollowingList(userId: Long, page: Int = 0, size: Int = 10) {
        ApiClient.apiService.getFollowingList(userId, page, size).enqueue(object : Callback<PaginatedResponseDto<FollowListResponseDto>> {
            override fun onResponse(call: Call<PaginatedResponseDto<FollowListResponseDto>>, response: Response<PaginatedResponseDto<FollowListResponseDto>>) {
                if (response.isSuccessful) {
                    println("Following list fetched successfully: ${response.body()?.content}")
                } else {
                    println("Error fetching following list: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PaginatedResponseDto<FollowListResponseDto>>, t: Throwable) {
                println("Network error: ${t.message}")
            }
        })
    }

    // 사용자 팔로우하기
    fun followUser(userId: Long, followingUserId: Long) {
        ApiClient.apiService.followUser(userId, followingUserId).enqueue(object : Callback<ResponseDto> {
            override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                if (response.isSuccessful) {
                    println("Followed user successfully: ${response.body()?.statusMessage}")
                } else {
                    println("Error following user: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                println("Network error: ${t.message}")
            }
        })
    }

    // 사용자 언팔로우하기
    fun unfollowUser(userId: Long, followingUserId: Long) {
        ApiClient.apiService.unfollowUser(userId, followingUserId).enqueue(object : Callback<ResponseDto> {
            override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                if (response.isSuccessful) {
                    println("Unfollowed user successfully: ${response.body()?.statusMessage}")
                } else {
                    println("Error unfollowing user: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                println("Network error: ${t.message}")
            }
        })
    }
}