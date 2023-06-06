package com.team.bpm.domain.model

import com.team.bpm.domain.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommunityList(
    val stories: List<Community>?,
    val storyCount: Int?,
) : BaseModel
