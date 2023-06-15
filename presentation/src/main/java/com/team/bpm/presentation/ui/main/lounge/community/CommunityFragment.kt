package com.team.bpm.presentation.ui.main.lounge.community

import androidx.fragment.app.viewModels
import com.team.bpm.presentation.base.BaseFragment
import com.team.bpm.presentation.databinding.FragmentLoungeCommunityBinding
import com.team.bpm.presentation.ui.main.lounge.community.detail.CommunityDetailActivity
import com.team.bpm.presentation.util.repeatCallDefaultOnStarted
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityFragment :
    BaseFragment<FragmentLoungeCommunityBinding>(FragmentLoungeCommunityBinding::inflate) {

    override val viewModel: CommunityViewModel by viewModels()

    override fun initLayout() {
        bind {
            vm = this@CommunityFragment.viewModel
            lifecycleOwner = viewLifecycleOwner

            list.adapter = CommunityAdapter {
                viewModel.event(CommunityContract.Event.OnClickListItem(it))
            }
        }
    }

    override fun setupCollect() {
        repeatCallDefaultOnStarted {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is CommunityContract.Effect.ShowToast -> {
                        requireContext().showToast(effect.text)
                    }

                    is CommunityContract.Effect.GoToCommunityDetail -> {
                        goToCommunityDetail(effect.communityId)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getCommunityList()
        binding.list.smoothScrollBy(0, 0)
    }

    private fun goToCommunityDetail(communityId: Int) {
        startActivity(CommunityDetailActivity.newIntent(requireContext(), communityId))
    }

    companion object {
        fun newInstance(): CommunityFragment {
            return CommunityFragment()
        }
    }
}