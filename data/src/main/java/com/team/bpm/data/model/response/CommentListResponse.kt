package com.team.bpm.data.model.response

import com.google.gson.annotations.SerializedName
import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.data.model.response.CommentResponse.Companion.toDataModel
import com.team.bpm.domain.model.CommentList
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommentListResponse(
    @SerializedName(value = "comments")
    val comments: List<CommentResponse>?,
    @SerializedName(value = "commentCount")
    val commentCount: Int?
) : BaseResponse {
    companion object : DataMapper<CommentListResponse, CommentList> {
        override fun CommentListResponse.toDataModel(): CommentList {
            return CommentList(
                comments = comments?.map { it.toDataModel() } ?: emptyList(),
                commentCount = commentCount
            )
        }
    }
}
