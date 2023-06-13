package com.team.bpm.data.model.response

import com.google.gson.annotations.SerializedName
import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.model.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommentResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("author")
    val author: User?,
    @SerializedName("body")
    val body: String?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("parentId")
    val parentId: Int?,
    @SerializedName("reportCount")
    val reportCount: Int?,
    @SerializedName("children")
    val children: List<CommentResponse>?,
    @SerializedName("favorite")
    val liked: Boolean?,
    @SerializedName("favoriteCount")
    val likeCount: Int?,
    @SerializedName("reported")
    val reported: Boolean?
) : BaseResponse {
    companion object : DataMapper<CommentResponse, Comment> {
        override fun CommentResponse.toDataModel(): Comment {
            return Comment(
                id = id,
                author = author,
                body = body,
                createdAt = createdAt,
                updatedAt = updatedAt,
                parentId = parentId,
                reportCount = reportCount,
                children = children?.map { it.toDataModel() },
                liked = liked,
                likeCount = likeCount,
                reported = reported
            )
        }
    }
}