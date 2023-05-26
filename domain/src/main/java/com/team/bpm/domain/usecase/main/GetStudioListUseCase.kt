package com.team.bpm.domain.usecase.main

import com.team.bpm.domain.model.StudioList
import com.team.bpm.domain.repository.MainRepository
import dagger.Reusable
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@Reusable
class GetStudioListUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    suspend operator fun invoke(limit: Int, offset: Int): Flow<StudioList> {
        return mainRepository.getStudioList(limit, offset)
    }
}