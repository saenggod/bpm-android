package com.team.bpm.domain.usecase.question

import com.team.bpm.domain.model.Question
import com.team.bpm.domain.model.ResponseStateV2
import com.team.bpm.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuestionDetailUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    suspend operator fun invoke(questionId: Int): Flow<ResponseStateV2<Question>> {
        return questionRepository.fetchQuestionDetail(questionId)
    }
}