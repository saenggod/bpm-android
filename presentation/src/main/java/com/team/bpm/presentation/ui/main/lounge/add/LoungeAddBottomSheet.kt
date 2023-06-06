package com.team.bpm.presentation.ui.main.lounge.add

import androidx.fragment.app.viewModels
import com.team.bpm.presentation.R
import com.team.bpm.presentation.base.BaseBottomSheetFragment
import com.team.bpm.presentation.databinding.BottomsheetLoungeAddBinding
import com.team.bpm.presentation.ui.main.lounge.LoungeViewModel
import com.team.bpm.presentation.util.repeatCallDefaultOnStarted
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoungeAddBottomSheet :
    BaseBottomSheetFragment<BottomsheetLoungeAddBinding>(BottomsheetLoungeAddBinding::inflate) {

    override fun getTheme(): Int = R.style.BPMBottomSheetDialog

    override val viewModel: LoungeAddViewModel by viewModels()

    private val fragmentViewModel: LoungeViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

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
                    is LoungeAddContract.Effect.ShowToast -> {
                        requireContext().showToast(effect.text)
                    }

                    LoungeAddContract.Effect.GoToAddCommunityPost -> {
                        goToAddCommunityPost()
                    }

                    LoungeAddContract.Effect.GoToAddQuestionPost -> {
                        goToAddQuestionPost()
                    }
                }
                dismissAllowingStateLoss()
            }
        }
    }

    private fun goToAddCommunityPost() {
        fragmentViewModel.onClickAddCommunityPost()
    }

    private fun goToAddQuestionPost() {
        fragmentViewModel.onClickAddQuestionPost()
    }

    companion object {

        fun newInstance(): LoungeAddBottomSheet {
            return LoungeAddBottomSheet()
        }
    }
}