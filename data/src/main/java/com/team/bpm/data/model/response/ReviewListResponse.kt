package com.team.bpm.data.model.response

import com.google.gson.annotations.SerializedName
import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.data.model.response.ReviewResponse.Companion.toDataModel
import com.team.bpm.domain.model.ReviewList
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewListResponse(
    @SerializedName("reviews")
    val reviews: List<ReviewResponse>?,
    @SerializedName("reviewCount")
    val reviewCount: Int?,
) : BaseResponse {
    companion object : DataMapper<ReviewListResponse, ReviewList> {
        override fun ReviewListResponse.toDataModel(): ReviewList {
            return ReviewList(
                reviews = reviews?.map { it.toDataModel() } ?: emptyList(),
                reviewCount = reviewCount
            )
        }
    }
}