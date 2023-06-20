package com.team.bpm.domain.repository

import com.team.bpm.domain.model.KeywordList
import com.team.bpm.domain.model.Review
import com.team.bpm.domain.model.ReviewList
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {
    suspend fun sendReview(
        studioId: Int,
        imageByteArrays: List<ByteArray>,
        rating: Double,
        recommends: List<Int>,
        content: String
    ): Flow<Review>

    suspend fun deleteReview(
        studioId: Int,
        reviewId: Int
    ): Flow<Unit>

    suspend fun fetchReviewList(studioId: Int): Flow<ReviewList>

    suspend fun fetchReviewDetail(
        studioId: Int,
        reviewId: Int
    ): Flow<Review>

    suspend fun sendReviewReport(
        studioId: Int,
        reviewId: Int,
        reason: String
    ): Flow<Unit>

    suspend fun sendReviewLike(
        studioId: Int,
        reviewId: Int
    ): Flow<Unit>

    suspend fun deleteReviewLike(
        studioId: Int,
        reviewId: Int
    ): Flow<Unit>

    suspend fun fetchKeywordList(): Flow<KeywordList>
}