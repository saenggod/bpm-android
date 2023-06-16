package com.team.bpm.presentation.ui.main.mypage.myquestion.more

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.team.bpm.presentation.base.BaseBottomSheetFragment
import com.team.bpm.presentation.databinding.BottomsheetMyQuestionMoreBinding
import com.team.bpm.presentation.ui.main.mypage.myquestion.MyQuestionViewModel
import com.team.bpm.presentation.util.repeatCallDefaultOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyQuestionMoreBottomSheet :
    BaseBottomSheetFragment<BottomsheetMyQuestionMoreBinding>(BottomsheetMyQuestionMoreBinding::inflate) {

    override val viewModel: MyQuestionMoreViewModel by viewModels()

    private val activityViewModel: MyQuestionViewModel by activityViewModels()

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
                    MyQuestionMoreContract.Effect.GoDelete -> {
                        activityViewModel.onClickDeleteMode()
                        dismissAllowingStateLoss()
                    }
                }
            }
        }
    }

    companion object {

        fun newInstance(): MyQuestionMoreBottomSheet {
            return MyQuestionMoreBottomSheet()
        }
    }
}