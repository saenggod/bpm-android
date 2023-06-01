package com.team.bpm.data.model.request

import com.google.gson.annotations.SerializedName

data class UserVerificationRequest(
    @SerializedName("kakaoId")
    val kakaoId: Long
)
