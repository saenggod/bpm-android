package com.team.bpm.domain.repository

import com.team.bpm.domain.model.RegisterStudioWrapper
import kotlinx.coroutines.flow.Flow

interface RegisterStudioRepository {
    suspend fun sendStudio(registerStudioWrapper: RegisterStudioWrapper): Flow<Unit>
    suspend fun fetchAddressName(latitude: Double, longitude: Double): Flow<String?>
}