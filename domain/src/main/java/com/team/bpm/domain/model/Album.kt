package com.team.bpm.domain.model

import com.team.bpm.domain.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Album(
    val id: Int? = 0,
    val albumName: String? = "",
    val date: String? = "",
    val memo: String? = "",
) : BaseModel