package com.team.bpm.domain.usecase.review.like

import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import javax.inject.Inject

class LikeReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(studioId: Int, reviewId: Int): Flow<ResponseState<ResponseBody>> {
        return reviewRepository.likeReview(studioId, reviewId)
    }
}