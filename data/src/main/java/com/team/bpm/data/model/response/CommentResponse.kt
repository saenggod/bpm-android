package com.team.bpm.data.model.response

import com.google.gson.annotations.SerializedName
import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.model.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommentResponse(
    @SerializedName(value = "id")
    val id: Int?,
    @SerializedName(value = "author")
    val author: User?,
    @SerializedName(value = "body")
    val body: String?,
    @SerializedName(value = "createdAt")
    val createdAt: String?,
    @SerializedName(value = "updatedAt")
    val updatedAt: String?,
    @SerializedName(value = "parentId")
    val parentId: Int?,
    @SerializedName(value = "reportCount")
    val reportCount: Int?,
    @SerializedName(value = "children")
    val children: List<CommentResponse>?,
    @SerializedName(value = "favorited")
    val favorited: Boolean?,
    @SerializedName(value = "favoritesCount")
    val favoritesCount: Int?
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
                liked = favorited,
                likeCount = favoritesCount
            )
        }
    }
}