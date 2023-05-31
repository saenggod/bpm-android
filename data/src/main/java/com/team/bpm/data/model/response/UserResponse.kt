package com.team.bpm.data.model.response

import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.domain.model.User
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("nickname")
    val nickname: String?,
    @SerializedName("profilePath")
    val profilePath: String?
) : BaseResponse {

    companion object : DataMapper<UserResponse, User> {
        override fun UserResponse.toDataModel(): User {
            return User(
                id = id?: 0,
                nickname = nickname ?: "",
                profilePath = profilePath ?: ""
            )
        }
    }
}
