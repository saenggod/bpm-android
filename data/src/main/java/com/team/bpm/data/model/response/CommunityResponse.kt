package com.team.bpm.data.model.response

import com.google.gson.annotations.SerializedName
import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.data.model.response.UserResponse.Companion.toDataModel
import com.team.bpm.domain.model.Community
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommunityResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("filesPath")
    val filesPath: List<String>?,
    @SerializedName("author")
    val author: UserResponse?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("favorite")
    val favorited: Boolean?,
    @SerializedName("favoriteCount")
    val favoriteCount: Int?
) : BaseResponse {
    companion object : DataMapper<CommunityResponse, Community> {
        override fun CommunityResponse.toDataModel(): Community {
            return Community(
                id = id,
                content = content,
                filesPath = filesPath,
                author = author?.toDataModel(),
                createdAt = createdAt,
                updatedAt = updatedAt,
                favorite = favorited,
                favoriteCount = favoriteCount
            )
        }
    }
}