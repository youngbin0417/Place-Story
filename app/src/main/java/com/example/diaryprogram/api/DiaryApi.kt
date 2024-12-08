package com.example.diaryprogram.api

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import com.example.diaryprogram.data.*
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
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
import java.time.LocalDate

// 다이어리 API
object DiaryApi {

    fun updateDiary(
        userId: Long,
        diaryId: Long,
        diaryRequestDto: DiaryRequestDto?,
        addImageUris: List<Uri>,
        removeImageIds: List<Long>?,
        contentResolver: ContentResolver,
        onSuccess: (ResponseDto?) -> Unit,
        onError: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val gson = GsonBuilder()
                    .registerTypeAdapter(LocalDate::class.java, object : TypeAdapter<LocalDate>() {
                        override fun write(out: JsonWriter, value: LocalDate) {
                            out.value(value.toString()) // LocalDate 직렬화
                        }

                        override fun read(input: JsonReader): LocalDate {
                            return LocalDate.parse(input.nextString()) // LocalDate 역직렬화
                        }
                    })
                    .registerTypeAdapter(DiaryStatus::class.java, DiaryStatusAdapter()) // DiaryStatus 직렬화 어댑터
                    .create()

                // DiaryRequestDto를 JSON으로 변환 (null 체크)
                val diaryRequestBody = diaryRequestDto?.let {
                    val diaryJson = gson.toJson(it)
                    diaryJson.toRequestBody("application/json".toMediaTypeOrNull())
                }

                // 추가 이미지 파일 준비
                val addImageParts = addImageUris.mapIndexed { index, uri ->
                    val fileName = getFileNameFromUri(uri, contentResolver)?.let { extractedName ->
                        val mimeType = contentResolver.getType(uri) ?: "image/jpeg"
                        val extension = when (mimeType) {
                            "image/jpeg" -> ".jpg"
                            "image/png" -> ".png"
                            "image/webp" -> ".webp"
                            else -> ".jpg"
                        }
                        if (extractedName.contains(".")) extractedName else "$extractedName$extension"
                    } ?: "image_$index.jpg"

                    val requestBody = contentResolver.openInputStream(uri)?.use {
                        it.readBytes().toRequestBody("image/*".toMediaTypeOrNull())
                    } ?: throw IllegalArgumentException("Unable to read image data for URI: $uri")

                    MultipartBody.Part.createFormData("addImages", fileName, requestBody)
                }

                // API 호출
                diaryRequestBody?.let {
                    ApiClient.apiService.updateDiary(
                        userId = userId,
                        diaryId = diaryId,
                        diary = it,
                        addImages = if (addImageParts.isNotEmpty()) addImageParts else null,
                        removeImageIds = removeImageIds
                    ).enqueue(object : Callback<ResponseDto> {
                        override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                            if (response.isSuccessful) {
                                onSuccess(response.body())
                            } else {
                                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                                onError("Error: $errorBody")
                            }
                        }

                        override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                            onError("Network error: ${t.message}")
                        }
                    })
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Unexpected error: ${e.message}")
                }
            }
        }
    }


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
                val gson = GsonBuilder()
                    .registerTypeAdapter(LocalDate::class.java, object : TypeAdapter<LocalDate>() {
                        override fun write(out: JsonWriter, value: LocalDate) {
                            out.value(value.toString()) // LocalDate 직렬화
                        }

                        override fun read(input: JsonReader): LocalDate {
                            return LocalDate.parse(input.nextString()) // LocalDate 역직렬화
                        }
                    })
                    .registerTypeAdapter(DiaryStatus::class.java, DiaryStatusAdapter()) // DiaryStatus 직렬화 어댑터
                    .create()

                // DiaryRequestDto를 JSON으로 변환
                val diaryJson = gson.toJson(diaryRequestDto)
                val diaryRequestBody = diaryJson.toRequestBody("application/json".toMediaTypeOrNull())

                // 이미지 파일 준비
                val imageParts = imageUris.mapIndexed { index, uri ->
                    val fileName = getFileNameFromUri(uri, contentResolver)?.let { extractedName ->
                        val mimeType = contentResolver.getType(uri) ?: "image/jpeg"
                        val extension = when (mimeType) {
                            "image/jpeg" -> ".jpg"
                            "image/png" -> ".png"
                            "image/webp" -> ".webp"
                            else -> ".jpg"
                        }
                        if (extractedName.contains(".")) extractedName else "$extractedName$extension"
                    } ?: "image_$index.jpg"

                    val requestBody = contentResolver.openInputStream(uri)?.use {
                        it.readBytes().toRequestBody("image/*".toMediaTypeOrNull())
                    } ?: throw IllegalArgumentException("Unable to read image data for URI: $uri")

                    MultipartBody.Part.createFormData("images", fileName, requestBody)
                }

                // API 호출
                ApiClient.apiService.createDiary(userId, diaryRequestBody, imageParts)
                    .enqueue(object : Callback<ResponseDto> {
                        override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                            if (response.isSuccessful) {
                                onSuccess(response.body())
                            } else {
                                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                                onError("Error: $errorBody")
                            }
                        }

                        override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                            onError("Network error: ${t.message}")
                        }
                    })
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Unexpected error: ${e.message}")
                }
            }
        }
    }


    class DiaryStatusAdapter : TypeAdapter<DiaryStatus>() {
        override fun write(out: JsonWriter, value: DiaryStatus) {
            out.value(value.name) // Enum의 name을 JSON에 기록
        }

        override fun read(input: JsonReader): DiaryStatus {
            return DiaryStatus.valueOf(input.nextString())
        }
    }

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
        onSuccess: () -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        apiService.likeDiary(userId, diaryId).enqueue(object : Callback<ResponseDto> {
            override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                if (response.isSuccessful) {
                    println("Diary liked successfully: ${response.body()}")
                    onSuccess() // 성공 시 콜백 호출
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    println("Failed to like diary: $errorMessage")
                    onFailure(Throwable(errorMessage)) // 실패 시 콜백 호출
                }
            }

            override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                println("Error occurred while liking diary: ${t.message}")
                onFailure(t) // 실패 시 콜백 호출
            }
        })
    }

    fun fetchMyDiaries(
        userId: Long,
        page: Int = 0,
        size: Int = 5,
        onSuccess: (List<DiaryResponseDto>, Int, Int) -> Unit, // 컨텐츠, 현재 페이지, 전체 페이지 전달
        onFailure: (Throwable) -> Unit
    ) {
        val call = ApiClient.apiService.getMyDiaries(userId, page, size)

        call.enqueue(object : Callback<PaginatedResponseDto<DiaryResponseDto>> {
            override fun onResponse(
                call: Call<PaginatedResponseDto<DiaryResponseDto>>,
                response: Response<PaginatedResponseDto<DiaryResponseDto>>
            ) {
                if (response.isSuccessful) {
                    val paginatedResponse = response.body()
                    if (paginatedResponse != null) {
                        val content = paginatedResponse.content
                        val currentPage = paginatedResponse.currentPage
                        val totalPages = paginatedResponse.totalPages
                        onSuccess(content, currentPage, totalPages)
                    } else {
                        onFailure(Throwable("Response body is null"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    onFailure(Throwable("HTTP Error: $errorBody"))
                }
            }

            override fun onFailure(call: Call<PaginatedResponseDto<DiaryResponseDto>>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    fun fetchAllDiaries(
        userId: Long,
        diaryStatus: DiaryStatus = DiaryStatus.FOLLOWER,
        page: Int = 0,
        size: Int = 5,
        onSuccess: (List<DiaryResponseDto>, Int, Int) -> Unit, // 컨텐츠, 현재 페이지, 전체 페이지 전달
        onFailure: (Throwable) -> Unit
    ) {
        val call = ApiClient.apiService.getAllDiaries(userId, diaryStatus, page, size)

        call.enqueue(object : Callback<PaginatedResponseDto<DiaryResponseDto>> {
            override fun onResponse(
                call: Call<PaginatedResponseDto<DiaryResponseDto>>,
                response: Response<PaginatedResponseDto<DiaryResponseDto>>
            ) {
                if (response.isSuccessful) {
                    val paginatedResponse = response.body()
                    if (paginatedResponse != null) {
                        val content = paginatedResponse.content
                        val currentPage = paginatedResponse.currentPage
                        val totalPages = paginatedResponse.totalPages
                        onSuccess(content, currentPage, totalPages)
                    } else {
                        onFailure(Throwable("Response body is null"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    onFailure(Throwable("HTTP Error: $errorBody"))
                }
            }

            override fun onFailure(call: Call<PaginatedResponseDto<DiaryResponseDto>>, t: Throwable) {
                onFailure(t)
            }
        })
    }



    fun deleteDiary(
        userId: Long,
        diaryId: Long,
        onSuccess: (ResponseDto) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val call = ApiClient.apiService.deleteDiary(userId, diaryId)

        call.enqueue(object : Callback<ResponseDto> {
            override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        println("Successfully deleted diary: ${it}")
                        onSuccess(it)
                    } ?: run {
                        println("Response was successful but body is null")
                        onFailure(Throwable("Response body is null"))
                    }
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
                    onFailure(Throwable("Failed with HTTP status: $statusCode"))
                }
            }

            override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                println("Failed to make API call.")
                println("Request URL: ${call.request().url}")
                println("Request Headers: ${call.request().headers}")
                println("Error Message: ${t.message}")
                println("Error Cause: ${t.cause}")
                onFailure(t)
            }
        })
    }


    fun fetchUserDiary(
        userId: Long,
        diaryId: Long,
        onSuccess: (UserDiaryResponseDto) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val call = ApiClient.apiService.getUserDiary(userId, diaryId)

        call.enqueue(object : Callback<UserDiaryResponseDto> {
            override fun onResponse(
                call: Call<UserDiaryResponseDto>,
                response: Response<UserDiaryResponseDto>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { diary ->
                        println("Diary fetched successfully: $diary")
                        onSuccess(diary)
                    } ?: run {
                        println("Response is successful but body is null")
                        onFailure(Throwable("Response body is null"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    val statusCode = response.code()
                    println("Error fetching diary - HTTP Code: $statusCode, Error: $errorBody")
                    onFailure(Throwable("Error: HTTP $statusCode"))
                }
            }

            override fun onFailure(call: Call<UserDiaryResponseDto>, t: Throwable) {
                println("API call failed: ${t.message}")
                onFailure(t)
            }
        })
    }

    fun fetchPublicDiaries(
        userId: Long,
        page: Int = 0,
        size: Int = 5,
        onSuccess: (List<DiaryResponseDto>, Int, Int) -> Unit, // 컨텐츠, 현재 페이지, 전체 페이지 전달
        onFailure: (Throwable) -> Unit
    ) {
        val call = ApiClient.apiService.getPublicDiaries(userId, page, size)

        call.enqueue(object : Callback<PaginatedResponseDto<DiaryResponseDto>> {
            override fun onResponse(
                call: Call<PaginatedResponseDto<DiaryResponseDto>>,
                response: Response<PaginatedResponseDto<DiaryResponseDto>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { paginatedResponse ->
                        onSuccess(paginatedResponse.content, paginatedResponse.currentPage, paginatedResponse.totalPages)
                    } ?: onFailure(Throwable("Response body is null"))
                } else {
                    onFailure(Throwable("HTTP Error: ${response.errorBody()?.string()}"))
                }
            }

            override fun onFailure(call: Call<PaginatedResponseDto<DiaryResponseDto>>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    fun loadPublicDiaryDetails(
        apiService: ApiService,
        diaryId: Long,
        onSuccess: (UserDiaryResponseDto) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getPublicDiaryDetails(diaryId).execute()
                if (response.isSuccessful && response.body() != null) {
                    withContext(Dispatchers.Main) {
                        onSuccess(response.body()!!)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onFailure(Exception("Failed to load diary details"))
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onFailure(e)
                }
            }
        }
    }





}