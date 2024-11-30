package com.example.diaryprogram.api

import com.example.diaryprogram.data.DiaryRequestDto
import com.example.diaryprogram.data.DiaryStatus
import com.example.diaryprogram.data.DiaryStatusAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
object ApiClient {
    private const val BASE_URL = "http://35.216.42.166:8081/api/"

    // HttpLoggingInterceptor 설정
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // 요청 및 응답을 BODY로 출력
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}

object GsonProvider {
    val instance: Gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(DiaryStatus::class.java, DiaryStatusAdapter())
            .create()
    }
}

fun DiaryRequestDto.toJson(): String {
    return GsonProvider.instance.toJson(this)
}
