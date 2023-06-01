package com.team.bpm.domain.model

sealed class ResponseState<out T> {
    data class Success<T>(val data: T) : ResponseState<T>()
    data class Error(val error: com.team.bpm.domain.model.Error) : ResponseState<Nothing>()
}
