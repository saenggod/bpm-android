package com.team.bpm.presentation.ui.main.lounge.question

import androidx.fragment.app.viewModels
import com.team.bpm.presentation.base.BaseFragment
import com.team.bpm.presentation.databinding.FragmentLoungeQuestionBinding
import com.team.bpm.presentation.ui.main.lounge.question.detail.QuestionDetailActivity
import com.team.bpm.presentation.util.repeatCallDefaultOnStarted
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionFragment :
    BaseFragment<FragmentLoungeQuestionBinding>(FragmentLoungeQuestionBinding::inflate) {

    override val viewModel: QuestionViewModel by viewModels()

    override fun initLayout() {
        bind {
            lifecycleOwner = viewLifecycleOwner
            vm = this@QuestionFragment.viewModel

            list.adapter = QuestionAdapter {
                viewModel.event(QuestionContract.Event.OnClickListItem(it))
            }
        }
    }

    override fun setupCollect() {
        repeatCallDefaultOnStarted {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is QuestionContract.Effect.ShowToast -> {
                        requireContext().showToast(effect.text)
                    }
                    QuestionContract.Effect.GoToTop -> {
                        binding.list.smoothScrollToPosition(0)
                    }
                    is QuestionContract.Effect.GoToQuestionDetail -> {
                        goToQuestionDetail(effect.questionId)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getQuestionList(viewModel.offset)
    }

    private fun goToQuestionDetail(questionId: Int) {
        startActivity(QuestionDetailActivity.newIntent(requireContext(), questionId))
    }

    companion object {
        fun newInstance(): QuestionFragment {
            return QuestionFragment()
        }
    }
}