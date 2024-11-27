package com.example.diaryprogram.api

import android.content.ContentResolver
import android.net.Uri
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
            // 이미지 파일을 MultipartBody.Part로 변환
            val imageParts = imageUris.mapIndexed { index, uri ->
                val fileName = "image_$index.jpg" // 서버에 업로드할 파일 이름
                val inputStream = contentResolver.openInputStream(uri)
                    ?: return onError("Unable to open InputStream for URI: $uri")
                val requestBody = inputStream.use { it.readBytes().toRequestBody("image/*".toMediaTypeOrNull()) }
                MultipartBody.Part.createFormData("images", fileName, requestBody)
            }

            // API 호출
            ApiClient.apiService.createDiary(userId, diaryRequestDto, imageParts).enqueue(object : Callback<ResponseDto> {
                override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                    if (response.isSuccessful) {
                        onSuccess(response.body())
                        println("Diary created successfully: ${response.body()?.statusMessage}")
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                        onError("Server error while creating diary: $errorMessage")
                        println("Error creating diary: $errorMessage")
                    }
                }

                override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                    val errorMessage = t.message ?: "Unknown network error"
                    onError("Network error: $errorMessage")
                    println("Network error: $errorMessage")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            onError("Unexpected error: ${e.message}")
        }
    }


}