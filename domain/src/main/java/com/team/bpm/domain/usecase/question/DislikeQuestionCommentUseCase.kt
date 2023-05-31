package com.team.bpm.domain.usecase.question

import com.team.bpm.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DislikeQuestionCommentUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    suspend operator fun invoke(questionId: Int, commentId: Int): Flow<Unit> {
        return questionRepository.deleteQuestionCommentLike(questionId, commentId)
    }
}