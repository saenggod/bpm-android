package com.team.bpm.presentation.ui.main.studio

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.team.bpm.presentation.R
import com.team.bpm.presentation.base.BaseFragment
import com.team.bpm.presentation.databinding.FragmentStudioHomeBinding
import com.team.bpm.presentation.model.StudioMainTabType
import com.team.bpm.presentation.ui.main.studio.recommend.StudioHomeRecommendFragment
import com.team.bpm.presentation.ui.schedule.ScheduleActivity
import com.team.bpm.presentation.util.repeatCallDefaultOnStarted
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StudioHomeFragment :
    BaseFragment<FragmentStudioHomeBinding>(FragmentStudioHomeBinding::inflate) {

    private lateinit var scheduleResultLauncher: ActivityResultLauncher<Intent>

    override val viewModel: StudioHomeViewModel by viewModels()

    private val fragmentList: List<Fragment> by lazy {
        listOf(
            StudioHomeRecommendFragment.newInstance(StudioMainTabType.HOT),
            StudioHomeRecommendFragment.newInstance(StudioMainTabType.REVIEW),
            StudioHomeRecommendFragment.newInstance(StudioMainTabType.OPEN)
        )
    }

    override fun initLayout() {
        bind {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        scheduleResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                viewModel.refreshUserSchedule()
            }

        setupPager()
    }

    override fun setupCollect() {
        repeatCallDefaultOnStarted {
            viewModel.state.collect { state ->
                when (state) {
                    StudioHomeState.Init -> {
                        viewModel.getUserSchedule()
                    }
                    StudioHomeState.UserSchedule -> Unit
                    StudioHomeState.Error -> {
                        // TODO : Error Handling
                    }
                }
            }
        }

        repeatCallDefaultOnStarted {
            viewModel.event.collect { event ->
                when (event) {
                    StudioHomeViewEvent.ClickSearch -> {
                        requireContext().showToast("검색페이지 이동")
                    }
                    StudioHomeViewEvent.ClickSchedule -> {
                        goToSchedule()
                    }
                }
            }
        }
    }

    private fun setupPager() {
        bind {
            pager.adapter = StudioHomePagerAdapter(requireActivity(), fragmentList)

            TabLayoutMediator(tab, pager, false, true) { tab: TabLayout.Tab?, position: Int ->
                val resId: Int = when (position) {
                    0 -> R.string.tab_hot
                    1 -> R.string.tab_review
                    2 -> R.string.tab_open
                    else -> -1
                }

                if (resId != -1) {
                    tab?.setText(resId)
                }
            }.attach()
        }
    }

    private fun goToSchedule() {
//        scheduleResultLauncher.launch(ScheduleActivity.newIntent(requireContext())) // TODO : 스케쥴 아이디를 받도록 수정되었습니다!
    }

    companion object {
        fun newInstance(): StudioHomeFragment {
            return StudioHomeFragment()
        }
    }
}