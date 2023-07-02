package com.team.bpm.presentation.ui.main.mypage

import androidx.fragment.app.viewModels
import com.team.bpm.presentation.base.BaseFragment
import com.team.bpm.presentation.databinding.FragmentMypageBinding
import com.team.bpm.presentation.ui.main.mypage.edit_profile.EditProfileActivity
import com.team.bpm.presentation.ui.main.mypage.mypost.MyPostActivity
import com.team.bpm.presentation.ui.main.mypage.myquestion.MyQuestionActivity
import com.team.bpm.presentation.ui.main.mypage.myscrap.MyScrapActivity
import com.team.bpm.presentation.ui.main.mypage.notification.MyPageNotificationActivity
import com.team.bpm.presentation.ui.main.mypage.starttab.MyPageStartTabActivity
import com.team.bpm.presentation.util.repeatCallDefaultOnStarted
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::inflate) {

    override val viewModel: MyPageViewModel by viewModels()

    override fun initLayout() {
        bind {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun setupCollect() {
        repeatCallDefaultOnStarted {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is MyPageContract.Effect.ShowToast -> {
                        requireContext().showToast(effect.text)
                    }
                    MyPageContract.Effect.GoNotification -> {
                        // 알림 페이지 이동
                        goToNotification()
                    }
                    MyPageContract.Effect.GoMyPost -> {
                        // 내가 작성한 커뮤니티 글
                        goToMyPost()
                    }
                    MyPageContract.Effect.GoScrappedStudios -> {
                        // 스크랩한 스튜디오 리스트
                        goToMyScrap()
                    }
                    MyPageContract.Effect.GoProfileManage -> {
                        // 프로필 관리 페이지 이동
                        goToEditProfile()
                    }
                    MyPageContract.Effect.GoMyQuestion -> {
                        // 질문 모아보기
                        goToMyQuestion()
                    }
                    MyPageContract.Effect.GoEditStartTab -> {
                        // 시작탭 변경
                        goToEditStartTab()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getMyPageInfo()
    }

    private fun goToNotification() {
        startActivity(MyPageNotificationActivity.newIntent(requireContext()))
    }

    private fun goToEditProfile() {
        startActivity(EditProfileActivity.newIntent(requireContext()))
    }

    private fun goToMyScrap() {
        startActivity(MyScrapActivity.newIntent(requireContext()))
    }

    private fun goToMyQuestion() {
        startActivity(MyQuestionActivity.newIntent(requireContext()))
    }

    private fun goToMyPost() {
        startActivity(MyPostActivity.newIntent(requireContext()))
    }

    private fun goToEditStartTab() {
        startActivity(MyPageStartTabActivity.newIntent(requireContext()))
    }

    companion object {

        fun newInstance(): MyPageFragment {
            return MyPageFragment()
        }
    }
}