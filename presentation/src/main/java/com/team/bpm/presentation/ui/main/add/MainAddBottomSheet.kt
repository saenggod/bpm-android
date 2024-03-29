package com.team.bpm.presentation.ui.main.add

import androidx.fragment.app.viewModels
import com.team.bpm.presentation.R
import com.team.bpm.presentation.databinding.BottomsheetMainAddBinding
import com.team.bpm.presentation.base.BaseBottomSheetFragment
import com.team.bpm.presentation.util.repeatCallDefaultOnStarted
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainAddBottomSheet :
    BaseBottomSheetFragment<BottomsheetMainAddBinding>(BottomsheetMainAddBinding::inflate) {

    override fun getTheme(): Int = R.style.BPMBottomSheetDialog

    override val viewModel: MainAddViewModel by viewModels()

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
                    MainAddViewEvent.Click -> {
                        requireContext().showToast("오픈 예정입니다!")
                    }
                }
            }
        }
    }

    companion object {

        fun newInstance(): MainAddBottomSheet {
            return MainAddBottomSheet()
        }
    }
}