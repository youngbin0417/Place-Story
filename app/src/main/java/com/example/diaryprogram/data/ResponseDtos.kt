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
    val userId: Long,
    val diaryId: Long,
    val name: String,
    val diaryTitle: String,
    val date: String,
    val profileImage: ImageResponseDto?,
    val latitude: Double,
    val longitude: Double,
    val isLiked: Boolean
)

data class ImageResponseDto(
    val imageId: Long,
    val url: String
)

data class DiaryDetailsResponseDto(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val content: String,
    val date: String,
    val likesCount: Int,
    val diaryStatus: DiaryStatus,
    val images: List<ImageResponseDto>
)

data class UserDiaryResponseDto(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val content: String,
    val date: String,
    val likesCount: Int,
    val diaryStatus: DiaryStatus,
    val images: List<ImageResponseDto>
)

data class FollowListResponseDto(
    val userIds: Long,
    val followNames: String,
    val profileImage: ImageResponseDto?
)

data class PaginatedResponseDto<T>(
    val content: List<T>,
    val currentPage: Int,
    val totalPages: Int,
    val totalItems: Long
)
