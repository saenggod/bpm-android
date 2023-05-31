package com.team.bpm.domain.usecase.community

import com.team.bpm.domain.model.Community
import com.team.bpm.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WriteCommunityUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(content: String, imageByteArrays: List<ByteArray>): Flow<Community> {
        return communityRepository.sendCommunity(content, imageByteArrays)
    }
}