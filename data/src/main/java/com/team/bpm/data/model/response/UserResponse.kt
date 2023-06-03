package com.team.bpm.data.model.response

import com.google.gson.annotations.SerializedName
import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.domain.model.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("nickname")
    val nickname: String?,
    @SerializedName("profilePath")
    val profilePath: String?
) : BaseResponse {

    companion object : DataMapper<UserResponse, User> {
        override fun UserResponse.toDataModel(): User {
            return User(
                id = id,
                nickname = nickname,
                profilePath = profilePath
            )
        }
    }
}
