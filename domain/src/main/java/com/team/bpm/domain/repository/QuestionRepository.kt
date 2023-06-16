package com.team.bpm.domain.repository

import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.model.CommentList
import com.team.bpm.domain.model.Question
import com.team.bpm.domain.model.QuestionList
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    suspend fun fetchQuestionList(limit: Int, offset: Int, slug: String?): Flow<QuestionList>

    suspend fun sendQuestion(
        title: String,
        content: String,
        imageByteArrays: List<ByteArray>
    ): Flow<Question>

    suspend fun deleteQuestion(questionId: Int): Flow<Unit>

    suspend fun fetchQuestionDetail(questionId: Int): Flow<Question>

    suspend fun sendQuestionReport(
        questionId: Int,
        reason: String
    ): Flow<Unit>

    suspend fun sendQuestionLike(questionId: Int): Flow<Unit>

    suspend fun deleteQuestionLike(questionId: Int): Flow<Unit>

    suspend fun sendQuestionComment(
        questionId: Int,
        parentId: Int?,
        comment: String
    ): Flow<Comment>

    suspend fun deleteQuestionComment(
        questionId: Int,
        commentId: Int
    ): Flow<Unit>

    suspend fun fetchQuestionCommentList(questionId: Int): Flow<CommentList>

    suspend fun sendQuestionCommentReport(
        questionId: Int,
        commentId: Int,
        reason: String
    ): Flow<Unit>

    suspend fun sendQuestionCommentLike(
        questionId: Int,
        commentId: Int
    ): Flow<Unit>

    suspend fun deleteQuestionCommentLike(
        questionId: Int,
        commentId: Int
    ): Flow<Unit>

    /* 마이페이지 내가 작성한 질문 */

    suspend fun fetchMyQuestionList(page: Int, size: Int): Flow<QuestionList>
}