package com.team.bpm.data.model.request

import com.google.gson.annotations.SerializedName

data class FilteredStudioListRequest(
    @SerializedName("keyword")
    val keywordList: List<Int>?,
    @SerializedName("region")
    val region: String?
)
