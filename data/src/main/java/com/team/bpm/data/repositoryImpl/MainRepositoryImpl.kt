package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.model.response.StudioListResponse.Companion.toDataModel
import com.team.bpm.data.model.response.UserScheduleResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponse
import com.team.bpm.data.network.BPMResponseHandler
import com.team.bpm.data.network.ErrorResponse.Companion.toDataModel
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.StudioList
import com.team.bpm.domain.model.UserSchedule
import com.team.bpm.domain.repository.MainRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

class MainRepositoryImpl @Inject constructor(
    private val mainApi: MainApi
) : MainRepository {

    override suspend fun getStudioList(limit: Int, offset: Int): Flow<ResponseState<StudioList>> {
        return flow {
            BPMResponseHandler().handle {
                mainApi.getStudioList(limit, offset)
            }.onEach { result ->
                when (result) {
                    is BPMResponse.Success -> emit(ResponseState.Success(result.data.toDataModel()))
                    is BPMResponse.Error -> emit(ResponseState.Error(result.error.toDataModel()))
                }
            }.collect()
        }
    }

    override suspend fun getUserSchedule(): Flow<ResponseState<UserSchedule>> {
        return flow {
            BPMResponseHandler().handle {
                mainApi.getUserSchedule()
            }.onEach { result ->
                when (result) {
                    is BPMResponse.Success -> emit(ResponseState.Success(result.data.toDataModel()))
                    is BPMResponse.Error -> emit(ResponseState.Error(result.error.toDataModel()))
                }
            }.collect()
        }
    }
}