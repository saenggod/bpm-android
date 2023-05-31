package com.team.bpm.domain.usecase.review

import com.team.bpm.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReportReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(studioId: Int, reviewId: Int, reason: String): Flow<Unit> {
        return reviewRepository.sendReviewReport(studioId, reviewId, reason)
    }
}