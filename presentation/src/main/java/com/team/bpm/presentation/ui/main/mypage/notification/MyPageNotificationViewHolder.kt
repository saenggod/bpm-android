package com.team.bpm.presentation.ui.main.mypage.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.team.bpm.domain.model.Notification
import com.team.bpm.presentation.R
import com.team.bpm.presentation.databinding.ItemMypageNotificationBinding
import com.team.bpm.presentation.model.NotificationType
import com.team.bpm.presentation.model.getNotificationType
import com.team.bpm.presentation.util.bindImageSrc
import com.team.bpm.presentation.util.bindImageUrl
import com.team.bpm.presentation.util.highLightWord

class MyPageNotificationViewHolder(
    private val binding: ItemMypageNotificationBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Notification, listener: (Notification) -> Unit) {
        with(binding) {

            val notificationType = getNotificationType(item.type)

            notificationType?.let { type ->

                var titleText = ""

                when (type) {
                    NotificationType.COMMUNITY_COMMENT -> {
                        titleText = "${item.responder.nickname}님이 회원님의 게시물에 댓글을 남겼습니다"
                        content.isVisible = true
                        content.text = item.commentBody
                        image.isVisible = true
                        image.bindImageUrl(item.filePath)
                        replyContent.text = item.content
                    }
                    NotificationType.COMMUNITY_FAVORITE -> {
                        titleText = "${item.responder.nickname}님이 회원님의 게시물을 좋아합니다"
                        content.isVisible = true
                        content.text = item.commentBody
                        image.isVisible = true
                        image.bindImageUrl(item.filePath)
                        replyContent.text = item.content
                    }
                    NotificationType.COMMUNITY_COMMENT_FAVORITE -> {
                        titleText = "${item.responder.nickname}님이 회원님의 댓글에 대댓글을 남겼습니다"
                        content.isVisible = true
                        content.text = item.commentBody
                        image.isVisible = true
                        image.bindImageUrl(item.filePath)
                        replyContent.text = item.content
                    }
                    NotificationType.QUESTION_BOARD_COMMENT -> {
                        titleText = "${item.responder.nickname}님이 회원님의 질문에 댓글을 남겼습니다"
                        content.isVisible = true
                        content.text = item.commentBody
                        image.isGone = true
                        replyContent.text = item.content
                    }
                    NotificationType.QUESTION_BOARD_FAVORITE -> {
                        titleText = "${item.responder.nickname}님이 회원님의 질문을 좋아합니다"
                        content.isGone = true
                        image.isGone = true
                        replyContent.text = item.content
                    }
                    NotificationType.QUESTION_BOARD_COMMENT_RESPONSE -> {
                        titleText = "${item.responder.nickname}님이 회원님의 댓글에 대댓글을 남겼습니다"
                        content.isVisible = true
                        content.text = item.commentBody
                        image.isGone = true
                        replyContent.text = item.content
                    }
                    NotificationType.QUESTION_BOARD_COMMENT_FAVORITE -> {
                        titleText = "${item.responder.nickname}님이 회원님의 댓글을 좋아합니다"
                        content.isVisible = true
                        content.text = item.commentBody
                        image.isGone = true
                        replyContent.text = item.content
                    }
                    NotificationType.REVIEW_FAVORITE -> {
                        titleText = "${item.responder.nickname}님이 회원님의 스튜디오 리뷰를 좋아합니다"
                        content.isVisible = true
                        content.text = item.commentBody
                        image.isGone = true
                        replyContent.text = item.content
                    }
                }

                title.text = titleText.highLightWord(item.responder.nickname)
                icon.bindImageSrc(notificationType.iconRes)

                // TODO : 알림에 대한 시간도 필요할 것 같음
                time.isGone = true

                root.setBackgroundColor(
                    if (item.read) {
                        ContextCompat.getColor(root.context, R.color.white)
                    } else {
                        ContextCompat.getColor(root.context, R.color.green_A8FF0F_10)
                    }
                )

                root.setOnClickListener {
                    listener.invoke(item)
                }

            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): MyPageNotificationViewHolder {
            val binding = ItemMypageNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return MyPageNotificationViewHolder(binding)
        }
    }
}
