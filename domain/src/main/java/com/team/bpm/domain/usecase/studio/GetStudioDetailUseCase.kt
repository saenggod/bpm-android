package com.team.bpm.domain.usecase.studio

import com.team.bpm.domain.model.Studio
import com.team.bpm.domain.repository.StudioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStudioDetailUseCase @Inject constructor(private val studioRepository: StudioRepository) {
    suspend operator fun invoke(
        studioId: Int
    ): Flow<Studio> {
        return studioRepository.fetchStudioDetail(studioId)
    }
}