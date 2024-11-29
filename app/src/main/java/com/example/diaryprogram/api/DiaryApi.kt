package com.example.diaryprogram.api

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import com.example.diaryprogram.data.*
import com.google.android.gms.common.internal.Objects
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 다이어리 API
object DiaryApi {


    fun createDiary(
        userId: Long,
        diaryRequestDto: DiaryRequestDto,
        imageUris: List<Uri>,
        contentResolver: ContentResolver,
        onSuccess: (ResponseDto?) -> Unit,
        onError: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 유효한 이미지 URI 필터링
                val validImageUris = imageUris.filter { uri ->
                    try {
                        contentResolver.openInputStream(uri)?.close()
                        true
                    } catch (e: Exception) {
                        println("Invalid URI: $uri - ${e.message}")
                        false
                    }
                }

                if (validImageUris.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        onError("No valid images selected for upload.")
                    }
                    return@launch
                }

                // 이미지 파일 준비
                val imageParts = validImageUris.mapIndexed { index, uri ->
                    val fileName = getFileNameFromUri(uri, contentResolver) ?: "image_$index.jpg"
                    val requestBody = contentResolver.openInputStream(uri)?.use {
                        it.readBytes().toRequestBody("image/*".toMediaTypeOrNull())
                    } ?: throw IllegalArgumentException("Unable to read image data for URI: $uri")
                    MultipartBody.Part.createFormData("images", fileName, requestBody)
                }

                // 네트워크 요청
                withContext(Dispatchers.Main) {
                    println("Request Debug Info:")
                    println("User ID: $userId")
                    println("Diary Request DTO: $diaryRequestDto")
                    println("Valid Image URIs: $validImageUris")
                    println("Image Parts: $imageParts")
                }

                ApiClient.apiService.createDiary(userId, diaryRequestDto, imageParts)
                    .enqueue(object : Callback<ResponseDto> {
                        override fun onResponse(
                            call: Call<ResponseDto>,
                            response: Response<ResponseDto>
                        ) {
                            if (response.isSuccessful) {
                                println("Diary created successfully!")
                                onSuccess(response.body())
                            } else {
                                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                                val statusCode = response.code()
                                val responseHeaders = response.headers()
                                val requestUrl = call.request().url
                                val requestMethod = call.request().method
                                println("HTTP Code: $statusCode")
                                println("Error Body: $errorBody")
                                println("Response Headers: $responseHeaders")
                                println("Request URL: $requestUrl")
                                println("Request Method: $requestMethod")
                            }
                        }

                        override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                            println("Network error while creating diary:")
                            println("Message: ${t.message}")
                            onError("Network error: ${t.message}")
                        }
                    })
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    println("Unexpected exception occurred while creating diary:")
                    println("Message: ${e.message}")
                    e.printStackTrace()
                    onError("Unexpected error: ${e.message}")
                }
            }
        }
    }



    // 파일 이름 추출 유틸 함수
    private fun getFileNameFromUri(uri: Uri, contentResolver: ContentResolver): String? {
        return contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1 && cursor.moveToFirst()) cursor.getString(nameIndex) else null
        }
    }


    fun likeDiary(
        apiService: ApiService,
        userId: Long,
        diaryId: Long,
    ) {
        apiService.likeDiary(userId, diaryId).enqueue(object : Callback<ResponseDto> {
            override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                if (response.isSuccessful) {
                    println("Diary liked successfully: ${response.body()}")
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    println("Failed to like diary: $errorMessage")
                }
            }

            override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                println("Error occurred while liking diary: ${t.message}")
            }
        })
    }

    fun fetchAllDiaries(
        userId: Long,
        diaryStatus: DiaryStatus,
        page: Int = 0,
        size: Int = 10,
        onSuccess: (PaginatedResponseDto<DiaryResponseDto>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val call = ApiClient.apiService.getAllDiaries(userId, diaryStatus, page, size)

        call.enqueue(object : Callback<PaginatedResponseDto<DiaryResponseDto>> {
            override fun onResponse(
                call: Call<PaginatedResponseDto<DiaryResponseDto>>,
                response: Response<PaginatedResponseDto<DiaryResponseDto>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        println("Successfully loaded diaries: ${it.content.size} entries")
                        onSuccess(it)
                    } ?: run {
                        println("Response was successful but body is null")
                        onFailure(Throwable("Response body is null"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    println("Failed to load diaries: ${response.code()} - ${response.message()}")
                    println("Error Body: $errorBody")
                    onFailure(Throwable("Error: ${response.code()} - ${response.message()}\n$errorBody"))
                }
            }

            override fun onFailure(call: Call<PaginatedResponseDto<DiaryResponseDto>>, t: Throwable) {
                println("Failed to make API call.")
                println("Request URL: ${call.request().url}")
                println("Request Headers: ${call.request().headers}")
                println("Error Message: ${t.message}")
                println("Error Cause: ${t.cause}")
                onFailure(t)
            }
        })
    }







}