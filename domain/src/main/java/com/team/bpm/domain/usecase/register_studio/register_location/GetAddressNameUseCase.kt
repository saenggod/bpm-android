package com.team.bpm.domain.usecase.register_studio.register_location

import com.team.bpm.domain.repository.RegisterStudioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAddressNameUseCase @Inject constructor(private val registerStudioRepository: RegisterStudioRepository) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double
    ): Flow<String?> {
        return registerStudioRepository.fetchAddressName(latitude, longitude)
    }
}