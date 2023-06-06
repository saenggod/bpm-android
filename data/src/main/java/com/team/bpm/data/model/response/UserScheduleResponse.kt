package com.team.bpm.data.model.response

import com.google.gson.annotations.SerializedName
import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.domain.model.UserSchedule
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserScheduleResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("scheduleName")
    val scheduleName: String?,
    @SerializedName("studioName")
    val studioName: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("memo")
    val memo: String?,
) : BaseResponse {
    companion object : DataMapper<UserScheduleResponse, UserSchedule> {
        override fun UserScheduleResponse.toDataModel(): UserSchedule {
            return UserSchedule(
                id = id,
                scheduleName = scheduleName,
                studioName = studioName,
                date = date,
                time = time,
                memo = memo
            )
        }
    }
}