package com.team.bpm.data.model.response

import com.google.gson.annotations.SerializedName
import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.data.model.response.CommunityResponse.Companion.toDataModel
import com.team.bpm.domain.model.CommunityList
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommunityListResponse(
    @SerializedName("stories")
    val stories: List<CommunityResponse>?,
    @SerializedName("storyCount")
    val storyCount: Int?,
) : BaseResponse {
    companion object : DataMapper<CommunityListResponse, CommunityList> {
        override fun CommunityListResponse.toDataModel(): CommunityList {
            return CommunityList(
                stories = stories?.map { it.toDataModel() },
                storyCount = storyCount
            )
        }
    }
}