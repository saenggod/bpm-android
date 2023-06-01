package com.team.bpm.domain.usecase.question

import com.team.bpm.domain.model.QuestionList
import com.team.bpm.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuestionListUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    suspend operator fun invoke(limit: Int, offset: Int, slug: String?): Flow<QuestionList> {
        return questionRepository.fetchQuestionList(limit, offset, slug)
    }
}