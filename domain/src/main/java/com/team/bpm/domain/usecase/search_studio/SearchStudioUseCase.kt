package com.team.bpm.domain.usecase.search_studio

import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.StudioList
import com.team.bpm.domain.repository.SearchStudioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchStudioUseCase @Inject constructor(
    private val searchStudioRepository: SearchStudioRepository
) {
    suspend operator fun invoke(
        query: String
    ): Flow<ResponseState<StudioList>> {
        return searchStudioRepository.fetchSearchStudioResult(query = query)
    }
}