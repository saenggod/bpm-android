package com.team.bpm.presentation.ui.main.lounge.community

import androidx.fragment.app.viewModels
import com.team.bpm.presentation.base.BaseFragment
import com.team.bpm.presentation.databinding.FragmentLoungeCommunityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityFragment :
    BaseFragment<FragmentLoungeCommunityBinding>(FragmentLoungeCommunityBinding::inflate) {

    override val viewModel: CommunityViewModel by viewModels()

    override fun initLayout() {
        bind {
            lifecycleOwner = viewLifecycleOwner
            vm = this@CommunityFragment.viewModel
        }
    }

    companion object {
        fun newInstance(): CommunityFragment {
            return CommunityFragment()
        }
    }
}