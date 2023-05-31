package com.team.bpm.data.model.response

import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.domain.model.Schedule
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScheduleResponse(
    @SerializedName("studioName")
    val studioName: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("memo")
    val memo: String?
) : BaseResponse {

    companion object : DataMapper<ScheduleResponse, Schedule> {
        override fun ScheduleResponse.toDataModel(): Schedule {
            return Schedule(
                studioName = studioName,
                date = date,
                time = time,
                memo = memo
            )
        }
    }
}