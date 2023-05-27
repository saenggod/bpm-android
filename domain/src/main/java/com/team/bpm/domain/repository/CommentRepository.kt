package com.team.bpm.domain.repository

import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.model.CommentList
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    suspend fun fetchCommentList(questionId: Int): Flow<CommentList>
    suspend fun sendComment(questionId: Int, parentId: Int?, comment: String): Flow<Comment>
}