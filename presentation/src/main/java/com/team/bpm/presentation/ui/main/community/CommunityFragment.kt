package com.team.bpm.presentation.ui.main.community

import androidx.fragment.app.viewModels
import com.team.bpm.presentation.databinding.FragmentCommunityBinding
import com.team.bpm.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityFragment :
    BaseFragment<FragmentCommunityBinding>(FragmentCommunityBinding::inflate) {

    override val viewModel: CommunityViewModel by viewModels()

    override fun initLayout() = Unit

    companion object {

        fun newInstance(): CommunityFragment {
            return CommunityFragment()
        }
    }
}