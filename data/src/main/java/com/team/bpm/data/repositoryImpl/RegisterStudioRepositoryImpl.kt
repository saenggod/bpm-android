package com.team.bpm.data.repositoryImpl

import android.location.Geocoder
import com.team.bpm.data.model.request.StudioRequest
import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.model.RegisterStudioWrapper
import com.team.bpm.domain.repository.RegisterStudioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterStudioRepositoryImpl @Inject constructor(
    private val mainApi: MainApi,
    private val geocoder: Geocoder
) : RegisterStudioRepository {
    override suspend fun sendStudio(registerStudioWrapper: RegisterStudioWrapper): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
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
            }.collect {
                emit(Unit)
            }
        }
    }

    override suspend fun fetchAddressName(latitude: Double, longitude: Double): Flow<String?> {
        return flow {
            geocoder.getFromLocation(latitude, longitude, 1)?.first()?.getAddressLine(0)?.drop(5)?.let { addressName ->
                emit(addressName)
            } ?: run {
                emit(null)
            }
        }
    }
}