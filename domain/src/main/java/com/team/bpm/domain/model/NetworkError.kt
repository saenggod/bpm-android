package com.team.bpm.domain.model

import com.team.bpm.domain.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class NetworkError(
    val error: String = "",
    val code: String = "",
    val message: String = "Unknown ErrorOccurred."
) : BaseModel
