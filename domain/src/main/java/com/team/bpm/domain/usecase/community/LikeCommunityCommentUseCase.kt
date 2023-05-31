package com.team.bpm.domain.usecase.community

import com.team.bpm.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LikeCommunityCommentUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(communityId: Int, commentId: Int): Flow<Unit> {
        return communityRepository.sendCommunityCommentLike(communityId, commentId)
    }
}