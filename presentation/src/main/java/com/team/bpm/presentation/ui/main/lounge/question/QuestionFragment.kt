package com.team.bpm.presentation.ui.main.lounge.question

import androidx.fragment.app.viewModels
import com.team.bpm.presentation.base.BaseFragment
import com.team.bpm.presentation.databinding.FragmentLoungeQuestionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionFragment :
    BaseFragment<FragmentLoungeQuestionBinding>(FragmentLoungeQuestionBinding::inflate) {

    override val viewModel: QuestionViewModel by viewModels()

    override fun initLayout() {
        bind {
            lifecycleOwner = viewLifecycleOwner
            vm = this@QuestionFragment.viewModel
        }

    }

    companion object {
        fun newInstance(): QuestionFragment {
            return QuestionFragment()
        }
    }
}