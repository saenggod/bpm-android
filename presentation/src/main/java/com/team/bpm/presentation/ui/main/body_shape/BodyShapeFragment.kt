package com.team.bpm.presentation.ui.main.body_shape

import androidx.fragment.app.viewModels
import com.team.bpm.presentation.base.BaseFragment
import com.team.bpm.presentation.databinding.FragmentBodyShapeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BodyShapeFragment : BaseFragment<FragmentBodyShapeBinding>(FragmentBodyShapeBinding::inflate) {

    override val viewModel: BodyShapeViewModel by viewModels()

    override fun initLayout() = Unit

    companion object {

        fun newInstance(): BodyShapeFragment {
            return BodyShapeFragment()
        }
    }
}