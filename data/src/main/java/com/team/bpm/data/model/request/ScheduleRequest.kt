package com.team.bpm.data.model.request

import com.google.gson.annotations.SerializedName

data class ScheduleRequest(
    @SerializedName("scheduleName")
    val scheduleName: String,
    @SerializedName("studioName")
    val studioName: String?,
    @SerializedName("date")
    val date: String,
    @SerializedName("time")
    val time: String?,
    @SerializedName("memo")
    val memo: String?
)
