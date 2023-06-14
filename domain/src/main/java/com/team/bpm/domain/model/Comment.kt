package com.team.bpm.domain.model

import com.team.bpm.domain.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
    val id: Int?,
    val author: User?,
    val body: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val parentId: Int?,
    val reportCount: Int?,
    val children: List<Comment>?,
    val liked: Boolean?,
    val likeCount: Int?,
    val reported: Boolean?
) : BaseModel
