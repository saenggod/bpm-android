package com.team.bpm.data.network

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.team.bpm.data.network.ErrorResponse.Companion.toDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class BpmResponseHandlerV2 {
    suspend fun <T> handle(call: suspend () -> Response<T>): Flow<T> {
        return flow {
            val response = call.invoke()
            response.body()?.let { result ->
                emit(result)
            } ?: run {
                val error = Gson().fromJson(response.errorBody()?.string(), JsonObject::class.java)["errors"].asJsonObject
                val errorRes = Gson().fromJson(error, ErrorResponse::class.java).toDataModel()
                throw java.lang.Exception(errorRes)
            }
        }
    }
}