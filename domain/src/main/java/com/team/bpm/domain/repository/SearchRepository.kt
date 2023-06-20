package com.team.bpm.domain.repository

import com.team.bpm.domain.model.StudioList
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun fetchSearchedStudioList(query: String): Flow<StudioList>

    fun getRecentSearchList(): Flow<String?>

    suspend fun setRecentSearchList(search: String): Flow<String?>

    suspend fun getFilteredStudioList(
        keywordList: List<Int>,
        region: String
    ): Flow<StudioList>
}