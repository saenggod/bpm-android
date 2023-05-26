package com.team.bpm.domain.usecase.register_studio

import com.team.bpm.domain.model.RegisterStudioWrapper
import com.team.bpm.domain.repository.RegisterStudioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterStudioUseCase @Inject constructor(
    private val registerStudioRepository: RegisterStudioRepository
) {
    suspend operator fun invoke(registerStudioWrapper: RegisterStudioWrapper): Flow<Unit> {
        return registerStudioRepository.sendStudio(registerStudioWrapper)
    }
}