package com.team.bpm.domain.model

import com.team.bpm.domain.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class BodyShape(
    val id: Int?,
    val content: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val filesPath: List<String>?,
    val author: User?
) : BaseModel
