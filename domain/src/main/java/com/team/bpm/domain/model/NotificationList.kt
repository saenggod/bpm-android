package com.team.bpm.domain.model

import com.team.bpm.domain.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationList(
    val alarmResponseList: List<Notification> = emptyList(),
    val alarmCount: Int = 0
) : BaseModel

@Parcelize
data class Notification(
    val id: Long = 0L,
    val type: String = "",
    val contentId: Long = 0L,
    val commentId: Long = 0L,
    val filePath: String = "",
    val content: String = "",
    val commentBody: String = "",
    val responder: NotificationResponder = NotificationResponder(),
    val read: Boolean = false,
) : BaseModel

@Parcelize
data class NotificationResponder(
    val id: Long = 0L,
    val nickname: String = ""
) : BaseModel
