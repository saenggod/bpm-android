package com.team.bpm.domain.model

import com.team.bpm.domain.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Keyword(
    val id: Int?,
    val keyword: String?
) : BaseModel
