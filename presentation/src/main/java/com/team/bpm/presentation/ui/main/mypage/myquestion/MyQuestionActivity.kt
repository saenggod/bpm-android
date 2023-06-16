package com.team.bpm.presentation.ui.main.mypage.myquestion

import android.content.Context
import android.content.Intent
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.team.bpm.presentation.base.BaseActivity
import com.team.bpm.presentation.databinding.ActivityMypageMyQuestionBinding
import com.team.bpm.presentation.ui.main.lounge.question.detail.QuestionDetailActivity
import com.team.bpm.presentation.ui.main.mypage.myquestion.more.MyQuestionMoreBottomSheet
import com.team.bpm.presentation.util.repeatCallDefaultOnStarted
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyQuestionActivity :
    BaseActivity<ActivityMypageMyQuestionBinding>(ActivityMypageMyQuestionBinding::inflate) {

    override val viewModel: MyQuestionViewModel by viewModels()

    private val onBackPressedCallBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.event(MyQuestionContract.Event.OnBackPressed)
        }
    }

    override fun initLayout() {
        bind {
            lifecycleOwner = this@MyQuestionActivity
            vm = this@MyQuestionActivity.viewModel

            list.adapter = MyQuestionAdapter {
                viewModel.event(MyQuestionContract.Event.OnClickListItem(it))
            }
        }

        onBackPressedDispatcher.addCallback(onBackPressedCallBack)
    }

    override fun setupCollect() {
        repeatCallDefaultOnStarted {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is MyQuestionContract.Effect.ShowToast -> {
                        showToast(effect.text)
                    }

                    MyQuestionContract.Effect.ShowDeleteBottomSheet -> {
                        showDeleteBottomSheet()
                    }

                    is MyQuestionContract.Effect.GoToQuestionDetail -> {
                        goToQuestionDetail(effect.questionId)
                    }

                    MyQuestionContract.Effect.GoOut -> {
                        finish()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getQuestionList(viewModel.offset)
        binding.list.smoothScrollBy(0, 0)
    }

    private fun showDeleteBottomSheet() {
        MyQuestionMoreBottomSheet.newInstance()
            .show(supportFragmentManager, MyQuestionMoreBottomSheet::class.java.simpleName)
    }

    private fun goToQuestionDetail(questionId: Int) {
        startActivity(QuestionDetailActivity.newIntent(this, questionId))
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, MyQuestionActivity::class.java)
        }
    }
}