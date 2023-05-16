package com.team.bpm.domain.model

import com.team.bpm.domain.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewList(
    val reviews: List<Review>?,
    val reviewCount: Int?,
) : BaseModel