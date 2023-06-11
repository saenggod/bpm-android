package com.team.bpm.domain.usecase.main

import com.team.bpm.domain.model.StudioList
import com.team.bpm.domain.repository.HomeRepository
import dagger.Reusable
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@Reusable
class GetStudioListUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    suspend operator fun invoke(
        limit: Int,
        offset: Int,
        type : String
    ): Flow<StudioList> {
        return homeRepository.fetchStudioList(limit, offset, type)
    }
}