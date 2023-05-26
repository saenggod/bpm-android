package com.team.bpm.domain.repository

import com.team.bpm.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun fetchPostDetail(postId: Int): Flow<Post>
}