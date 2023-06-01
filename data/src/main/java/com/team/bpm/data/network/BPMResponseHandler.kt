package com.team.bpm.data.network

import com.team.bpm.data.model.response.ErrorResponse
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
                val errorBody = response.errorBody()?.string()
                val message = if (errorBody.isNullOrEmpty()) {
                    response.message()
                } else {
                    errorBody
                }
                emit(BPMResponse.Error(ErrorResponse(code = response.code().toString(), message = message ?: "Unknown Error")))
            }
        }
    }
}