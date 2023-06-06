package com.team.bpm.presentation.ui.main.mypage.starttab

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.team.bpm.presentation.base.BaseActivity
import com.team.bpm.presentation.databinding.FragmentMypageBinding
import com.team.bpm.presentation.base.BaseFragment
import com.team.bpm.presentation.databinding.ActivityMypageStarttabBinding
import com.team.bpm.presentation.util.repeatCallDefaultOnStarted
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageStartTabActivity :
    BaseActivity<ActivityMypageStarttabBinding>(ActivityMypageStarttabBinding::inflate) {

    override val viewModel: MyPageStartTabViewModel by viewModels()

    override fun initLayout() {
        bind {
            vm = viewModel
            lifecycleOwner = this@MyPageStartTabActivity

            list.adapter = MyPageStartTabAdapter {
                it?.let { index -> viewModel.event(MyPageStartTabContract.Event.EditTab(index)) }
            }
        }
    }

    override fun setupCollect() {
        repeatCallDefaultOnStarted {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is MyPageStartTabContract.Effect.ShowToast -> {
                        showToast(effect.text)
                    }
                }
            }
        }
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, MyPageStartTabActivity::class.java)
        }
    }
}