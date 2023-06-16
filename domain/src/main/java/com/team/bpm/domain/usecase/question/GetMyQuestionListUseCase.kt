package com.team.bpm.domain.usecase.question

import com.team.bpm.domain.model.QuestionList
import com.team.bpm.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMyQuestionListUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    suspend operator fun invoke(page: Int, size: Int): Flow<QuestionList> {
        return questionRepository.fetchMyQuestionList(page, size)
    }
}