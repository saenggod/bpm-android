package com.team.bpm.domain.usecase.question

import com.team.bpm.domain.repository.CommentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SendCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(questionId: Int, parentId: Int?, comment: String): Flow<Unit> {
        return commentRepository.sendComment(questionId, parentId, comment)
    }
}