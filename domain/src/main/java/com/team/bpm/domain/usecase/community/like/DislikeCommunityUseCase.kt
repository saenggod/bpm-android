package com.team.bpm.domain.usecase.community.like

import com.team.bpm.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DislikeCommunityUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(communityId: Int): Flow<Unit> {
        return communityRepository.deleteCommunityLike(communityId)
    }
}