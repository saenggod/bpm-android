package com.team.bpm.presentation.di

import com.team.bpm.data.datastore.DataStoreManager
import com.team.bpm.data.network.MainApi
import com.team.bpm.data.pref.SharedPreferenceManager
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
    fun provideHomeRepository(mainApi: MainApi): HomeRepository {
        return HomeRepositoryImpl(mainApi)
    }

    @Singleton
    @Provides
    fun provideSearchRepository(
        dataStoreManager: DataStoreManager,
        mainApi: MainApi
    ): SearchRepository {
        return SearchRepositoryImpl(dataStoreManager, mainApi)
    }

    @Singleton
    @Provides
    fun provideSplashRepository(
        dataStoreManager: DataStoreManager,
        sharedPreferenceManager: SharedPreferenceManager,
        mainApi: MainApi
    ): SplashRepository {
        return SplashRepositoryImpl(dataStoreManager, sharedPreferenceManager, mainApi)
    }

    @Singleton
    @Provides
    fun provideSignUpRepository(mainApi: MainApi): SignUpRepository {
        return SignUpRepositoryImpl(mainApi)
    }

    @Singleton
    @Provides
    fun provideBodyShapeRepository(mainApi: MainApi): BodyShapeRepository {
        return BodyShapeRepositoryImpl(mainApi)
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
    fun provideMyPageRepository(dataStoreManager: DataStoreManager): MyPageRepository {
        return MyPageRepositoryImpl(dataStoreManager)
    }

    @Singleton
    @Provides
    fun provideUserRepository(
        dataStoreManager: DataStoreManager,
        mainApi: MainApi
    ): UserRepository {
        return UserRepositoryImpl(dataStoreManager, mainApi)
    }
}