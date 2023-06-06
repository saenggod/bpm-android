package com.team.bpm.domain.model

import com.team.bpm.domain.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserSchedule(
    val id: Int? = 0,
    val scheduleName: String? = "",
    val studioName: String? = "",
    val date: String? = "",
    val time: String? = "",
    val memo: String? = "",
) : BaseModel