package com.team.bpm.data.model.response

import com.google.gson.annotations.SerializedName
import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.data.model.response.UserResponse.Companion.toDataModel
import com.team.bpm.domain.model.Question
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionResponse(
    @SerializedName(value = "id")
    val id: Int?,
    @SerializedName(value = "slug")
    val slug: String?,
    @SerializedName(value = "content")
    val content: String?,
    @SerializedName(value = "filesPath")
    val filesPath: List<String>?,
    @SerializedName(value = "author")
    val author: UserResponse?,
    @SerializedName(value = "createdAt")
    val createdAt: String?,
    @SerializedName(value = "updatedAt")
    val updatedAt: String?,
    @SerializedName(value = "favorited")
    val favorited: Boolean?,
    @SerializedName(value = "favoritesCount")
    val favoritesCount: Int?,
    @SerializedName("commentsCount")
    val commentsCount: Int?
) : BaseResponse {
    companion object : DataMapper<QuestionResponse, Question> {
        override fun QuestionResponse.toDataModel(): Question {
            return Question(
                id = id,
                slug = slug,
                content = content,
                filesPath = filesPath,
                author = author?.toDataModel(),
                createdAt = createdAt,
                updatedAt = updatedAt,
                favorited = favorited,
                favoritesCount = favoritesCount,
                commentsCount = commentsCount
            )
        }
    }
}