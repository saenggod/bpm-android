package com.team.bpm.presentation.model

import androidx.annotation.DrawableRes
import com.team.bpm.presentation.R

enum class NotificationType(val realType: String, @DrawableRes val iconRes: Int) {

    // 커뮤니티 글에 댓글이 달린 경우
    COMMUNITY_COMMENT(
        "communityComment",
        R.drawable.ic_notification_comment
    ),

    // 커뮤니티 글에 좋아요가 눌린 경우
    COMMUNITY_FAVORITE(
        "communityFavorite",
        R.drawable.ic_notification_like
    ),

    // 커뮤니티 글의 댓글에 좋아요가 달린 경우,
    COMMUNITY_COMMENT_FAVORITE(
        "communityCommentFavorite",
        R.drawable.ic_notification_like
    ),

    // 질문게시판 글에 댓글이 달린 경우
    QUESTION_BOARD_COMMENT(
        "questionBoardComment",
        R.drawable.ic_notification_comment
    ),

    // 질문게시판 글에 좋아요가 눌린 경우
    QUESTION_BOARD_FAVORITE(
        "questionBoardFavorite",
        R.drawable.ic_notification_like
    ),

    // 질문게시판 글의 댓글에 대댓글이 달린 경우
    QUESTION_BOARD_COMMENT_RESPONSE(
        "questionBoardCommentResponse",
        R.drawable.ic_notification_comment
    ),

    // 질문게시판 글의 댓글에 좋아요가 달린 경우
    QUESTION_BOARD_COMMENT_FAVORITE(
        "questionBoardCommentFavorite",
        R.drawable.ic_notification_like
    ),

    // 스튜디오 리뷰에 좋아요가 달린 경우
    REVIEW_FAVORITE(
        "reviewFavorite",
        R.drawable.ic_notification_like
    );
}

fun getNotificationType(typeText: String): NotificationType? {
    return NotificationType.values().firstOrNull { it.realType == typeText }
}