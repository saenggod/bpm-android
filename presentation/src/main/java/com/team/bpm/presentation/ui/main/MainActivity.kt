package com.team.bpm.presentation.ui.main

import android.content.Context
import android.content.Intent
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.fragment.app.commitNow
import com.team.bpm.presentation.R
import com.team.bpm.presentation.base.BaseActivity
import com.team.bpm.presentation.databinding.ActivityMainBinding
import com.team.bpm.presentation.ui.main.add.MainAddBottomSheet
import com.team.bpm.presentation.ui.main.bodyshape.BodyShapeFragment
import com.team.bpm.presentation.ui.main.lounge.LoungeFragment
import com.team.bpm.presentation.ui.main.mypage.MyPageFragment
import com.team.bpm.presentation.ui.main.studio.StudioHomeFragment
import com.team.bpm.presentation.util.repeatCallDefaultOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override val viewModel: MainViewModel by viewModels()

    private val onBackPressedCallBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finishAffinity()
        }
    }

    override fun initLayout() {
        bind {
            vm = viewModel
            lifecycleOwner = this@MainActivity
        }

        onBackPressedDispatcher.addCallback(onBackPressedCallBack)
    }

    override fun setupCollect() {
        repeatCallDefaultOnStarted {
            viewModel.state.collect { state ->
                when (state) {
                    MainState.Init -> {
                        viewModel.getMainTabIndex()
                    }

                    is MainState.Tab -> {
                        setUpNavigation(state.startIndex)
                    }
                }
            }
        }

        repeatCallDefaultOnStarted {
            viewModel.event.collect { event ->
                when (event) {
                    MainViewEvent.Add -> {
                        showAddBottomSheet()
                    }
                    is MainViewEvent.MoveTab -> {
                        changeFragment(findFragmentId(event.tabIndex))
                    }
                }
            }
        }
    }

    private fun setUpNavigation(startTabIndex: Int = -1) {
        bind {
            if (supportFragmentManager.primaryNavigationFragment == null && startTabIndex != -1) {
                mainTab.selectedItemId = findFragmentId(startTabIndex)
                changeFragment(findFragmentId(startTabIndex))
            }

            mainTab.setOnItemSelectedListener {
                changeFragment(it.itemId)
                return@setOnItemSelectedListener true
            }
        }
    }

    private fun showAddBottomSheet() {
        MainAddBottomSheet().show(supportFragmentManager, MainAddBottomSheet::class.simpleName)
    }

    private fun findFragmentId(startTabIndex: Int): Int {
        return when (startTabIndex) {
            TAB_STUDIO -> {
                R.id.nav_studio
            }
            TAB_LOUNGE -> {
                R.id.nav_lounge
            }
            TAB_BODY_SHAPE -> {
                R.id.nav_body_shape
            }
            TAB_MYPAGE -> {
                R.id.nav_mypage
            }
            else -> {
                R.id.nav_studio
            }
        }
    }

    private fun changeFragment(fragmentId: Int? = null) {
        val fragment = when (fragmentId) {
            R.id.nav_body_shape -> {
                BodyShapeFragment.newInstance()
            }
            R.id.nav_studio -> {
                StudioHomeFragment.newInstance()
            }
            R.id.nav_lounge -> {
                LoungeFragment.newInstance()
            }
            R.id.nav_mypage -> {
                MyPageFragment.newInstance()
            }
            else -> {
                StudioHomeFragment.newInstance()
            }
        }

        supportFragmentManager.commitNow(true) {
            val currentFragment = supportFragmentManager.primaryNavigationFragment
            if (currentFragment != null) {
                hide(currentFragment)
            }

            var newFragment =
                supportFragmentManager.findFragmentByTag(fragment::class.java.simpleName)
            if (newFragment == null) {
                newFragment = fragment
                add(R.id.container, newFragment, fragment::class.java.simpleName)
            } else {
                show(newFragment)
            }

            setPrimaryNavigationFragment(newFragment)
            setReorderingAllowed(true)
        }
    }

    companion object {

        const val TAB_STUDIO = 0
        const val TAB_LOUNGE = 1
        const val TAB_BODY_SHAPE = 2
        const val TAB_MYPAGE = 3

        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }

    }
}