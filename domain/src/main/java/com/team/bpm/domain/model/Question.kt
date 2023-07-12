package com.team.bpm.domain.model

import com.team.bpm.domain.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(
    val id: Int?,
    val slug: String?,
    val content: String?,
    val filesPath: List<String>?,
    val author: User?,
    val createdAt: String?,
    val updatedAt: String?,
    val commentsCount: Int?,
    val liked: Boolean?,
    val likeCount: Int?,
    val reported: Boolean?,
    val reportCount: Int?,
    var isSelected: Boolean = false
) : BaseModel {
}
