package com.team.bpm.domain.usecase.question

import com.team.bpm.domain.model.CommentList
import com.team.bpm.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuestionCommentListUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    suspend operator fun invoke(questionId: Int): Flow<CommentList> {
        return questionRepository.fetchQuestionCommentList(questionId)
    }
}