package com.team.bpm.domain.repository

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface WriteReviewRepository {
    suspend fun sendReview(
        studioId: Int,
        imageByteArrays: List<ByteArray>,
        rating: Double,
        recommends: List<String>,
        content: String
    ): Flow<ResponseBody>
}