package com.team.bpm.presentation.ui.main.mypage

import androidx.fragment.app.viewModels
import com.team.bpm.presentation.base.BaseFragment
import com.team.bpm.presentation.databinding.FragmentMypageBinding
import com.team.bpm.presentation.ui.main.mypage.myquestion.MyQuestionActivity
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

                    MyPageContract.Effect.GoProfileManage -> {
                        // 프로필 관리 페이지 이동
                    }

                    MyPageContract.Effect.GoHistoryPost -> {
                        startActivity(MyQuestionActivity.newIntent(requireContext()))
                        // 질문 모아보기 (API 필요)
                    }

                    MyPageContract.Effect.GoEditStartTab -> {
                        startActivity(MyPageStartTabActivity.newIntent(requireContext()))
                    }
                }
            }
        }
    }

    companion object {

        fun newInstance(): MyPageFragment {
            return MyPageFragment()
        }
    }
}