package com.team.bpm.presentation.ui.main.lounge

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.team.bpm.presentation.R
import com.team.bpm.presentation.base.BaseFragment
import com.team.bpm.presentation.databinding.FragmentLoungeBinding
import com.team.bpm.presentation.ui.main.lounge.add.LoungeAddBottomSheet
import com.team.bpm.presentation.ui.main.lounge.community.CommunityFragment
import com.team.bpm.presentation.ui.main.lounge.community.posting.CommunityPostingActivity
import com.team.bpm.presentation.ui.main.lounge.question.QuestionFragment
import com.team.bpm.presentation.ui.main.lounge.question.posting.QuestionPostingActivity
import com.team.bpm.presentation.util.BasePagerAdapter
import com.team.bpm.presentation.util.repeatCallDefaultOnStarted
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoungeFragment : BaseFragment<FragmentLoungeBinding>(FragmentLoungeBinding::inflate) {

    override val viewModel: LoungeViewModel by viewModels()

    private val fragmentList: List<Fragment> by lazy {
        listOf(
            CommunityFragment.newInstance(),
            QuestionFragment.newInstance(),
        )
    }

    override fun initLayout() {
        bind {
            lifecycleOwner = viewLifecycleOwner
            vm = this@LoungeFragment.viewModel
        }

        setupPager()
    }

    override fun setupCollect() {
        repeatCallDefaultOnStarted {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is LoungeContract.Effect.ShowToast -> {
                        requireContext().showToast(effect.text)
                    }

                    is LoungeContract.Effect.ShowAddBottomSheet -> {
                        showAddPostBottomSheet()
                    }

                    LoungeContract.Effect.GoToAddCommunityPost -> {
                        goToCommunityPosting()
                    }

                    LoungeContract.Effect.GoToAddQuestionPost -> {
                        goToQuestionPosting()
                    }
                }
            }
        }
    }

    private fun setupPager() {
        bind {
            pager.adapter = BasePagerAdapter(requireActivity(), fragmentList)

            TabLayoutMediator(tab, pager, false, true) { tab: TabLayout.Tab?, position: Int ->
                val resId: Int = when (position) {
                    0 -> R.string.lounge_tab_community
                    1 -> R.string.lounge_tab_question
                    else -> -1
                }

                if (resId != -1) {
                    tab?.setText(resId)
                }
            }.attach()

            pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.setCurrentTabPosition(position)
                }
            })

        }
    }

    private fun showAddPostBottomSheet() {
        LoungeAddBottomSheet().show(childFragmentManager, LoungeAddBottomSheet::class.simpleName)
    }

    private fun goToCommunityPosting() {
        startActivity(CommunityPostingActivity.newIntent(requireContext()))
    }

    private fun goToQuestionPosting() {
        startActivity(QuestionPostingActivity.newIntent(requireContext()))
    }

    companion object {

        fun newInstance(): LoungeFragment {
            return LoungeFragment()
        }
    }
}