package com.team.bpm.domain.model

import com.team.bpm.domain.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Schedule(
    val studioName: String?,
    val date: String?,
    val time: String?,
    val memo: String?
) : BaseModel
