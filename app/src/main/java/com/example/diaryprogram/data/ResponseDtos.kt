package com.example.diaryprogram.data
import java.time.LocalDate

data class ResponseDto(
    val statusCode: String,
    val statusMessage: String
)

data class UserProfileResponseDto(
    val id: Long,
    val name: String,
    val premiumBadge: Boolean,
    val profileImage: Image?,
    val totalLikesCount: Int,
    val totalDiaryCount: Int,
    val totalFollowCount: Int
)

data class DiaryResponseDto(
    val diaryId: Long,
    val name: String,
    val diaryTitles: String,
    val profileImage: Image?,
    val diaryImages: List<Image>,
    val date: LocalDate,
    val latitude: Double,
    val longitude: Double
)

data class DiaryDetailsResponseDto(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val content: String,
    val date: LocalDate,
    val likesCount: Int,
    val diaryStatus: DiaryStatus,
    val images: List<Image>
)

data class UserDiaryResponseDto(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val content: String,
    val date: LocalDate,
    val likesCount: Int,
    val diaryStatus: DiaryStatus,
    val images: List<Image>
)

data class FollowListResponseDto(
    val userIds: Long,
    val followNames: String,
    val profileImage: Image?
)

data class PaginatedResponseDto<T>(
    val content: List<T>,
    val currentPage: Int,
    val totalPages: Int,
    val totalItems: Long
)
