package com.team.bpm.domain.usecase.review

import com.team.bpm.domain.model.Review
import com.team.bpm.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReviewDetailUseCase @Inject constructor(private val reviewRepository: ReviewRepository) {
    suspend operator fun invoke(
        studioId: Int,
        reviewId: Int
    ): Flow<Review> {
        return reviewRepository.fetchReviewDetail(studioId, reviewId)
    }
}