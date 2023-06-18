package com.team.bpm.presentation.ui.main.studio

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.PagerSnapHelper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.team.bpm.presentation.R
import com.team.bpm.presentation.base.BaseFragment
import com.team.bpm.presentation.databinding.FragmentStudioHomeBinding
import com.team.bpm.presentation.model.MainBanner
import com.team.bpm.presentation.model.StudioMainTabType
import com.team.bpm.presentation.ui.main.MainActivity
import com.team.bpm.presentation.ui.main.MainViewModel
import com.team.bpm.presentation.ui.main.eyebody.posting.EyeBodyPostingActivity
import com.team.bpm.presentation.ui.main.studio.banner.BannerListAdapter
import com.team.bpm.presentation.ui.main.studio.recommend.StudioHomeRecommendFragment
import com.team.bpm.presentation.util.BasePagerAdapter
import com.team.bpm.presentation.util.BannerPagerIndicatorDecoration
import com.team.bpm.presentation.util.repeatCallDefaultOnStarted
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StudioHomeFragment :
    BaseFragment<FragmentStudioHomeBinding>(FragmentStudioHomeBinding::inflate) {

    private lateinit var scheduleResultLauncher: ActivityResultLauncher<Intent>

    private val activityViewModel: MainViewModel by activityViewModels()

    override val viewModel: StudioHomeViewModel by viewModels()

    private val fragmentList: List<Fragment> by lazy {
        listOf(
            StudioHomeRecommendFragment.newInstance(StudioMainTabType.POPULAR),
            StudioHomeRecommendFragment.newInstance(StudioMainTabType.REVIEW),
            StudioHomeRecommendFragment.newInstance(StudioMainTabType.NEW)
        )
    }

    // TODO : 추후에 아키텍쳐 MVI로 변경 시 이동 필요
    private val bannerAdapter = BannerListAdapter {
        when (MainBanner.valueOf(it)) {
            MainBanner.REGISTER -> {
                goToRegisterStore()
            }
            MainBanner.WRITE -> {
                goToWriteEyebody()
            }
            MainBanner.LOUNGE -> {
                goToLounge()
            }
        }
    }

    private val snapHelper = PagerSnapHelper()

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
        setupBanner()
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
            pager.adapter = BasePagerAdapter(requireActivity(), fragmentList)

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

    private fun setupBanner() {
        bind {
            bannerList.adapter = bannerAdapter
            bannerAdapter.submitList(MainBanner.values().toList())
            snapHelper.attachToRecyclerView(bannerList)
            bannerList.addItemDecoration(BannerPagerIndicatorDecoration())

            // TODO : 무한스크롤 하려면 해야 함
//
//            bannerList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                    super.onScrollStateChanged(recyclerView, newState)
////                    isPageScrolled = (newState == RecyclerView.SCROLL_STATE_DRAGGING)
//
//                    when (newState) {
//                        RecyclerView.SCROLL_STATE_DRAGGING -> {
////                            stopAutoScroll()
//                        }
//                        RecyclerView.SCROLL_STATE_IDLE -> {
////                            startAutoScroll()
//                        }
//                    }
//                }
//
//                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    super.onScrolled(recyclerView, dx, dy)
//                    val centerView: View = snapHelper.findSnapView(recyclerView.layoutManager) ?: return
//                    val pos: Int = recyclerView.layoutManager?.getPosition(centerView) ?: return
//
////                    viewModel.setBannerSelectedPosition(pos)
//
//                    val firstVisiblePosition = (recyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition() ?: 0
//                    val lastVisiblePosition = (recyclerView.layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition() ?: 0
//                    val realListSize = MainBanner.values().size
//
//                    if (firstVisiblePosition >= realListSize + 1) {
//                        recyclerView.layoutManager?.scrollToPosition(1)
//                    } else if (lastVisiblePosition <= 0) {
//                        recyclerView.layoutManager?.scrollToPosition(realListSize)
//                    }
//                }
//            })
        }
    }

    private fun goToSchedule() {
//        scheduleResultLauncher.launch(ScheduleActivity.newIntent(requireContext())) // TODO : 스케쥴 아이디를 받도록 수정되었습니다!
    }

    private fun goToRegisterStore() {
        // TODO : 구글폼 주소 필요
    }

    private fun goToWriteEyebody() {
        // 눈바디 기록하기 페이지 이동
        startActivity(EyeBodyPostingActivity.newIntent(requireContext()))
    }

    private fun goToLounge() {
        // 라운지로 이동
        activityViewModel.moveToOtherTab(MainActivity.TAB_LOUNGE)
    }

    companion object {

        fun newInstance(): StudioHomeFragment {
            return StudioHomeFragment()
        }
    }
}