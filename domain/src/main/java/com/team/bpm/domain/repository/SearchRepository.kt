package com.team.bpm.domain.repository

import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getRecentSearchList(): Flow<String?>

    suspend fun setRecentSearchList(search: String): Flow<String?>
}