package com.team.bpm.data.model.response

import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.domain.model.UserProfile
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProfileResponse(
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

    companion object : DataMapper<UserProfileResponse, UserProfile> {
        override fun UserProfileResponse.toDataModel(): UserProfile {
            return UserProfile(
                id = id,
                nickname = nickname,
                bio = bio,
                token = token,
                image = image
            )
        }
    }
}
