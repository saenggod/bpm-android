package com.team.bpm.domain.usecase.question

import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SendQuestionCommentUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    suspend operator fun invoke(questionId: Int, parentId: Int?, comment: String): Flow<Comment> {
        return questionRepository.sendQuestionComment(questionId, parentId, comment)
    }
}