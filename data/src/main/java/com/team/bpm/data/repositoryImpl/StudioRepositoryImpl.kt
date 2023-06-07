package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.model.response.StudioListResponse.Companion.toDataModel
import com.team.bpm.data.model.response.StudioResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.model.Studio
import com.team.bpm.domain.model.StudioList
import com.team.bpm.domain.repository.StudioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class StudioRepositoryImpl @Inject constructor(private val mainApi: MainApi) : StudioRepository {

    override suspend fun fetchStudioDetail(studioId: Int): Flow<Studio> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchStudioDetail(studioId)
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun sendStudioScrap(studioId: Int): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendScrap(studioId)
            }.collect {
                emit(Unit)
            }
        }
    }

    override suspend fun deleteStudioScrap(studioId: Int): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.deleteScrap(studioId)
            }.collect {
                emit(Unit)
            }
        }
    }

    override suspend fun fetchMyScrapList(): Flow<StudioList> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchMyScrapList()
            }.collect { result ->
                result.response?.let { emit(it.toDataModel()) }
            }
        }
    }
}