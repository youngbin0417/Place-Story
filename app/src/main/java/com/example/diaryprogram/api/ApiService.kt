package com.example.diaryprogram.api
import com.example.diaryprogram.data.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // 1. Create Diary
    @Multipart
    @POST("users/{userId}/diaries")
    fun createDiary(
        @Path("userId") userId: Long,
        @Part("diary") diary: RequestBody, // JSON 형태로 전송
        @Part images: List<MultipartBody.Part> // 이미지 파일
    ): Call<ResponseDto>

    // 2. Get User Diary // 유저 아이디의 일기 조회
    @GET("users/{userId}/diaries/{diaryId}")
    fun getUserDiary(
        @Path("userId") userId: Long,
        @Path("diaryId") diaryId: Long
    ): Call<UserDiaryResponseDto>


    // 3. Update Diary
    @Multipart
    @PATCH("users/{userId}/diaries/{diaryId}")
    fun updateDiary(
        @Path("userId") userId: Long,
        @Path("diaryId") diaryId: Long,
        @Part("addImages") addImages: List<MultipartBody.Part>?,
        @Part("removeImages") removeImageIds: List<Long>?
    ): Call<ResponseDto>

    // 4. Delete Diary
    @DELETE("users/{userId}/diaries/{diaryId}")
    fun deleteDiary(
        @Path("userId") userId: Long,
        @Path("diaryId") diaryId: Long
    ): Call<ResponseDto>

    // 5. Get All Diaries
    @GET("users/{userId}/diaries")
    fun getAllDiaries(
        @Path("userId") userId: Long,
        @Query("diaryStatus") diaryStatus: DiaryStatus,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<PaginatedResponseDto<DiaryResponseDto>>

    // 6. Get Public Diaries


    @GET("diaries")
    fun getPublicDiaries(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<PaginatedResponseDto<DiaryResponseDto>>

    // 7. Get Public Diary Details
    @GET("diaries/{diaryId}")
    fun getPublicDiaryDetails(
        @Path("diaryId") diaryId: Long
    ): Call<DiaryDetailsResponseDto>

    // 8. Sign Up
    @POST("sign-up")
    fun signUp(@Body signUpRequestDto: SignUpRequestDto): Call<ResponseDto>

    // 9. Sign In
    @POST("sign-in")
    fun signIn(@Body loginRequestDto: LoginRequestDto): Call<ResponseDto>

    // 10. Get User Profile
    @GET("users/{userId}/profile")
    fun getUserProfile(
        @Path("userId") userId: Long
    ): Call<UserProfileResponseDto>

    // 11. Get Following List
    @GET("users/{userId}/follows")
    fun getFollowingList(
        @Path("userId") userId: Long
    ): Call<List<FollowListResponseDto>>


    // 12. Follow User
    @POST("users/{userId}/follows/{followingUserId}")
    fun followUser(
        @Path("userId") userId: Long,
        @Path("followingUserId") followingUserId: Long
    ): Call<ResponseDto>

    // 13. Unfollow User
    @DELETE("users/{userId}/follows/{followingUserId}")
    fun unfollowUser(
        @Path("userId") userId: Long,
        @Path("followingUserId") followingUserId: Long
    ): Call<ResponseDto>

    // 14. Like Diary
    @POST("users/{userId}/diaries/{diaryId}")
    fun likeDiary(
        @Path("userId") userId: Long,
        @Path("diaryId") diaryId: Long
    ): Call<ResponseDto>
}
