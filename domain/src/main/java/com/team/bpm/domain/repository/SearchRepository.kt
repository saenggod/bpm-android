package com.team.bpm.domain.repository

import com.team.bpm.domain.model.StudioList
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun fetchSearchedStudioList(query: String): Flow<StudioList>

    fun loadRecentSearchList(): Flow<String?>

    suspend fun saveRecentSearchList(search: String): Flow<String?>

    suspend fun fetchFilteredStudioList(
        keywordList: List<Int>,
        region: String
    ): Flow<StudioList>
}