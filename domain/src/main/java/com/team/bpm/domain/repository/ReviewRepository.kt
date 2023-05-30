package com.team.bpm.domain.repository

import com.team.bpm.domain.model.Review
import com.team.bpm.domain.model.ReviewList
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {
    suspend fun sendReview(
        studioId: Int,
        imageByteArrays: List<ByteArray>,
        rating: Double,
        recommends: List<String>,
        content: String
    ): Flow<Unit>

    suspend fun deleteReview(
        studioId: Int,
        reviewId: Int
    ): Flow<Unit>

    suspend fun fetchReviewList(studioId: Int): Flow<ReviewList>

    suspend fun fetchReviewDetail(
        studioId: Int,
        reviewId: Int
    ): Flow<Review>

    suspend fun sendReviewLike(
        studioId: Int,
        reviewId: Int
    ): Flow<Unit>

    suspend fun deleteReviewLike(
        studioId: Int,
        reviewId: Int
    ): Flow<Unit>
}