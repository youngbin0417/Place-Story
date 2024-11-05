package com.example.diaryprogram.api

import com.example.diaryprogram.data.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 다이어리 API
object DiaryApi {
    // 다이어리 생성
    fun createDiary(userId: Long, diaryRequestDto: DiaryRequestDto, images: List<MultipartBody.Part>) {
        ApiClient.apiService.createDiary(userId, diaryRequestDto, images).enqueue(object : Callback<ResponseDto> {
            override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                if (response.isSuccessful) {
                    println("Diary created successfully: ${response.body()?.statusMessage}")
                } else {
                    println("Error creating diary: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                println("Network error: ${t.message}")
            }
        })
    }

    // 특정 다이어리 가져오기
    fun fetchDiary(userId: Long, diaryId: Long) {
        ApiClient.apiService.getUserDiary(userId, diaryId).enqueue(object : Callback<UserDiaryResponseDto> {
            override fun onResponse(call: Call<UserDiaryResponseDto>, response: Response<UserDiaryResponseDto>) {
                if (response.isSuccessful) {
                    println("Diary fetched successfully: ${response.body()}")
                } else {
                    println("Error fetching diary: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<UserDiaryResponseDto>, t: Throwable) {
                println("Network error: ${t.message}")
            }
        })
    }

    // 다이어리 업데이트
    fun updateDiary(userId: Long, diaryId: Long, addImages: List<MultipartBody.Part>?, removeImageIds: List<Long>?) {
        ApiClient.apiService.updateDiary(userId, diaryId, addImages, removeImageIds).enqueue(object : Callback<ResponseDto> {
            override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                if (response.isSuccessful) {
                    println("Diary updated successfully: ${response.body()?.statusMessage}")
                } else {
                    println("Error updating diary: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                println("Network error: ${t.message}")
            }
        })
    }

    // 다이어리 삭제
    fun deleteDiary(userId: Long, diaryId: Long) {
        ApiClient.apiService.deleteDiary(userId, diaryId).enqueue(object : Callback<ResponseDto> {
            override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                if (response.isSuccessful) {
                    println("Diary deleted successfully: ${response.body()?.statusMessage}")
                } else {
                    println("Error deleting diary: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                println("Network error: ${t.message}")
            }
        })
    }

    // 사용자 다이어리 목록 가져오기
    fun fetchDiaries(userId: Long, diaryStatus: DiaryStatus, page: Int = 0, size: Int = 10) {
        ApiClient.apiService.getAllDiaries(userId, diaryStatus, page, size).enqueue(object : Callback<PaginatedResponseDto<DiariesResponseDto>> {
            override fun onResponse(call: Call<PaginatedResponseDto<DiariesResponseDto>>, response: Response<PaginatedResponseDto<DiariesResponseDto>>) {
                if (response.isSuccessful) {
                    println("Diaries fetched successfully: ${response.body()?.content}")
                } else {
                    println("Error fetching diaries: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PaginatedResponseDto<DiariesResponseDto>>, t: Throwable) {
                println("Network error: ${t.message}")
            }
        })
    }

    // 공개 다이어리 목록 가져오기
    fun fetchPublicDiaries(page: Int = 0, size: Int = 10) {
        ApiClient.apiService.getPublicDiaries(page, size).enqueue(object : Callback<PaginatedResponseDto<DiariesResponseDto>> {
            override fun onResponse(call: Call<PaginatedResponseDto<DiariesResponseDto>>, response: Response<PaginatedResponseDto<DiariesResponseDto>>) {
                if (response.isSuccessful) {
                    println("Public diaries fetched successfully: ${response.body()?.content}")
                } else {
                    println("Error fetching public diaries: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PaginatedResponseDto<DiariesResponseDto>>, t: Throwable) {
                println("Network error: ${t.message}")
            }
        })
    }

    // 공개 다이어리 세부정보 가져오기
    fun fetchPublicDiary(diaryId: Long) {
        ApiClient.apiService.getPublicDiaryDetails(diaryId).enqueue(object : Callback<DiaryDetailsResponseDto> {
            override fun onResponse(call: Call<DiaryDetailsResponseDto>, response: Response<DiaryDetailsResponseDto>) {
                if (response.isSuccessful) {
                    println("Public diary fetched successfully: ${response.body()}")
                } else {
                    println("Error fetching public diary: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<DiaryDetailsResponseDto>, t: Throwable) {
                println("Network error: ${t.message}")
            }
        })
    }

    // 다이어리 좋아요
    fun likeDiary(userId: Long, diaryId: Long) {
        ApiClient.apiService.likeDiary(userId, diaryId).enqueue(object : Callback<ResponseDto> {
            override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                if (response.isSuccessful) {
                    println("Diary liked successfully: ${response.body()?.statusMessage}")
                } else {
                    println("Error liking diary: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                println("Network error: ${t.message}")
            }
        })
    }
}