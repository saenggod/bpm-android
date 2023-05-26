package com.team.bpm.domain.usecase.question

import com.team.bpm.domain.model.CommentList
import com.team.bpm.domain.repository.CommentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCommentListUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(questionId: Int): Flow<CommentList> {
        return commentRepository.fetchCommentList(questionId)
    }
}