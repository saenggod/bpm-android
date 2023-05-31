package com.team.bpm.domain.usecase.question

import com.team.bpm.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReportQuestionUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    suspend operator fun invoke(questionId: Int, reason: String): Flow<Unit> {
        return questionRepository.sendQuestionReport(questionId, reason)
    }
}