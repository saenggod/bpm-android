package com.team.bpm.domain.model

import com.team.bpm.domain.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommentList(
    val comments: List<Comment>?,
    val commentCount: Int?
) : BaseModel
