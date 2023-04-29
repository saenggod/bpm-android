package com.team.bpm.presentation.ui.main.community

import androidx.fragment.app.viewModels
import com.team.bpm.presentation.base.BaseFragment
import com.team.bpm.presentation.databinding.FragmentLoungeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoungeFragment : BaseFragment<FragmentLoungeBinding>(FragmentLoungeBinding::inflate) {

    override val viewModel: LoungeViewModel by viewModels()

    override fun initLayout() = Unit

    companion object {

        fun newInstance(): LoungeFragment {
            return LoungeFragment()
        }
    }
}