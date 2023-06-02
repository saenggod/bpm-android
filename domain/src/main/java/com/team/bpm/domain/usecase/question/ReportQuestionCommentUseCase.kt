package com.team.bpm.domain.usecase.question

import com.team.bpm.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReportQuestionCommentUseCase @Inject constructor(private val questionRepository: QuestionRepository) {
    suspend operator fun invoke(
        questionId: Int,
        commentId: Int,
        reason: String
    ): Flow<Unit> {
        return questionRepository.sendQuestionCommentReport(questionId, commentId, reason)
    }
}