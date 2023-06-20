package com.team.bpm.data.model.request

import com.google.gson.annotations.SerializedName

data class ProfileRequest(
    @SerializedName("kakaoId")
    val kakaoId: Long,
    val nickname: String,
    val bio: String
)
