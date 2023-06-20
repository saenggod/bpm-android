package com.team.bpm.domain.usecase.search

import com.team.bpm.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetRecentSearchListUseCase @Inject constructor(private val searchRepository: SearchRepository) {
    suspend operator fun invoke(search: String): Flow<String?> {
        return searchRepository.saveRecentSearchList(search)
    }
}