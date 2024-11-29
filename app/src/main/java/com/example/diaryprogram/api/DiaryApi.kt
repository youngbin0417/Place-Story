package com.example.diaryprogram.api

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import com.example.diaryprogram.data.*
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
        try {
            val validImageUris = imageUris.filter { uri ->
                try {
                    contentResolver.openInputStream(uri)?.close()
                    true
                } catch (e: Exception) {
                    println("Invalid URI: $uri")
                    false
                }
            }

            if (validImageUris.isEmpty()) {
                onError("No valid images selected for upload.")
                return
            }

            // 이미지 파일을 MultipartBody.Part로 변환
            val imageParts = validImageUris.mapIndexed { index, uri ->
                // 파일 이름 추출
                val fileName = contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor.moveToFirst()
                    cursor.getString(nameIndex)
                } ?: "image_$index.jpg" // 기본 이름 설정

                // InputStream 열기
                val inputStream = contentResolver.openInputStream(uri)
                    ?: return onError("Unable to open InputStream for URI: $uri")

                // RequestBody 생성
                val requestBody = inputStream.use { it.readBytes().toRequestBody("image/*".toMediaTypeOrNull()) }

                // MultipartBody.Part 생성
                MultipartBody.Part.createFormData(
                    name = "images", // 서버에서 기대하는 파라미터 이름
                    filename = fileName, // 서버로 전송할 파일 이름
                    body = requestBody
                )
            }

            println("Request Debug Info:")
            println("User ID: $userId")
            println("Diary Request DTO: $diaryRequestDto")
            println("Image URIs: $imageUris")
            println("Image Parts: $imageParts")


            ApiClient.apiService.createDiary(userId, diaryRequestDto, imageParts).enqueue(object : Callback<ResponseDto> {
                override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                    if (response.isSuccessful) {
                        onSuccess(response.body())
                        println("Diary created successfully!")
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                        // 응답 디버깅 정보 출력
                        println("Server returned error:")
                        println("HTTP Code: ${response.code()}")
                        println("Error Body: $errorMessage")
                        println("Headers: ${response.headers()}")
                        println("Request URL: ${call.request().url}")
                    }
                }

                override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                    val errorMessage = t.message ?: "Unknown network error"
                    onError("Network error: $errorMessage")

                    println("Network error while creating diary:")
                    println("Error Message: $errorMessage")
                    println("Request URL: ${call.request().url}")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            onError("Unexpected error: ${e.message}")

            println("Exception occurred while creating diary:")
            println("Message: ${e.message}")
            println("Stack Trace: ${e.stackTrace.joinToString("\n")}")
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