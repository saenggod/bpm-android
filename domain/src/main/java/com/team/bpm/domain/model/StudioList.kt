package com.team.bpm.domain.model

import com.team.bpm.domain.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class StudioList(
    val studios: List<Studio>?,
    val studioCount: Int?,
) : BaseModel