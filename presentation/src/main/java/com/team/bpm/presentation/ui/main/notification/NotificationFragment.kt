package com.team.bpm.presentation.ui.main.notification

import androidx.fragment.app.viewModels
import com.team.bpm.presentation.databinding.FragmentNotificationBinding
import com.team.bpm.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment :
    BaseFragment<FragmentNotificationBinding>(FragmentNotificationBinding::inflate) {

    override val viewModel: NotificationViewModel by viewModels()

    override fun initLayout() = Unit

    companion object {

        fun newInstance(): NotificationFragment {
            return NotificationFragment()
        }
    }
}