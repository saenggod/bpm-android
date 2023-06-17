package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.datastore.DataStoreManager
import com.team.bpm.data.model.request.FilteredStudioListRequest
import com.team.bpm.data.model.response.StudioListResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.model.StudioList
import com.team.bpm.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
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

    override suspend fun getFilteredStudioList(
        keywordList: List<Int>,
        region: String
    ): Flow<StudioList> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchFilteredStudioList(FilteredStudioListRequest(keywordList.ifEmpty { null }, region))
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }
}