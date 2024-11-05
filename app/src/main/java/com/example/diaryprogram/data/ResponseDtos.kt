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
    val profileImage: Image?
)

data class DiariesResponseDto(
    val diaryInfos: List<DiaryInfo>
) {
    data class DiaryInfo(
        val diaryIds: Long,
        val diaryTitles: String
    )
}

data class DiaryDetailsResponseDto(
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
    val followInfos: List<FollowInfo>
) {
    data class FollowInfo(
        val userIds: Long,
        val followNames: String
    )
}

data class PaginatedResponseDto<T>(
    val content: List<T>,
    val currentPage: Int,
    val totalPages: Int,
    val totalItems: Long
)
