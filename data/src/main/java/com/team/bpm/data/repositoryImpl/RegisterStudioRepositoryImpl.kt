package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.model.request.StudioRequest
import com.team.bpm.data.network.BPMResponse
import com.team.bpm.data.network.BPMResponseHandler
import com.team.bpm.data.network.ErrorResponse.Companion.toDataModel
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.model.RegisterStudioWrapper
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.repository.RegisterStudioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import okhttp3.ResponseBody
import javax.inject.Inject

class RegisterStudioRepositoryImpl @Inject constructor(
    private val mainApi: MainApi
) : RegisterStudioRepository {
    override suspend fun sendStudio(registerStudioWrapper: RegisterStudioWrapper): Flow<ResponseState<ResponseBody>> {
        return flow {
            BPMResponseHandler().handle {
                with(registerStudioWrapper) {
                    mainApi.sendStudio(
                        StudioRequest(
                            name = name,
                            address = address,
                            latitude = latitude,
                            longitude = longitude,
                            recommends = recommends,
                            sns = sns,
                            phone = phone,
                            openHours = openHours,
                            price = price
                        )
                    )
                }
            }.onEach { result ->
                when (result) {
                    is BPMResponse.Success -> emit(ResponseState.Success(result.data))
                    is BPMResponse.Error -> emit(ResponseState.Error(result.error.toDataModel()))
                }
            }.collect()
        }
    }
}