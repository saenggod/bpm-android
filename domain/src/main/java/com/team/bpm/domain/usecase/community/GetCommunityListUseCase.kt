package com.team.bpm.domain.usecase.community

import com.team.bpm.domain.model.CommunityList
import com.team.bpm.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCommunityListUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(page: Int, size: Int): Flow<CommunityList> {
        return communityRepository.fetchCommunityList(page, size)
    }
}