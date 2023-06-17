package com.team.bpm.domain.usecase.search

import com.team.bpm.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentSearchListUseCase @Inject constructor(private val searchRepository: SearchRepository) {
    operator fun invoke(): Flow<String?> {
        return searchRepository.getRecentSearchList()
    }
}