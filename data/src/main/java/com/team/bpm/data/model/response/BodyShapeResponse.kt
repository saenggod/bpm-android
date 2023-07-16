package com.team.bpm.data.model.response

import com.google.gson.annotations.SerializedName
import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.data.model.response.UserResponse.Companion.toDataModel
import com.team.bpm.domain.model.BodyShape
import kotlinx.parcelize.Parcelize

@Parcelize
data class BodyShapeResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("filesPath")
    val filesPath: List<String>?,
    @SerializedName("author")
    val author: UserResponse?,
    @SerializedName("dday")
    val dDay: Int?
) : BaseResponse {
    companion object : DataMapper<BodyShapeResponse, BodyShape> {
        override fun BodyShapeResponse.toDataModel(): BodyShape {
            return BodyShape(
                id = id,
                content = content?.replace("\\n", "\n"),
                createdAt = createdAt,
                updatedAt = updatedAt,
                filesPath = filesPath,
                author = author?.toDataModel(),
                dDay = dDay
            )
        }
    }
}
