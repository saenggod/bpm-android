package com.team.bpm.presentation.ui.main.mypage.mypost

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.team.bpm.presentation.base.BaseActivity
import com.team.bpm.presentation.databinding.ActivityMypageMyPostBinding
import com.team.bpm.presentation.ui.main.lounge.community.CommunityAdapter
import com.team.bpm.presentation.ui.main.lounge.community.detail.CommunityDetailActivity
import com.team.bpm.presentation.util.repeatCallDefaultOnStarted
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPostActivity :
    BaseActivity<ActivityMypageMyPostBinding>(ActivityMypageMyPostBinding::inflate) {

    override val viewModel: MyPostViewModel by viewModels()

    override fun initLayout() {
        bind {
            lifecycleOwner = this@MyPostActivity
            vm = this@MyPostActivity.viewModel

            list.adapter = CommunityAdapter {
                viewModel.event(MyPostContract.Event.OnClickListItem(it))
            }
        }

    }

    override fun setupCollect() {
        repeatCallDefaultOnStarted {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is MyPostContract.Effect.ShowToast -> {
                        showToast(effect.text)
                    }
                    is MyPostContract.Effect.GoToCommunityDetail -> {
                        goToCommunityDetail(effect.communityId)
                    }

                    MyPostContract.Effect.GoOut -> {
                        finish()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCommunityList(viewModel.offset)
        binding.list.smoothScrollBy(0, 0)
    }

    private fun goToCommunityDetail(id: Int) {
        startActivity(CommunityDetailActivity.newIntent(this, id))
    }


    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, MyPostActivity::class.java)
        }
    }
}