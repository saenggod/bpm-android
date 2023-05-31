package com.team.bpm.data.model.request

import com.google.gson.annotations.SerializedName

data class ReportRequest(
    @SerializedName("reason")
    val reason: String
)
