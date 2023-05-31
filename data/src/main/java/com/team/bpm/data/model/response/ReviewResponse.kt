package com.team.bpm.data.model.response

import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.data.model.response.StudioResponse.Companion.toDataModel
import com.team.bpm.data.model.response.UserResponse.Companion.toDataModel
import com.team.bpm.domain.model.Review
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("studio")
    val studio: StudioResponse?,
    @SerializedName("author")
    val author: UserResponse?,
    @SerializedName("rating")
    val rating: Double?,
    @SerializedName("recommends")
    val recommends: List<String>?,
    @SerializedName("filesPath")
    val filesPath: List<String>?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("likeCount")
    val likeCount: Int?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("liked")
    val liked: Boolean?
) : BaseResponse {
    companion object : DataMapper<ReviewResponse, Review> {
        override fun ReviewResponse.toDataModel(): Review {
            return Review(
                id = id,
                studio = studio?.toDataModel(),
                author = author?.toDataModel(),
                rating = rating,
                recommends = recommends,
                filesPath = filesPath,
                content = content,
                likeCount = likeCount,
                createdAt = createdAt,
                updatedAt = updatedAt,
                liked = liked
            )
        }
    }
}