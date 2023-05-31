package com.team.bpm.data.network

import com.team.bpm.data.model.request.CommentRequest
import com.team.bpm.data.model.request.ReportRequest
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
    ): Response<BPMResponseV2<SignUpResponse>>

    @GET("api/studio/{studioId}")
    suspend fun fetchStudioDetail(
        @Path("studioId") studioId: Int
    ): Response<BPMResponseV2<StudioResponse>>

    @POST("api/studio/{studioId}/scrap")
    suspend fun sendScrap(
        @Path("studioId") studioId: Int
    ): Response<BPMResponseV2<ResponseBody>>

    @DELETE("api/studio/{studioId}/scrap")
    suspend fun deleteScrap(
        @Path("studioId") studioId: Int
    ): Response<BPMResponseV2<ResponseBody>>

    @GET("api/studio/{studioId}/review")
    suspend fun fetchReviewList(
        @Path("studioId") studioId: Int
    ): Response<BPMResponseV2<ReviewListResponse>>

    @GET("api/studio/{studioId}/review/{reviewId}")
    suspend fun fetchReviewDetail(
        @Path("studioId") studioId: Int,
        @Path("reviewId") reviewId: Int
    ): Response<BPMResponseV2<ReviewResponse>>

    @POST("api/studio/{studioId}/review/{reviewId}/like")
    suspend fun sendReviewLike(
        @Path("studioId") studioId: Int,
        @Path("reviewId") reviewId: Int
    ): Response<BPMResponseV2<ResponseBody>>

    @DELETE("api/studio/{studioId}/review/{reviewId}/like")
    suspend fun deleteReviewLike(
        @Path("studioId") studioId: Int,
        @Path("reviewId") reviewId: Int
    ): Response<BPMResponseV2<ResponseBody>>

    @Multipart
    @POST("api/studio/{studioId}/review")
    suspend fun sendReview(
        @Path("studioId") studioId: Int,
        @Part files: List<MultipartBody.Part>,
        @Part("rating") rating: Double,
        @Part("recommends") recommends: List<String>,
        @Part("content") content: String
    ): Response<BPMResponseV2<ResponseBody>>

    @DELETE("api/studio/{studioId}/review/{reviewId}")
    suspend fun deleteReview(
        @Path("studioId") studioId: Int,
        @Path("reviewId") reviewId: Int
    ): Response<BPMResponseV2<ResponseBody>>

    @POST("api/studio/{studioId}/review/{reviewId}/report")
    suspend fun reportReview(
        @Path("studioId") studioId: Int,
        @Path("reviewId") reviewId: Int,
        @Body reportRequest: ReportRequest
    ): Response<BPMResponseV2<ResponseBody>>

    @GET("api/studio/list")
    suspend fun getStudioList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): Response<BPMResponseV2<StudioListResponse>>

    @GET("api/users/schedule")
    suspend fun getUserSchedule(): Response<UserScheduleResponse>

    @GET("api/studio")
    suspend fun searchStudio(
        @Query("q") query: String
    ): Response<StudioListResponse>

    @POST("api/studio")
    suspend fun sendStudio(
        @Body studio: StudioRequest
    ): Response<BPMResponseV2<ResponseBody>>

    @Multipart
    @POST("api/lounge/community")
    suspend fun sendCommunity(
        @Part("content") content: String,
        @Part files: List<MultipartBody.Part>,
    ): Response<BPMResponseV2<CommunityResponse>>

    @GET("api/lounge/community/{communityId}")
    suspend fun fetchCommunityDetail(
        @Path("communityId") communityId: Int
    ): Response<BPMResponseV2<CommunityResponse>>

    @GET("api/lounge/community/{communityId}/comments")
    suspend fun fetchCommunityComments(
        @Path("communityId") communityId: Int
    ): Response<BPMResponseV2<CommentListResponse>>

    @POST("api/lounge/community/{communityId}/comments")
    suspend fun sendCommunityComment(
        @Path("communityId") communityId: Int,
        @Body comment: CommentRequest
    ): Response<BPMResponseV2<CommentResponse>>

    @POST("api/lounge/community/{communityId}/favorite")
    suspend fun sendCommunityLike(
        @Path("communityId") communityId: Int,
    ): Response<BPMResponseV2<ResponseBody>>

    @DELETE("api/lounge/community/{communityId}/favorite")
    suspend fun deleteCommunityLike(
        @Path("communityId") communityId: Int,
    ): Response<BPMResponseV2<ResponseBody>>

    @POST("api/lounge/community/{communityId}/comments/{commentId}/favorite")
    suspend fun sendCommunityCommentLike(
        @Path("communityId") communityId: Int,
        @Path("commentId") commentId: Int
    ): Response<BPMResponseV2<ResponseBody>>

    @DELETE("api/lounge/community/{communityId}/comments/{commentId}/favorite")
    suspend fun deleteCommunityCommentLike(
        @Path("communityId") communityId: Int,
        @Path("commentId") commentId: Int
    ): Response<BPMResponseV2<ResponseBody>>

    @DELETE("api/lounge/community/{communityId}")
    suspend fun deleteCommunity(
        @Path("communityId") communityId: Int
    ): Response<BPMResponseV2<ResponseBody>>

    @POST("api/lounge/community/{communityId}/report")
    suspend fun reportCommunity(
        @Path("communityId") communityId: Int,
        @Body reportRequest: ReportRequest
    ): Response<BPMResponseV2<ResponseBody>>

    @Multipart
    @POST("api/lounge/question-board")
    suspend fun sendQuestion(
        @Part("title") title: String,
        @Part("content") content: String,
        @Part files: List<MultipartBody.Part>,
    ): Response<BPMResponseV2<QuestionResponse>>

    @GET("api/lounge/question-board/{questionId}")
    suspend fun fetchQuestionDetail(
        @Path("questionId") questionId: Int
    ): Response<BPMResponseV2<QuestionResponse>>

    @GET("api/lounge/question-board/{questionBoardArticleId}/comments")
    suspend fun fetchQuestionComments(
        @Path("questionBoardArticleId") questionId: Int
    ): Response<BPMResponseV2<CommentListResponse>>

    @POST("api/lounge/question-board/{questionBoardArticleId}/comments")
    suspend fun sendQuestionComment(
        @Path("questionBoardArticleId") questionId: Int,
        @Body comment: CommentRequest
    ): Response<BPMResponseV2<CommentResponse>>

    @POST("api/lounge/question-board/{questionBoardArticleId}/favorite")
    suspend fun sendQuestionLike(
        @Path("questionBoardArticleId") questionId: Int
    ): Response<BPMResponseV2<ResponseBody>>

    @DELETE("api/lounge/question-board/{questionBoardArticleId}/favorite")
    suspend fun deleteQuestionLike(
        @Path("questionBoardArticleId") questionId: Int
    ): Response<BPMResponseV2<ResponseBody>>

    @POST("api/lounge/question-board/{questionBoardArticleId}/comments/{commentId}/favorite")
    suspend fun sendQuestionCommentLike(
        @Path("questionBoardArticleId") questionId: Int,
        @Path("commentId") commentId: Int
    ): Response<BPMResponseV2<ResponseBody>>

    @DELETE("api/lounge/question-board/{questionBoardArticleId}/comments/{commentId}/favorite")
    suspend fun deleteQuestionCommentLike(
        @Path("questionBoardArticleId") questionId: Int,
        @Path("commentId") commentId: Int
    ): Response<BPMResponseV2<ResponseBody>>

    @DELETE("api/lounge/question-board/{questionBoardArticleId}")
    suspend fun deleteQuestion(
        @Path("questionBoardArticleId") questionId: Int
    ): Response<BPMResponseV2<ResponseBody>>

    @POST("api/lounge/question-board/{questionBoardArticleId}/report")
    suspend fun reportQuestion(
        @Path("questionBoardArticleId") questionId: Int,
        @Body reportRequest: ReportRequest
    ): Response<BPMResponseV2<ResponseBody>>

    @DELETE("api/lounge/question-board/{questionBoardArticleId}/comments/{commentId}")
    suspend fun deleteQuestionComment(
        @Path("questionBoardArticleId") questionId: Int,
        @Path("commentId") commentId: Int
    ): Response<BPMResponseV2<ResponseBody>>

    @Multipart
    @POST("api/community/body-shape")
    suspend fun sendEyeBody(
        @Part("content") content: String,
        @Part files: List<MultipartBody.Part>,
    ): Response<BPMResponseV2<EyeBodyResponse>>
}
