package com.team.bpm.data.network

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.team.bpm.data.model.response.ErrorResponse
import com.team.bpm.data.model.response.ErrorResponse.Companion.toDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class BPMResponseHandler {
    suspend fun <T> handle(call: suspend () -> Response<T>): Flow<BPMResponse<T>> {
        return flow {
            val response = call.invoke()
            if (response.isSuccessful && response.body() != null) {
                response.body()?.let { emit(BPMResponse.Success(it)) }
            } else {
            val gson = Gson()
                val error = gson.fromJson(response.errorBody()?.string(), JsonObject::class.java)["errors"].asJsonObject
                val errorRes = gson.fromJson(error, ErrorResponse::class.java).toDataModel()
                throw java.lang.Exception(errorRes)
            }
        }
    }
}