package com.team.bpm.data.model.response

import com.google.gson.annotations.SerializedName
import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.data.model.response.NotificationResponderResponse.Companion.toDataModel
import com.team.bpm.data.model.response.NotificationResponse.Companion.toDataModel
import com.team.bpm.domain.model.Notification
import com.team.bpm.domain.model.NotificationList
import com.team.bpm.domain.model.NotificationResponder
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationListResponse(
    @SerializedName("alarmResponseList")
    val alarmResponseList: List<NotificationResponse>?,
    @SerializedName("alarmCount")
    val alarmCount: Int?
) : BaseResponse {
    companion object : DataMapper<NotificationListResponse, NotificationList> {
        override fun NotificationListResponse.toDataModel(): NotificationList {
            return NotificationList(
                alarmResponseList = alarmResponseList?.map { it.toDataModel() } ?: emptyList(),
                alarmCount = alarmCount ?: 0
            )
        }
    }
}

@Parcelize
data class NotificationResponse(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("contentId")
    val contentId: Long?,
    @SerializedName("commentId")
    val commentId: Long?,
    @SerializedName("filePath")
    val filePath: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("commentBody")
    val commentBody: String?,
    @SerializedName("responder")
    val responder: NotificationResponderResponse?,
    @SerializedName("read")
    val read: Boolean?,
) : BaseResponse {
    companion object : DataMapper<NotificationResponse, Notification> {
        override fun NotificationResponse.toDataModel(): Notification {
            return Notification(
                id = id ?: 0L,
                type = type.orEmpty(),
                contentId = contentId ?: 0L,
                commentId = commentId ?: 0L,
                filePath = filePath.orEmpty(),
                content = content.orEmpty(),
                commentBody = commentBody.orEmpty(),
                responder = responder?.toDataModel() ?: NotificationResponder(),
                read = read ?: false
            )
        }
    }
}

@Parcelize
data class NotificationResponderResponse(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("nickname")
    val nickname: String?
) : BaseResponse {
    companion object : DataMapper<NotificationResponderResponse, NotificationResponder> {
        override fun NotificationResponderResponse.toDataModel(): NotificationResponder {
            return NotificationResponder(
                id = id ?: 0L,
                nickname = nickname.orEmpty()
            )
        }
    }
}
