package com.team.bpm.data.model.request

import com.google.gson.annotations.SerializedName

data class CommentRequest(
    @SerializedName("parentId")
    val parentId: Int? = null,
    @SerializedName("body")
    val body: String
)