package com.team.bpm.domain.repository

import com.team.bpm.domain.model.StudioList
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getRecentSearchList(): Flow<String?>

    suspend fun setRecentSearchList(search: String): Flow<String?>

    suspend fun getFilteredStudioList(
        keywordList: List<Int>,
        region: String
    ): Flow<StudioList>
}