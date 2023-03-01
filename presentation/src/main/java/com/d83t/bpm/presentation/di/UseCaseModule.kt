package com.d83t.bpm.presentation.di

import com.d83t.bpm.domain.repository.MakingReservationRepository
import com.d83t.bpm.domain.repository.SignUpRepository
import com.d83t.bpm.domain.repository.SplashRepository
import com.d83t.bpm.domain.usecase.splash.GetKakaoUserIdUseCase
import com.d83t.bpm.domain.usecase.splash.SetKakaoUserIdUseCase
import com.d83t.bpm.domain.usecase.splash.making_reservation.SaveScheduleUseCase
import com.d83t.bpm.domain.usecase.splash.sign_up.SignUpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideGetKakaoUserIdUseCase(splashRepository: SplashRepository): GetKakaoUserIdUseCase {
        return GetKakaoUserIdUseCase(splashRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideSetKakaoUserIdUseCase(splashRepository: SplashRepository): SetKakaoUserIdUseCase {
        return SetKakaoUserIdUseCase(splashRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideSignUpUseCase(signUpRepository: SignUpRepository): SignUpUseCase {
        return SignUpUseCase(signUpRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideSaveScheduleUseCase(makingReservationRepository: MakingReservationRepository): SaveScheduleUseCase {
        return SaveScheduleUseCase(makingReservationRepository)
    }
}