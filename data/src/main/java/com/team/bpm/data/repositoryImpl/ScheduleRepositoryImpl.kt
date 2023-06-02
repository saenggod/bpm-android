package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.model.request.ScheduleRequest
import com.team.bpm.data.model.response.UserScheduleResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.model.UserSchedule
import com.team.bpm.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(private val mainApi: MainApi) : ScheduleRepository {

    override suspend fun sendSchedule(
        studioName: String,
        date: String,
        time: String,
        memo: String
    ): Flow<UserSchedule> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendSchedule(
                    ScheduleRequest(
                        studioName = studioName,
                        date = date,
                        time = time,
                        memo = memo
                    )
                )
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun fetchSchedule(): Flow<UserSchedule> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchSchedule()
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }
}