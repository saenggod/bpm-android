package com.team.bpm.presentation.ui.main.bodyshape

import androidx.fragment.app.viewModels
import com.team.bpm.presentation.base.BaseFragment
import com.team.bpm.presentation.databinding.FragmentBodyshapeBinding
import com.team.bpm.presentation.ui.main.bodyshape.album.BodyShapeAlbumActivity
import com.team.bpm.presentation.ui.main.bodyshape.album.add.BodyShapeAlbumAddActivity
import com.team.bpm.presentation.ui.main.bodyshape.detail.posting.BodyShapeDetailPostingActivity
import com.team.bpm.presentation.util.repeatCallDefaultOnStarted
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class BodyShapeFragment :
    BaseFragment<FragmentBodyshapeBinding>(FragmentBodyshapeBinding::inflate) {

    override val viewModel: BodyShapeViewModel by viewModels()

    override fun initLayout() {
        bind {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner

            list.adapter = BodyShapeAdapter(
                listener = {
                    viewModel.onClickBodyShapeDetail(it)
                },
                imageClickListener = {
                    viewModel.onClickBodyShapePosting(it)
                }
            )

            refresh.setOnRefreshListener {
                viewModel.getUserSchedule()
                refresh.isRefreshing = false
            }
        }

        viewModel.getUserSchedule()
    }

    override fun setupCollect() {
        repeatCallDefaultOnStarted {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    is BodyShapeContract.Effect.ShowToast -> {
                        requireContext().showToast(effect.text)
                    }
                    is BodyShapeContract.Effect.GoBodyShapeDetail -> {
                        goToBodyShapeAlbum(effect.id)
                    }
                    is BodyShapeContract.Effect.GoBodyShapePosting -> {
                        goToBodyShapePosting(effect.id)
                    }
                    BodyShapeContract.Effect.GoCreateBodyShape -> {
                        goToAddBodyShapeAlbum()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getUserSchedule()
    }

    // 눈바디 앨범 상세
    private fun goToBodyShapeAlbum(id: Int) {
        startActivity(BodyShapeAlbumActivity.newIntent(requireContext(), id))
    }

    // 오늘의 눈바디 사진 찍기
    private fun goToBodyShapePosting(id: Int) {
        startActivity(BodyShapeDetailPostingActivity.newIntent(requireContext(), id))
    }

    // 눈바디 앨범 생성
    private fun goToAddBodyShapeAlbum() {
        startActivity(BodyShapeAlbumAddActivity.newIntent(requireContext(), null))
    }

    companion object {

        fun newInstance(): BodyShapeFragment {
            return BodyShapeFragment()
        }
    }
}