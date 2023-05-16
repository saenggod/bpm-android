package com.team.bpm.domain.repository

import com.team.bpm.domain.model.Post
import com.team.bpm.domain.model.ResponseState
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun fetchPost(postId: Int): Flow<ResponseState<Post>>
}