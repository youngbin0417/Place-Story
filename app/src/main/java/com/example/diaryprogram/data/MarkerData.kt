package com.example.diaryprogram.data

data class MarkerData(
    val id: Long,
    val name: String,
    val url: String,
    val type: String,
    val size: Long,
    val diaryInfo: DiariesResponseDto.DiaryInfo
)