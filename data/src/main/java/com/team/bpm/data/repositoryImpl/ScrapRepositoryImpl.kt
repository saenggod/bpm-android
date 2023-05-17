package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.network.BPMResponse
import com.team.bpm.data.network.BPMResponseHandler
import com.team.bpm.data.network.ErrorResponse.Companion.toDataModel
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.repository.ScrapRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import okhttp3.ResponseBody
import javax.inject.Inject

class ScrapRepositoryImpl @Inject constructor(
    private val mainApi: MainApi
) : ScrapRepository {
    override suspend fun sendScrap(studioId: Int): Flow<ResponseState<ResponseBody>> {
        return flow {
            BPMResponseHandler().handle {
                mainApi.sendScrap(studioId)
            }.onEach { result ->
                when (result) {
                    is BPMResponse.Success -> emit(ResponseState.Success(result.data))
                    is BPMResponse.Error -> emit(ResponseState.Error(result.error.toDataModel()))
                }
            }.collect()
        }
    }

    override suspend fun deleteScrap(studioId: Int): Flow<ResponseState<ResponseBody>> {
        return flow {
            BPMResponseHandler().handle {
                mainApi.deleteScrap(studioId)
            }.onEach { result ->
                when (result) {
                    is BPMResponse.Success -> emit(ResponseState.Success(result.data))
                    is BPMResponse.Error -> emit(ResponseState.Error(result.error.toDataModel()))
                }
            }.collect()
        }
    }
}