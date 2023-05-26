package com.team.bpm.domain.repository

import com.team.bpm.domain.model.RegisterStudioWrapper
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface RegisterStudioRepository {
    suspend fun sendStudio(registerStudioWrapper: RegisterStudioWrapper): Flow<ResponseBody>
    suspend fun fetchAddressName(latitude: Double, longitude: Double): Flow<String?>
}