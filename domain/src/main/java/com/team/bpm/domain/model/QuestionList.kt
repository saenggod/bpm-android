package com.team.bpm.domain.model

import com.team.bpm.domain.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionList(
    val questionBoardList: List<Question>?,
    val questionBoardCount: Int?
) : BaseModel
