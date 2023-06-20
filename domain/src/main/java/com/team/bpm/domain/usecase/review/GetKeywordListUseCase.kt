package com.team.bpm.domain.usecase.review

import com.team.bpm.domain.model.KeywordList
import com.team.bpm.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetKeywordListUseCase @Inject constructor(private val reviewRepository: ReviewRepository) {
    suspend operator fun invoke(): Flow<KeywordList> {
        return reviewRepository.fetchKeywordList()
    }
}