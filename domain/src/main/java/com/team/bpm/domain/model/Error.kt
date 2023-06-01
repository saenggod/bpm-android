package com.team.bpm.domain.model

import com.team.bpm.domain.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Error(
    val status: Int,
    val error: String,
    val code: Int,
    override val message: String
) : BaseModel, Throwable()
