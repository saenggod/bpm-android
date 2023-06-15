package com.team.bpm.data.model.response

import com.google.gson.annotations.SerializedName
import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.data.model.response.CommunityResponse.Companion.toDataModel
import com.team.bpm.domain.model.CommunityList
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommunityListResponse(
    @SerializedName("communities")
    val communities: List<CommunityResponse>?,
    @SerializedName("communityCount")
    val communityCount: Int?,
) : BaseResponse {
    companion object : DataMapper<CommunityListResponse, CommunityList> {
        override fun CommunityListResponse.toDataModel(): CommunityList {
            return CommunityList(
                communities = communities?.map { it.toDataModel() },
                communityCount = communityCount
            )
        }
    }
}