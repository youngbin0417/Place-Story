package com.example.diaryprogram.data

data class Image(
    val id: Long,
    val name: String,
    val url: String,
    val type: String,
    val size: Long,
    val imageType: ImageType
)
