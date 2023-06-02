package com.team.bpm.domain.usecase.review

import com.team.bpm.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteReviewUseCase @Inject constructor(private val reviewRepository: ReviewRepository) {
    suspend operator fun invoke(
        studioId: Int,
        reviewId: Int
    ): Flow<Unit> {
        return reviewRepository.deleteReview(studioId, reviewId)
    }
}