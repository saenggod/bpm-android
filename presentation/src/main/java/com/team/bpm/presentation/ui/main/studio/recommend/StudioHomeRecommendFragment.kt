package com.team.bpm.presentation.ui.main.studio.recommend

import android.content.Intent
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.team.bpm.presentation.base.BaseFragment
import com.team.bpm.presentation.databinding.FragmentHomeRecommendBinding
import com.team.bpm.presentation.model.StudioMainTabType
import com.team.bpm.presentation.ui.main.studio.detail.StudioDetailActivity
import com.team.bpm.presentation.ui.main.studio.recommend.list.StudioHomeRecommendAdapter
import com.team.bpm.presentation.util.repeatCallDefaultOnStarted
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudioHomeRecommendFragment :
    BaseFragment<FragmentHomeRecommendBinding>(FragmentHomeRecommendBinding::inflate) {

    override val viewModel: StudioHomeRecommendViewModel by viewModels()

    override fun initLayout() {
        bind {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner

            list.adapter = StudioHomeRecommendAdapter(
                listener = { studioId ->
                    viewModel.clickStudioDetail(studioId)
                },
                scrapListener = { studioId, isScrapped ->
                    viewModel.clickStudioScrap(studioId, isScrapped)
                }
            )
        }
    }

    override fun setupCollect() {
        repeatCallDefaultOnStarted {
            viewModel.state.collect { state ->
                when (state) {
                    StudioHomeRecommendState.Init -> {
                        viewModel.getStudioList()
                    }

                    StudioHomeRecommendState.List -> Unit

                    is StudioHomeRecommendState.Error -> {
                        requireContext().showToast(state.error.message)
                    }
                }
            }
        }

        repeatCallDefaultOnStarted {
            viewModel.event.collect { event ->
                when (event) {
                    is StudioHomeRecommendViewEvent.ClickDetail -> {
                        goToStudioDetail(event.studioId)
                    }
                    is StudioHomeRecommendViewEvent.ClickScrap -> {
                        goToStudioDetail(event.studioId)
                    }
                }
            }
        }
    }

    override fun onResume() {
        viewModel.getStudioList()

        super.onResume()
    }

    private fun goToStudioDetail(studioId: Int?) {
        studioId?.let {
            startActivity(Intent(context, StudioDetailActivity::class.java).apply {
                putExtra(StudioDetailActivity.KEY_STUDIO_ID, it)
            })
        }
    }

    companion object {
        const val KEY_TYPE = "KEY_TYPE"

        fun newInstance(type: StudioMainTabType): StudioHomeRecommendFragment {
            return StudioHomeRecommendFragment().apply {
                arguments = bundleOf(KEY_TYPE to type)
            }
        }
    }
}