package com.team.bpm.domain.repository

import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.Review
import kotlinx.coroutines.flow.Flow
import java.io.File

interface WriteReviewRepository {
    suspend fun sendReview(
        studioId: Int,
        images: List<File>,
        rating: Double,
        recommends: List<String>,
        content: String
    ): Flow<ResponseState<Review>>
}