package com.team.bpm.domain.repository

import com.team.bpm.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    suspend fun fetchQuestionDetail(questionId: Int): Flow<Question>
    suspend fun sendQuestionLike(questionId: Int): Flow<Unit>
    suspend fun deleteQuestionLike(questionId: Int): Flow<Unit>
    suspend fun sendQuestionCommentLike(questionId: Int, commentId: Int): Flow<Unit>
    suspend fun deleteQuestionCommentLike(questionId: Int, commentId: Int): Flow<Unit>
}