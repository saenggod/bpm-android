package com.team.bpm.domain.usecase.community

import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WriteCommunityCommentUseCase @Inject constructor(private val communityRepository: CommunityRepository) {
    suspend operator fun invoke(
        communityId: Int,
        comment: String
    ): Flow<Comment> {
        return communityRepository.sendCommunityComment(communityId, comment)
    }
}