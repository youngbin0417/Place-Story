package com.example.diaryprogram.Data

class UserInfo {
    data class SignUpInfo(
        val userId : String,
        val userName : String,
        val userDiary : List<Diary>,
        val followingDiary : List<Diary>
    )

    data class Diary(
        val title: String,
        val content: String,
        val location: String, // 위치 저장형식에따라 추후 수정
        val date: String
    )
}