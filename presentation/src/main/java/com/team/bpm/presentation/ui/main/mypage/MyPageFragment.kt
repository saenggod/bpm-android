package com.team.bpm.presentation.ui.main.mypage

import androidx.fragment.app.viewModels
import com.team.bpm.presentation.databinding.FragmentMypageBinding
import com.team.bpm.presentation.base.BaseFragment
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
            viewModel.event.collect { event ->
                when (event) {
                    MyPageViewEvent.Click -> {
                        requireContext().showToast("오픈 예정입니다!")
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