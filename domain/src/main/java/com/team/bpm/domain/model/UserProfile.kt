package com.team.bpm.domain.model

import com.team.bpm.domain.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProfile(
    val id: Long?,
    val nickname: String?,
    val bio: String?,
    val token: String?,
    val image: String?
) : BaseModel
