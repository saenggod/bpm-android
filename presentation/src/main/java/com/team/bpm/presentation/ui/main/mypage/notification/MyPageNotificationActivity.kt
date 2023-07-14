package com.team.bpm.presentation.ui.main.mypage.notification

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.team.bpm.domain.model.Notification
import com.team.bpm.presentation.base.BaseActivity
import com.team.bpm.presentation.databinding.ActivityMypageNotificationBinding
import com.team.bpm.presentation.model.NotificationType
import com.team.bpm.presentation.model.getNotificationType
import com.team.bpm.presentation.ui.main.lounge.community.detail.CommunityDetailActivity
import com.team.bpm.presentation.ui.main.lounge.question.detail.QuestionDetailActivity
import com.team.bpm.presentation.ui.main.studio.detail.StudioDetailActivity
import com.team.bpm.presentation.util.repeatCallDefaultOnStarted
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageNotificationActivity :
    BaseActivity<ActivityMypageNotificationBinding>(ActivityMypageNotificationBinding::inflate) {

    override val viewModel: MyPageNotificationViewModel by viewModels()

    private val notificationAdapter: MyPageNotificationAdapter by lazy {
        MyPageNotificationAdapter { item ->
            viewModel.event(MyPageNotificationContract.Event.ClickNotification(item))
        }
    }

    override fun initLayout() {
        bind {
            vm = viewModel
            lifecycleOwner = this@MyPageNotificationActivity

            list.adapter = notificationAdapter

            refresh.setOnRefreshListener {
                refresh.isRefreshing = false

                viewModel.getNotifications()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getNotifications()
    }

    override fun setupCollect() {
        repeatCallDefaultOnStarted {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is MyPageNotificationContract.Effect.ShowToast -> {
                        showToast(effect.text)
                    }
                    is MyPageNotificationContract.Effect.GoToNotificationDetail -> {
                        goToNotificationDetail(effect.item)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.notificationList.collect { notificationList ->
                notificationAdapter.submitData(lifecycle, notificationList)
            }
        }
    }

    private fun goToNotificationDetail(item: Notification) {
        val notificationType = getNotificationType(item.type)

        notificationType?.let { type ->
            when (type) {
                // 커뮤니티 게시글로 이동
                NotificationType.COMMUNITY_FAVORITE, NotificationType.COMMUNITY_COMMENT_FAVORITE, NotificationType.COMMUNITY_COMMENT -> {
                    startActivity(CommunityDetailActivity.newIntent(this, item.contentId.toInt()))
                }

                // 질문 게시글로 이동
                NotificationType.QUESTION_BOARD_COMMENT_RESPONSE, NotificationType.QUESTION_BOARD_COMMENT, NotificationType.QUESTION_BOARD_COMMENT_FAVORITE, NotificationType.QUESTION_BOARD_FAVORITE -> {
                    startActivity(QuestionDetailActivity.newIntent(this, item.contentId.toInt()))
                }
                // 스튜디오 상세로 이동
                NotificationType.REVIEW_FAVORITE -> {
                    startActivity(StudioDetailActivity.newIntent(this, item.contentId.toInt()))
                }
            }
        }
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, MyPageNotificationActivity::class.java)
        }
    }
}