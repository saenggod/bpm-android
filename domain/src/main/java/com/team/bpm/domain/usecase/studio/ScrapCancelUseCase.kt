package com.team.bpm.domain.usecase.studio

import com.team.bpm.domain.repository.StudioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScrapCancelUseCase @Inject constructor(
    private val studioRepository: StudioRepository
) {
    suspend operator fun invoke(studioId: Int): Flow<Unit> {
        return studioRepository.sendStudioScrap(studioId)
    }
}