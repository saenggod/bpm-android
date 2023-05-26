package com.team.bpm.domain.repository

import kotlinx.coroutines.flow.Flow

interface WriteReviewRepository {
    suspend fun sendReview(
        studioId: Int,
        imageByteArrays: List<ByteArray>,
        rating: Double,
        recommends: List<String>,
        content: String
    ): Flow<Unit>
}