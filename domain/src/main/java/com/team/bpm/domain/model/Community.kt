package com.team.bpm.domain.model

import com.team.bpm.domain.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Community(
    val id: Int?,
    val content: String?,
    val filesPath: List<String>?,
    val author: User?,
    val createdAt: String?,
    val updatedAt: String?,
    val favorite: Boolean?,
    val favoriteCount: Int?
) : BaseModel
