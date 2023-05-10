package com.team.bpm.domain.usecase.register_studio

import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.repository.RegisterStudioRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import javax.inject.Inject

class RegisterStudioUseCase @Inject constructor(
    private val registerStudioRepository: RegisterStudioRepository
) {
    suspend operator fun invoke(
        name: String,
        address: String,
        latitude: Double,
        longitude: Double,
        recommends: List<String>,
        phone: String,
        sns: String,
        openHours: String,
        price: String
    ): Flow<ResponseState<ResponseBody>> {
        return registerStudioRepository.sendStudio(
            name = name,
            address = address,
            latitude = latitude,
            longitude = longitude,
            recommends = recommends,
            phone = phone,
            sns = sns,
            openHours = openHours,
            price = price
        )
    }
}