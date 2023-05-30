package com.team.bpm.data.model.response

import com.google.gson.annotations.SerializedName
import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.data.model.response.UserResponse.Companion.toDataModel
import com.team.bpm.domain.model.EyeBody
import kotlinx.parcelize.Parcelize

@Parcelize
data class EyeBodyResponse(
    @SerializedName(value = "id")
    val id: Int?,
    @SerializedName(value = "content")
    val content: String?,
    @SerializedName(value = "createdAt")
    val createdAt: String?,
    @SerializedName(value = "updatedAt")
    val updatedAt: String?,
    @SerializedName(value = "filesPath")
    val filesPath: List<String>?,
    @SerializedName(value = "author")
    val author: UserResponse?
) : BaseResponse {
    companion object : DataMapper<EyeBodyResponse, EyeBody> {
        override fun EyeBodyResponse.toDataModel(): EyeBody {
            return EyeBody(
                id = id,
                content = content,
                createdAt = createdAt,
                updatedAt = updatedAt,
                filesPath = filesPath,
                author = author?.toDataModel()
            )
        }
    }
}
