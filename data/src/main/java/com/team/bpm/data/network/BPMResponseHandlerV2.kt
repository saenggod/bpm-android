package com.team.bpm.data.network

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.team.bpm.data.network.ErrorResponse.Companion.toDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class BPMResponseHandlerV2 {
    suspend fun <T> handle(call: suspend () -> Response<T>): Flow<T> {
        return flow {
            val response = call.invoke()
            response.body()?.let { result ->
                emit(result)
            } ?: Gson().run {
                val error = fromJson(response.errorBody()?.string(), JsonObject::class.java)["errors"].asJsonObject
                val errorRes = fromJson(error, ErrorResponse::class.java).toDataModel()
                throw java.lang.Exception(errorRes)
            }
        }
    }
}