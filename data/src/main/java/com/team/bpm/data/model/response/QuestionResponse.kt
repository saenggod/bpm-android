package com.team.bpm.data.model.response

import com.google.gson.annotations.SerializedName
import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.data.model.response.UserResponse.Companion.toDataModel
import com.team.bpm.domain.model.Question
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("slug")
    val slug: String?,
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
    val liked: Boolean?,
    @SerializedName("favoriteCount")
    val likeCount: Int?,
    @SerializedName("commentsCount")
    val commentsCount: Int?,
    @SerializedName("reported")
    val reported: Boolean?,
    @SerializedName("reportCount")
    val reportCount: Int?
) : BaseResponse {
    companion object : DataMapper<QuestionResponse, Question> {
        override fun QuestionResponse.toDataModel(): Question {
            return Question(
                id = id,
                slug = slug,
                content = content?.replace("\\n", "\n"),
                filesPath = filesPath,
                author = author?.toDataModel(),
                createdAt = createdAt,
                updatedAt = updatedAt,
                liked = liked,
                likeCount = likeCount,
                commentsCount = commentsCount,
                reported = reported,
                reportCount = reportCount
            )
        }
    }
}