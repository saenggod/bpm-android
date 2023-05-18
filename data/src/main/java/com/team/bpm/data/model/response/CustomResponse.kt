package com.team.bpm.data.model.response

import com.team.bpm.data.network.ErrorResponse

data class CustomResponse<T>(
    val data: T?,
    val errors: ErrorResponse?
)
