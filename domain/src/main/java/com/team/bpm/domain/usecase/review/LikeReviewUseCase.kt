package com.team.bpm.domain.usecase.review

import com.team.bpm.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LikeReviewUseCase @Inject constructor(private val reviewRepository: ReviewRepository) {
    suspend operator fun invoke(
        studioId: Int,
        reviewId: Int
    ): Flow<Unit> {
        return reviewRepository.sendReviewLike(studioId, reviewId)
    }
}