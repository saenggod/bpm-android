package com.team.bpm.data.model.response

import com.google.gson.annotations.SerializedName
import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.data.model.response.UserResponse.Companion.toDataModel
import com.team.bpm.domain.model.Post
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostResponse(
    @SerializedName(value = "id")
    val id: Int?,
    @SerializedName(value = "content")
    val content: String,
    @SerializedName(value = "filesPath")
    val filesPath: List<String>?,
    @SerializedName(value = "author")
    val author: UserResponse?,
    @SerializedName(value = "createdAt")
    val createdAt: String?,
    @SerializedName(value = "updatedAt")
    val updatedAt: String?
) : BaseResponse {
    companion object : DataMapper<PostResponse, Post> {
        override fun PostResponse.toDataModel(): Post {
            return Post(
                id = id,
                content = content,
                filesPath = filesPath,
                author = author?.toDataModel(),
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}