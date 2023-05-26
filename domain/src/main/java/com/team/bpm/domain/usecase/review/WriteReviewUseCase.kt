package com.team.bpm.domain.usecase.review

import com.team.bpm.domain.repository.WriteReviewRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import javax.inject.Inject

class WriteReviewUseCase @Inject constructor(
    private val writeReviewRepository: WriteReviewRepository
) {
    suspend operator fun invoke(
        studioId: Int,
        imageByteArrays: List<ByteArray>,
        rating: Double,
        recommends: List<String>,
        content: String
    ): Flow<ResponseBody> {
        return writeReviewRepository.sendReview(
            studioId = studioId,
            imageByteArrays = imageByteArrays,
            rating = rating,
            recommends = recommends,
            content = content
        )
    }
}