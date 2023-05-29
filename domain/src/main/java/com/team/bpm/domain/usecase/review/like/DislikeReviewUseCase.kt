package com.team.bpm.domain.usecase.review.like

import com.team.bpm.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DislikeReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(studioId: Int, reviewId: Int): Flow<Unit> {
        return reviewRepository.deleteReviewLike(studioId, reviewId)
    }
}