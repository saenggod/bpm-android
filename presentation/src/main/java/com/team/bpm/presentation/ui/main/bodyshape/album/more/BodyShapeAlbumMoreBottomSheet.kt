package com.team.bpm.presentation.ui.main.bodyshape.album.more

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.team.bpm.presentation.base.BaseBottomSheetFragment
import com.team.bpm.presentation.databinding.BottomsheetBodyshapeAlbumMoreBinding
import com.team.bpm.presentation.databinding.BottomsheetMyQuestionMoreBinding
import com.team.bpm.presentation.ui.main.bodyshape.album.BodyShapeAlbumViewModel
import com.team.bpm.presentation.ui.main.mypage.myquestion.MyQuestionViewModel
import com.team.bpm.presentation.util.repeatCallDefaultOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BodyShapeAlbumMoreBottomSheet :
    BaseBottomSheetFragment<BottomsheetBodyshapeAlbumMoreBinding>(BottomsheetBodyshapeAlbumMoreBinding::inflate) {

    override val viewModel: BodyShapeAlbumMoreViewModel by viewModels()

    private val activityViewModel: BodyShapeAlbumViewModel by activityViewModels()

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
                    BodyShapeAlbumMoreContract.Effect.GoEdit -> {
                        activityViewModel.onClickAlbumEdit()
                        dismissAllowingStateLoss()
                    }
                    BodyShapeAlbumMoreContract.Effect.GoDelete -> {
                        activityViewModel.deleteAlbum()
                        dismissAllowingStateLoss()
                    }
                }
            }
        }
    }

    companion object {

        fun newInstance(): BodyShapeAlbumMoreBottomSheet {
            return BodyShapeAlbumMoreBottomSheet()
        }
    }
}