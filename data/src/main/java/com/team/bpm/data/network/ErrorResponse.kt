package com.team.bpm.data.network

import com.google.gson.annotations.SerializedName
import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.domain.model.NetworkError
import kotlinx.parcelize.Parcelize

@Parcelize
data class ErrorResponse(
    @SerializedName("timestamp")
    val timestamp: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("error")
    val error: String? = null,
    @SerializedName("code")
    val code: String? = null,
    override var message: String? = null
) : BaseResponse, Throwable() {
    companion object : DataMapper<ErrorResponse, NetworkError> {
        override fun ErrorResponse.toDataModel(): NetworkError {
            return NetworkError(
                error = error ?: "null",
                code = code ?: "null",
                message = message ?: "Unknown Error"
            )
        }
    }
}
