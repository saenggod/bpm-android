package com.team.bpm.domain.model

import com.team.bpm.domain.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserSchedule(
    val studioName: String? = "",
    val date: String? = "",
    val time: String? = "",
    val memo: String? = "끝까지 화이팅!",
) : BaseModel