package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.model.response.StudioListResponse.Companion.toDataModel
import com.team.bpm.data.model.response.AlbumResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponse
import com.team.bpm.data.network.BPMResponseHandler
import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.model.response.ErrorResponse.Companion.toDataModel
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.StudioList
import com.team.bpm.domain.model.Album
import com.team.bpm.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val mainApi: MainApi) : HomeRepository {

    override suspend fun fetchStudioList(
        limit: Int,
        offset: Int,
        type: String
    ): Flow<StudioList> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchStudioList(limit, offset, type)
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun fetchAlbum(): Flow<ResponseState<Album>> {
        return flow {
            BPMResponseHandler().handle {
                mainApi.getAlbum()
            }.onEach { result ->
                when (result) {
                    is BPMResponse.Success -> emit(ResponseState.Success(result.data.toDataModel()))
                    is BPMResponse.Error -> emit(ResponseState.Error(result.error.toDataModel()))
                }
            }.collect()
        }
    }
}