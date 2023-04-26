package com.team.bpm.presentation.ui.main.notification

import androidx.fragment.app.viewModels
import com.team.bpm.presentation.base.BaseFragment
import com.team.bpm.presentation.databinding.FragmentEyebodyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EyebodyFragment : BaseFragment<FragmentEyebodyBinding>(FragmentEyebodyBinding::inflate) {

    override val viewModel: EyebodyViewModel by viewModels()

    override fun initLayout() = Unit

    companion object {

        fun newInstance(): EyebodyFragment {
            return EyebodyFragment()
        }
    }
}