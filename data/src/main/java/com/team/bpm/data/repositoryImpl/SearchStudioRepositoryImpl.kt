package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.model.response.StudioListResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.model.StudioList
import com.team.bpm.domain.repository.SearchStudioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class SearchStudioRepositoryImpl @Inject constructor(private val mainApi: MainApi) : SearchStudioRepository {
    override suspend fun fetchSearchedStudioList(query: String): Flow<StudioList> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.searchStudio(query = query)
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }
}