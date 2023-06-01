package com.team.bpm.data.network

import com.team.bpm.data.model.response.ErrorResponse

sealed class BPMResponse<out T> {
    data class Success<T>(val data: T) : BPMResponse<T>()
    data class Error(val error: ErrorResponse) : BPMResponse<Nothing>()
}
