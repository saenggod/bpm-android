package com.team.bpm.domain.usecase.search_studio

import com.team.bpm.domain.model.StudioList
import com.team.bpm.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchStudioUseCase @Inject constructor(private val searchRepository: SearchRepository) {
    suspend operator fun invoke(query: String): Flow<StudioList> {
        return searchRepository.fetchSearchedStudioList(query)
    }
}