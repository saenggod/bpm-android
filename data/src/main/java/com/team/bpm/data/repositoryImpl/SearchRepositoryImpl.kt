package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.datastore.DataStoreManager
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val mainApi: MainApi
) : SearchRepository {
    override fun getRecentSearchList(): Flow<String?> {
        return dataStoreManager.getRecentSearchList()
    }

    override suspend fun setRecentSearchList(search: String): Flow<String?> {
        return dataStoreManager.setRecentSearchList(search)
    }
}