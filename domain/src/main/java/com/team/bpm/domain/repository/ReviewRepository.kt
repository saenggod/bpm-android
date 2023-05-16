package com.team.bpm.domain.repository

import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.Review
import com.team.bpm.domain.model.ReviewList
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface ReviewRepository {

    suspend fun fetchReviewList(studioId: Int): Flow<ResponseState<ReviewList>>

    suspend fun fetchReviewDetail(
        studioId: Int,
        reviewId: Int
    ): Flow<ResponseState<Review>>

    suspend fun likeReview(
        studioId: Int,
        reviewId: Int
    ): Flow<ResponseState<ResponseBody>>

    suspend fun dislikeReview(
        studioId: Int,
        reviewId: Int
    ): Flow<ResponseState<ResponseBody>>
}