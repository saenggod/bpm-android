package com.team.bpm.domain.usecase.community

import com.team.bpm.domain.model.Community
import com.team.bpm.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCommunityDetailUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(postId: Int): Flow<Community> {
        return communityRepository.fetchCommunityDetail(postId)
    }
}