package com.team.bpm.domain.usecase.search

import com.team.bpm.domain.model.StudioList
import com.team.bpm.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilteredStudioListUseCase @Inject constructor(private val searchRepository: SearchRepository) {
    suspend operator fun invoke(keywordList: List<Int>, region: String): Flow<StudioList> {
        return searchRepository.getFilteredStudioList(keywordList, region)
    }
}