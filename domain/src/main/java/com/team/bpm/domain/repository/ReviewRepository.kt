package com.team.bpm.domain.repository

import com.team.bpm.domain.model.Review
import com.team.bpm.domain.model.ReviewList
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {

    suspend fun fetchReviewList(studioId: Int): Flow<ReviewList>

    suspend fun fetchReviewDetail(
        studioId: Int,
        reviewId: Int
    ): Flow<Review>

    suspend fun likeReview(
        studioId: Int,
        reviewId: Int
    ): Flow<Unit>

    suspend fun dislikeReview(
        studioId: Int,
        reviewId: Int
    ): Flow<Unit>
}