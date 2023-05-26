package com.team.bpm.domain.model

sealed class ResponseStateV2<out T> {
    data class Success<T>(val data: T) : ResponseStateV2<T>()
    data class Error(val code: String?) : ResponseStateV2<Nothing>() // TODO : can be changed String to Int
}

