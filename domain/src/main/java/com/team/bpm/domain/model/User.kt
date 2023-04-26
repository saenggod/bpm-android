package com.team.bpm.domain.model

import com.team.bpm.domain.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize

data class User(
    val id: Int?,
    val nickname: String?,
    val profilePath: String?
) : BaseModel