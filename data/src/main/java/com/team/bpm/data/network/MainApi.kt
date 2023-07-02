package com.team.bpm.data.network

import com.team.bpm.data.model.request.*
import com.team.bpm.data.model.response.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface MainApi {

    /* 계정 */

    @Headers("shouldBeAuthorized: false")
    @Multipart
    @POST("api/users/signup")
    suspend fun sendSignUp(
        @Part("kakaoId") kakaoId: Long,
        @Part("nickname") nickname: String,
        @Part("bio") bio: String,
        @Part file: MultipartBody.Part,
    ): Response<BPMResponseV2<UserProfileResponse>>

    @Headers("shouldBeAuthorized: false")
    @POST("api/users/verification")
    suspend fun sendKakaoIdVerification(
        @Body kakaoIdReq: UserVerificationRequest
    ): Response<BPMResponseV2<UserProfileResponse>>

    @GET("api/users")
    suspend fun fetchUserProfile(): Response<BPMResponseV2<UserProfileResponse>>

    @Multipart
    @PUT("api/profile")
    suspend fun sendEditedUserProfile(
        @Part("kakaoId") kakaoId: Long,
        @Part("nickname") nickname: String,
        @Part("bio") bio: String,
        @Part file: MultipartBody.Part,
    ): Response<BPMResponseV2<UserProfileResponse>>

    /* 스튜디오 */

    @GET("api/studio")
    suspend fun searchStudio(
        @Query("q") query: String
    ): Response<BPMResponseV2<StudioListResponse>>

    @POST("api/studio/filter")
    suspend fun fetchFilteredStudioList(
        @Body filteredStudioListRequest: FilteredStudioListRequest
    ): Response<BPMResponseV2<StudioListResponse>>

    @GET("api/studio")
    suspend fun fetchStudioList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("condition") type: String
    ): Response<BPMResponseV2<StudioListResponse>>

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

    @GET("api/users/scrap")
    suspend fun fetchMyScrapList(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = Int.MAX_VALUE,
        @Query("sort") sort: String = ""
    ): Response<BPMResponseV2<StudioListResponse>>

    /* 일정 */

    @GET("api/users/schedule")
    suspend fun fetchUserSchedule(): Response<BPMResponseV2<BodyShapeSchedulesResponse>>

    @GET("api/users/schedule/{scheduleId}")
    suspend fun fetchAlbumInfo(
        @Path("scheduleId") scheduleId: Int
    ): Response<BPMResponseV2<BodyShapeScheduleResponse>>

    @POST("api/users/schedule")
    suspend fun sendAlbum(
        @Body album: AlbumRequest
    ): Response<BPMResponseV2<AlbumResponse>>

    @PUT("api/users/schedule/{scheduleId}")
    suspend fun sendEditedAlbum(
        @Path("albumId") albumId: Int,
        @Body albumRequest: AlbumRequest
    ): Response<BPMResponseV2<AlbumResponse>>

    @GET("api/users/schedule/{scheduleId}")
    suspend fun fetchAlbum(
        @Path("albumId") albumId: Int
    ): Response<BPMResponseV2<AlbumResponse>>

    @GET("api/users/schedule")
    suspend fun getAlbum(): Response<AlbumResponse>

    /* 리뷰 */

    @Multipart
    @POST("api/studio/{studioId}/review")
    suspend fun sendReview(
        @Path("studioId") studioId: Int,
        @Part files: List<MultipartBody.Part>,
        @Part("rating") rating: Double,
        @Part("recommends") recommends: List<Int>,
        @Part("content") content: String
    ): Response<BPMResponseV2<ReviewResponse>>

    @DELETE("api/studio/{studioId}/review/{reviewId}")
    suspend fun deleteReview(
        @Path("studioId") studioId: Int,
        @Path("reviewId") reviewId: Int
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

    @POST("api/studio/{studioId}/review/{reviewId}/report")
    suspend fun sendReviewReport(
        @Path("studioId") studioId: Int,
        @Path("reviewId") reviewId: Int,
        @Body reportRequest: ReportRequest
    ): Response<BPMResponseV2<ResponseBody>>

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

    @GET("api/keywords")
    suspend fun fetchKeywordList(): Response<BPMResponseV2<KeywordListResponse>>

    /* 커뮤니티 */

    @GET("api/lounge/community")
    suspend fun fetchCommunityList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): Response<BPMResponseV2<CommunityListResponse>>

    @Multipart
    @POST("api/lounge/community")
    suspend fun sendCommunity(
        @Part("content") content: String,
        @Part files: List<MultipartBody.Part>,
    ): Response<BPMResponseV2<CommunityResponse>>

    @DELETE("api/lounge/community/{communityId}")
    suspend fun deleteCommunity(
        @Path("communityId") communityId: Int
    ): Response<BPMResponseV2<ResponseBody>>

    @GET("api/lounge/community/{communityId}")
    suspend fun fetchCommunityDetail(
        @Path("communityId") communityId: Int
    ): Response<BPMResponseV2<CommunityResponse>>

    @POST("api/lounge/community/{communityId}/report")
    suspend fun sendCommunityReport(
        @Path("communityId") communityId: Int,
        @Body reportRequest: ReportRequest
    ): Response<BPMResponseV2<ResponseBody>>

    @POST("api/lounge/community/{communityId}/favorite")
    suspend fun sendCommunityLike(
        @Path("communityId") communityId: Int,
    ): Response<BPMResponseV2<ResponseBody>>

    @DELETE("api/lounge/community/{communityId}/favorite")
    suspend fun deleteCommunityLike(
        @Path("communityId") communityId: Int,
    ): Response<BPMResponseV2<ResponseBody>>

    /*  커뮤니티 댓글 */

    @POST("api/lounge/community/{communityId}/comments")
    suspend fun sendCommunityComment(
        @Path("communityId") communityId: Int,
        @Body comment: CommentRequest
    ): Response<BPMResponseV2<CommentResponse>>

    @GET("api/lounge/community/{communityId}/comments")
    suspend fun fetchCommunityCommentList(
        @Path("communityId") communityId: Int
    ): Response<BPMResponseV2<CommentListResponse>>

    @DELETE("api/lounge/community/{communityId}/comments/{commentId}")
    suspend fun deleteCommunityComment(
        @Path("communityId") communityId: Int,
        @Path("commentId") commentId: Int
    ): Response<BPMResponseV2<ResponseBody>>

    @POST("api/lounge/community/{communityId}/comments/{commentId}/report")
    suspend fun sendCommunityCommentReport(
        @Path("communityId") communityId: Int,
        @Path("commentId") commentId: Int,
        @Body reportRequest: ReportRequest
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

    /* 질문 */

    @GET("api/lounge/question-board")
    suspend fun fetchQuestionList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("slug") slug: String?
    ): Response<BPMResponseV2<QuestionListResponse>>

    @Multipart
    @POST("api/lounge/question-board")
    suspend fun sendQuestion(
        @Part("title") title: String,
        @Part("content") content: String,
        @Part files: List<MultipartBody.Part>,
    ): Response<BPMResponseV2<QuestionResponse>>

    @DELETE("api/lounge/question-board/{questionBoardArticleId}")
    suspend fun deleteQuestion(
        @Path("questionBoardArticleId") questionId: Int
    ): Response<BPMResponseV2<ResponseBody>>

    @GET("api/lounge/question-board/{questionId}")
    suspend fun fetchQuestionDetail(
        @Path("questionId") questionId: Int
    ): Response<BPMResponseV2<QuestionResponse>>

    @POST("api/lounge/question-board/{questionBoardArticleId}/report")
    suspend fun sendQuestionReport(
        @Path("questionBoardArticleId") questionId: Int,
        @Body reportRequest: ReportRequest
    ): Response<BPMResponseV2<ResponseBody>>

    @POST("api/lounge/question-board/{questionBoardArticleId}/favorite")
    suspend fun sendQuestionLike(
        @Path("questionBoardArticleId") questionId: Int
    ): Response<BPMResponseV2<ResponseBody>>

    @DELETE("api/lounge/question-board/{questionBoardArticleId}/favorite")
    suspend fun deleteQuestionLike(
        @Path("questionBoardArticleId") questionId: Int
    ): Response<BPMResponseV2<ResponseBody>>

    /* 질문 댓글 */

    @POST("api/lounge/question-board/{questionBoardArticleId}/comments")
    suspend fun sendQuestionComment(
        @Path("questionBoardArticleId") questionId: Int,
        @Body comment: CommentRequest
    ): Response<BPMResponseV2<CommentResponse>>

    @DELETE("api/lounge/question-board/{questionBoardArticleId}/comments/{commentId}")
    suspend fun deleteQuestionComment(
        @Path("questionBoardArticleId") questionId: Int,
        @Path("commentId") commentId: Int
    ): Response<BPMResponseV2<ResponseBody>>

    @GET("api/lounge/question-board/{questionBoardArticleId}/comments")
    suspend fun fetchQuestionCommentList(
        @Path("questionBoardArticleId") questionId: Int
    ): Response<BPMResponseV2<CommentListResponse>>

    @POST("api/lounge/question-board/{questionBoardArticleId}/comments/{commentId}/report")
    suspend fun sendQuestionCommentReport(
        @Path("questionBoardArticleId") questionId: Int,
        @Path("commentId") commentId: Int,
        @Body reportRequest: ReportRequest
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

    /* 마이페이지 */

    @GET("api/users/question-board")
    suspend fun fetchMyQuestionList(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<BPMResponseV2<QuestionListResponse>>

    @GET("api/users/community")
    suspend fun fetchMyPostList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String = "createdDate",
    ): Response<BPMResponseV2<CommunityListResponse>>

    /* 눈바디 */

    @Multipart
    @POST("api/users/schedule/{scheduleId}/body-shape")
    suspend fun sendBodyShape(
        @Path("scheduleId") albumId: Int,
        @Part("content") content: String,
        @Part files: List<MultipartBody.Part>,
    ): Response<BPMResponseV2<BodyShapeResponse>>

    @Multipart
    @PUT("api/users/schedule/{scheduleId}/body-shape/{bodyShapeId}")
    suspend fun sendEditedBodyShape(
        @Path("scheduleId") albumId: Int,
        @Path("bodyShapeId") bodyShapeId: Int,
        @Part("content") content: String,
        @Part files: List<MultipartBody.Part>,
    ): Response<BPMResponseV2<BodyShapeResponse>>

    @DELETE("api/users/schedule/{scheduleId}/body-shape/{bodyShapeId}")
    suspend fun deleteBodyShape(
        @Path("scheduleId") albumId: Int,
        @Path("bodyShapeId") bodyShapeId: Int
    ): Response<BPMResponseV2<ResponseBody>>

    @GET("api/users/schedule/{scheduleId}/body-shape/{bodyShapeId}")
    suspend fun fetchBodyShape(
        @Path("scheduleId") albumId: Int,
        @Path("bodyShapeId") bodyShapeId: Int
    ): Response<BPMResponseV2<BodyShapeResponse>>

    /* 알림 */
    @GET("api/users/alarm")
    suspend fun fetchNotificationList(
        @Query("page") page: Int,
        @Query("size") size: Int = 30,
        @Query("sort") sort: String = "createdDate"
    ): Response<BPMResponseV2<NotificationListResponse>>

    @POST("api/users/alarm/{alarmId}")
    suspend fun setNotificationIsRead(
        @Path("alarmId") alarmId: Long
    ): Response<BPMResponseV2<ResponseBody>>

}
