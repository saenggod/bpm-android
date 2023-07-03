package com.team.bpm.data.model.request

import com.google.gson.annotations.SerializedName

data class AlbumRequest(
    @SerializedName("scheduleName")
    val albumName: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("memo")
    val memo: String?
)
