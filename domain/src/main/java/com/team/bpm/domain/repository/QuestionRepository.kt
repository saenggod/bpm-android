package com.team.bpm.domain.repository

import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.model.CommentList
import com.team.bpm.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    suspend fun sendQuestion(title: String, content: String, imageByteArrays: List<ByteArray>): Flow<Question>
    suspend fun fetchQuestionDetail(questionId: Int): Flow<Question>
    suspend fun fetchQuestionCommentList(questionId: Int): Flow<CommentList>
    suspend fun sendQuestionComment(questionId: Int, parentId: Int?, comment: String): Flow<Comment>
    suspend fun sendQuestionLike(questionId: Int): Flow<Unit>
    suspend fun deleteQuestionLike(questionId: Int): Flow<Unit>
    suspend fun sendQuestionCommentLike(questionId: Int, commentId: Int): Flow<Unit>
    suspend fun deleteQuestionCommentLike(questionId: Int, commentId: Int): Flow<Unit>
    suspend fun deleteQuestion(questionId: Int): Flow<Unit>
    suspend fun reportQuestion(questionId: Int, reason: String): Flow<Unit>
}