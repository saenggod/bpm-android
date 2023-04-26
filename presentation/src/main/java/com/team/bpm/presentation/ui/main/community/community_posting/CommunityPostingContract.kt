package com.team.bpm.presentation.ui.main.community.community_posting

import androidx.compose.ui.graphics.ImageBitmap
import com.team.bpm.presentation.base.UnidirectionalViewModel
import java.util.*

interface CommunityPostingContract : UnidirectionalViewModel<CommunityPostingContract.State, CommunityPostingContract.Event, CommunityPostingContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val imageList: LinkedList<ImageBitmap> = LinkedList()
    )

    sealed class Event {

    }

    sealed class Effect {

    }
}