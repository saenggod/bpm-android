package com.team.bpm.data.network

import com.google.gson.Gson
import com.google.gson.JsonObject
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
                throw java.lang.Exception(Gson().fromJson(response.errorBody()?.string(), JsonObject::class.java)["errors"].asJsonObject["message"].asString ?: "알 수 없는 에러입니다.")
            }
        }
    }
}