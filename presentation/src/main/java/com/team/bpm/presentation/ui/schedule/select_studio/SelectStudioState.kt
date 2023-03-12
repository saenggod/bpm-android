package com.team.bpm.presentation.ui.schedule.select_studio

import com.team.bpm.domain.model.Studio

sealed interface SelectStudioState {
    object Init : SelectStudioState
    object Loading: SelectStudioState
    data class Success(val studioList: List<Studio>) : SelectStudioState
    object Error: SelectStudioState
}