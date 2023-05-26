package com.team.bpm.data.model.request

import com.google.gson.annotations.SerializedName

data class CommentRequest(
    @SerializedName(value = "parentId")
    val parentId: Int? = null,
    @SerializedName(value = "body")
    val body: String
)