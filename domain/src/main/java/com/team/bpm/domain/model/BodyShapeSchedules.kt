package com.team.bpm.domain.model

import com.team.bpm.domain.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class BodyShapeSchedules(
    val schedules: List<BodyShapeSchedule>?,
    val scheduleCount: Int?
) : BaseModel

@Parcelize
data class BodyShapeSchedule(
    val id: Int?,
    val scheduleName: String?,
    val studioName: String?,
    val date: String?,
    val time: String?,
    val memo: String?,
    val createAt: String?,
    val imagePath: String?,
    val isTodayPost: Boolean?,
    val bodyShapeList: BodyShapeList?,
    val dday: Int,
) : BaseModel

@Parcelize
data class BodyShapeList(
    val bodyShapeDetails: List<BodyShapeDetail>?,
    val bodyShapeListSize: Int?,
) : BaseModel

@Parcelize
data class BodyShapeDetail(
    val id: Int,
    val createAt: String?,
    val imagePath: String?,
    val dday: Int?,
) : BaseModel