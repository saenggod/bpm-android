package com.team.bpm.domain.repository

import com.team.bpm.domain.model.ResponseState
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface RegisterStudioRepository {
    suspend fun sendStudio(
        name: String,
        address: String,
        latitude: Double,
        longitude: Double,
        recommends: List<String>,
        phone: String,
        sns: String,
        openHours: String,
        price: String
    ): Flow<ResponseState<ResponseBody>>
}