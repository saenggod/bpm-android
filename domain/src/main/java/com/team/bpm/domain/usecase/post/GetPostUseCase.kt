package com.team.bpm.domain.usecase.post

import com.team.bpm.domain.model.Post
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postId: Int): Flow<ResponseState<Post>> {
        return postRepository.fetchPost(postId)
    }
}