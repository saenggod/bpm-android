package com.team.bpm.domain.usecase.studio_detail

import com.team.bpm.domain.model.Studio
import com.team.bpm.domain.repository.StudioDetailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StudioDetailUseCase @Inject constructor(
    private val studioDetailRepository: StudioDetailRepository
) {
    suspend operator fun invoke(
        studioId: Int
    ): Flow<Studio> {
        return studioDetailRepository.fetchStudioDetail(studioId = studioId)
    }
}