package com.team.bpm.domain.usecase.studio

import com.team.bpm.domain.model.StudioList
import com.team.bpm.domain.repository.StudioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMyScrapListUseCase @Inject constructor(private val studioRepository: StudioRepository) {
    suspend operator fun invoke(): Flow<StudioList> {
        return studioRepository.fetchMyScrapList()
    }
}