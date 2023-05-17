package com.team.bpm.domain.usecase.review

import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.Review
import com.team.bpm.domain.model.ReviewList
import com.team.bpm.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReviewListUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(studioId: Int): Flow<ResponseState<ReviewList>> {
        return reviewRepository.fetchReviewList(studioId = studioId)
    }
}