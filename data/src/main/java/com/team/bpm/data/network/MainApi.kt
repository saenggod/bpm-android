package com.team.bpm.data.network

import com.team.bpm.data.model.request.ScheduleRequest
import com.team.bpm.data.model.request.StudioRequest
import com.team.bpm.data.model.request.UserVerificationRequest
import com.team.bpm.data.model.response.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface MainApi {
    @Headers("shouldBeAuthorized: false")
    @Multipart
    @POST("api/users/signup")
    suspend fun signUp(
        @Part("kakaoId") kakaoId: Long,
        @Part("nickname") nickname: String,
        @Part("bio") bio: String,
        @Part file: MultipartBody.Part,
    ): Response<SignUpResponse>

    @GET("api/users/schedule")
    suspend fun fetchSchedule(): Response<ScheduleResponse>

    @POST("api/users/schedule")
    suspend fun sendSchedule(
        @Body schedule: ScheduleRequest
    ): Response<ScheduleResponse>

    @Headers("shouldBeAuthorized: false")
    @POST("api/users/verification")
    suspend fun sendKakaoUserIdVerification(
        @Body kakaoUserIdReq: UserVerificationRequest
    ): Response<SignUpResponse>

    @GET("api/studio/{studioId}")
    suspend fun fetchStudioDetail(
        @Path("studioId") studioId: Int
    ): Response<StudioResponse>

    @GET("api/studio/{studioId}/review")
    suspend fun fetchReviewList(
        @Path("studioId") studioId: Int
    ): Response<ReviewListResponse>

    @GET("api/studio/{studioId}/review/{reviewId}")
    suspend fun fetchReviewDetail(
        @Path("studioId") studioId: Int,
        @Path("reviewId") reviewId: Int
    ): Response<ReviewResponse>

    @POST("api/studio/{studioId}/review/{reviewId}/like")
    suspend fun sendReviewLike(
        @Path("studioId") studioId: Int,
        @Path("reviewId") reviewId: Int
    ): Response<ResponseBody>

    @DELETE("api/studio/{studioId}/review/{reviewId}/like")
    suspend fun deleteReviewLike(
        @Path("studioId") studioId: Int,
        @Path("reviewId") reviewId: Int
    ): Response<ResponseBody>

    @Multipart
    @POST("api/studio/{studioId}/review")
    suspend fun sendReview(
        @Path("studioId") studioId: Int,
        @Part files: List<MultipartBody.Part>,
        @Part("rating") rating: Double,
        @Part("recommends") recommends: List<String>,
        @Part("content") content: String
    ): Response<ReviewResponse>

    @GET("api/studio/list")
    suspend fun getStudioList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): Response<StudioListResponse>

    @GET("api/users/schedule")
    suspend fun getUserSchedule(): Response<UserScheduleResponse>

    @GET("api/studio")
    suspend fun searchStudio(
        @Query("q") query: String
    ): Response<StudioListResponse>

    @POST("api/studio")
    suspend fun sendStudio(
        @Body studio: StudioRequest
    ): Response<ResponseBody>

    @GET("api/community/story/{storyId}")
    suspend fun fetchPostDetail(
        @Path("storyId") postId: Int
    ): Response<PostResponse>

    @GET("api/community/question-board/{questionId}")
    suspend fun fetchQuestionDetail(
        @Path("questionId") questionId: Int
    ): Response<QuestionResponse>
}