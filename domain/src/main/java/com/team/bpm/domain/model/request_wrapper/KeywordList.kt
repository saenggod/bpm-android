package com.team.bpm.domain.model.request_wrapper

import com.team.bpm.domain.base.BaseModel
import com.team.bpm.domain.model.Keyword
import kotlinx.parcelize.Parcelize

@Parcelize
data class KeywordList(
    val keywords: List<Keyword>?
) : BaseModel