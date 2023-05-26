package com.team.bpm.domain.usecase.post

import com.team.bpm.domain.model.Post
import com.team.bpm.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostDetailUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postId: Int): Flow<Post> {
        return postRepository.fetchPostDetail(postId)
    }
}