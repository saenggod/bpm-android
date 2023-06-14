package com.team.bpm.presentation.di

import android.location.Geocoder
import com.team.bpm.data.datastore.DataStoreManager
import com.team.bpm.data.network.MainApi
import com.team.bpm.data.repositoryImpl.*
import com.team.bpm.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideUserRepository(dataStoreManager: DataStoreManager): UserRepository {
        return UserRepositoryImpl(dataStoreManager)
    }

    @Singleton
    @Provides
    fun provideMainRepository(mainApi: MainApi): MainRepository {
        return MainRepositoryImpl(mainApi)
    }

    @Singleton
    @Provides
    fun provideSplashRepository(
        dataStoreManager: DataStoreManager,
        mainApi: MainApi
    ): SplashRepository {
        return SplashRepositoryImpl(dataStoreManager, mainApi)
    }

    @Singleton
    @Provides
    fun provideSignUpRepository(mainApi: MainApi): SignUpRepository {
        return SignUpRepositoryImpl(mainApi)
    }

    @Singleton
    @Provides
    fun provideScheduleRepository(mainApi: MainApi): ScheduleRepository {
        return ScheduleRepositoryImpl(mainApi)
    }

    @Singleton
    @Provides
    fun provideStudioDetailRepository(mainApi: MainApi): StudioRepository {
        return StudioRepositoryImpl(mainApi)
    }

    @Singleton
    @Provides
    fun provideReviewRepository(mainApi: MainApi): ReviewRepository {
        return ReviewRepositoryImpl(mainApi)
    }

    @Singleton
    @Provides
    fun provideSearchStudioRepository(mainApi: MainApi): SearchStudioRepository {
        return SearchStudioRepositoryImpl(mainApi)
    }

    @Singleton
    @Provides
    fun provideRegisterStudioRepository(
        mainApi: MainApi,
        geocoder: Geocoder
    ): RegisterStudioRepository {
        return RegisterStudioRepositoryImpl(mainApi, geocoder)
    }

    @Singleton
    @Provides
    fun providePostRepository(mainApi: MainApi): CommunityRepository {
        return CommunityRepositoryImpl(mainApi)
    }

    @Singleton
    @Provides
    fun provideQuestionRepository(mainApi: MainApi): QuestionRepository {
        return QuestionRepositoryImpl(mainApi)
    }

    @Singleton
    @Provides
    fun provideEyeBodyRepository(mainApi: MainApi): EyeBodyRepository {
        return EyeBodyRepositoryImpl(mainApi)
    }

    @Singleton
    @Provides
    fun provideMyPageRepository(dataStoreManager: DataStoreManager): MyPageRepository {
        return MyPageRepositoryImpl(dataStoreManager)
    }
}