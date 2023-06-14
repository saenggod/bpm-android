package com.team.bpm.data.model.response

import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.domain.model.UserInfo
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUpResponse(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("nickname")
    val nickname: String?,
    @SerializedName("bio")
    val bio: String?,
    @SerializedName("token")
    val token: String?,
    @SerializedName("image")
    val image: String?
) : BaseResponse {

    companion object : DataMapper<SignUpResponse, UserInfo> {
        override fun SignUpResponse.toDataModel(): UserInfo {
            return UserInfo(
                id = id,
                nickname = nickname,
                bio = bio,
                token = token,
                image = image
            )
        }
    }
}
