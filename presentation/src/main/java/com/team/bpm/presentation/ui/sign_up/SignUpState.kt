package com.team.bpm.presentation.ui.sign_up

import com.team.bpm.domain.model.UserInfo
import com.team.bpm.presentation.util.ComposeUiState

interface SignUpState : ComposeUiState {
    object Init : SignUpState

    object Loading : SignUpState

    data class SignUpSuccess(val userInfo: UserInfo) : SignUpState

    object Error : SignUpState
}