package com.team.bpm.data.network

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.team.bpm.data.model.response.CustomResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class BpmResponseHandlerV2 {
    suspend inline fun <reified T> handle(key: String?, crossinline call: suspend () -> Response<JsonObject>): Flow<CustomResponse<T>> {
        return flow {
            val response = call.invoke()
            if (response.isSuccessful) {
                if (key != null) {
                    val responseType = object : TypeToken<T>() {}.type
                    emit(CustomResponse(data = Gson().fromJson(response.body()?.get(key)?.asJsonObject, responseType), errors = null))
                } else {
                    emit(CustomResponse(data = null, errors = null))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val message = if (errorBody.isNullOrEmpty()) {
                    response.message()
                } else {
                    errorBody
                }
                emit(CustomResponse(data = null, ErrorResponse(code = response.code().toString(), message = message ?: "Unknown Error")))
            }
        }
    }
}