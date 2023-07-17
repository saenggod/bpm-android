package com.team.bpm.presentation.ui.main.bodyshape

import androidx.fragment.app.viewModels
import com.team.bpm.presentation.base.BaseFragment
import com.team.bpm.presentation.databinding.FragmentBodyshapeBinding
import com.team.bpm.presentation.ui.main.bodyshape.album.BodyShapeAlbumActivity
import com.team.bpm.presentation.ui.main.bodyshape.album.add.BodyShapeAlbumAddActivity
import com.team.bpm.presentation.ui.main.bodyshape.detail.BodyShapeDetailActivity
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
                    viewModel.onClickAlbumDetail(it)
                },
                imageClickListener = { albumId, bodyShapeId ->
                    if(bodyShapeId == null) {
                        viewModel.onClickBodyShapePosting(albumId)
                    } else {
                        viewModel.onClickAlbumDetail(albumId)
                        viewModel.onClickBodyShapeDetail(albumId, bodyShapeId)
                    }
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
                    is BodyShapeContract.Effect.GoBodyAlbumDetail -> {
                        goToBodyShapeAlbum(effect.id)
                    }
                    is BodyShapeContract.Effect.GoBodyShapeDetail -> {
                        goToBodyShapeDetail(effect.albumId, effect.bodyShapeDetailId, effect.dday)
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

    // 눈바디 사진 상세
    private fun goToBodyShapeDetail(albumId: Int, bodyShapeDetailId : Int, dday : Int) {
        startActivity(BodyShapeDetailActivity.newIntent(requireContext(), albumId, bodyShapeDetailId, dday))
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