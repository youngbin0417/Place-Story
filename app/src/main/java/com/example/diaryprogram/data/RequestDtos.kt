package com.example.diaryprogram.data

import java.time.LocalDate

data class DiaryRequestDto(
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val content: String,
    val date: LocalDate,
    val enums: DiaryStatus?
)

data class LoginRequestDto(
    val username: String, // id
    val password: String
)

data class SignUpRequestDto(
    val username: String, // id
    val password: String,
    val name: String
)
