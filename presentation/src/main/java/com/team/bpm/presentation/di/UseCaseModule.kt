package com.team.bpm.presentation.di

import com.team.bpm.domain.repository.*
import com.team.bpm.domain.usecase.community.GetCommunityCommentListUseCase
import com.team.bpm.domain.usecase.community.GetCommunityDetailUseCase
import com.team.bpm.domain.usecase.community.SendCommunityCommentUseCase
import com.team.bpm.domain.usecase.community.like.DislikeCommunityCommentUseCase
import com.team.bpm.domain.usecase.community.like.DislikeCommunityUseCase
import com.team.bpm.domain.usecase.community.like.LikeCommunityCommentUseCase
import com.team.bpm.domain.usecase.community.like.LikeCommunityUseCase
import com.team.bpm.domain.usecase.question.GetQuestionCommentListUseCase
import com.team.bpm.domain.usecase.question.GetQuestionDetailUseCase
import com.team.bpm.domain.usecase.question.SendQuestionCommentUseCase
import com.team.bpm.domain.usecase.question.like.DislikeQuestionCommentUseCase
import com.team.bpm.domain.usecase.question.like.DislikeQuestionUseCase
import com.team.bpm.domain.usecase.question.like.LikeQuestionCommentUseCase
import com.team.bpm.domain.usecase.question.like.LikeQuestionUseCase
import com.team.bpm.domain.usecase.register_studio.RegisterStudioUseCase
import com.team.bpm.domain.usecase.register_studio.register_location.GetAddressNameUseCase
import com.team.bpm.domain.usecase.review.GetReviewDetailUseCase
import com.team.bpm.domain.usecase.review.GetReviewListUseCase
import com.team.bpm.domain.usecase.review.WriteReviewUseCase
import com.team.bpm.domain.usecase.review.like.DislikeReviewUseCase
import com.team.bpm.domain.usecase.review.like.LikeReviewUseCase
import com.team.bpm.domain.usecase.schedule.GetScheduleUseCase
import com.team.bpm.domain.usecase.schedule.SaveScheduleUseCase
import com.team.bpm.domain.usecase.scrap.ScrapCancelUseCase
import com.team.bpm.domain.usecase.scrap.ScrapUseCase
import com.team.bpm.domain.usecase.search_studio.SearchStudioUseCase
import com.team.bpm.domain.usecase.sign_up.SignUpUseCase
import com.team.bpm.domain.usecase.splash.GetKakaoUserIdUseCase
import com.team.bpm.domain.usecase.splash.SetKakaoUserIdUseCase
import com.team.bpm.domain.usecase.studio_detail.StudioDetailUseCase
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
    fun provideSaveScheduleUseCase(scheduleRepository: ScheduleRepository): SaveScheduleUseCase {
        return SaveScheduleUseCase(scheduleRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetScheduleUseCase(scheduleRepository: ScheduleRepository): GetScheduleUseCase {
        return GetScheduleUseCase(scheduleRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideStudioDetailUseCase(studioDetailRepository: StudioDetailRepository): StudioDetailUseCase {
        return StudioDetailUseCase(studioDetailRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetReviewDetailUseCase(reviewRepository: ReviewRepository): GetReviewDetailUseCase {
        return GetReviewDetailUseCase(reviewRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetReviewListUseCase(reviewRepository: ReviewRepository): GetReviewListUseCase {
        return GetReviewListUseCase(reviewRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideWriteReviewUseCase(writeReviewRepository: WriteReviewRepository): WriteReviewUseCase {
        return WriteReviewUseCase(writeReviewRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideSearchStudioUseCase(searchStudioRepository: SearchStudioRepository): SearchStudioUseCase {
        return SearchStudioUseCase(searchStudioRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideLikeReviewUseCase(reviewRepository: ReviewRepository): LikeReviewUseCase {
        return LikeReviewUseCase(reviewRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideDislikeReviewUseCase(reviewRepository: ReviewRepository): DislikeReviewUseCase {
        return DislikeReviewUseCase(reviewRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideRegisterStudioUseCase(registerStudioRepository: RegisterStudioRepository): RegisterStudioUseCase {
        return RegisterStudioUseCase(registerStudioRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetAddressNameUseCase(registerStudioRepository: RegisterStudioRepository): GetAddressNameUseCase {
        return GetAddressNameUseCase(registerStudioRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetCommunityUseCase(communityRepository: CommunityRepository): GetCommunityDetailUseCase {
        return GetCommunityDetailUseCase(communityRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetCommunityCommentListUseCase(communityRepository: CommunityRepository): GetCommunityCommentListUseCase {
        return GetCommunityCommentListUseCase(communityRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideSendCommunityCommentUseCase(communityRepository: CommunityRepository): SendCommunityCommentUseCase {
        return SendCommunityCommentUseCase(communityRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideLikeCommunityUseCase(communityRepository: CommunityRepository): LikeCommunityUseCase {
        return LikeCommunityUseCase(communityRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideDislikeCommunityUseCase(communityRepository: CommunityRepository): DislikeCommunityUseCase {
        return DislikeCommunityUseCase(communityRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideLikeCommunityCommentUseCase(communityRepository: CommunityRepository): LikeCommunityCommentUseCase {
        return LikeCommunityCommentUseCase(communityRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideDislikeCommunityCommentUseCase(communityRepository: CommunityRepository): DislikeCommunityCommentUseCase {
        return DislikeCommunityCommentUseCase(communityRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideScrapUseCase(scrapRepository: ScrapRepository): ScrapUseCase {
        return ScrapUseCase(scrapRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideScrapCancelUseCase(scrapRepository: ScrapRepository): ScrapCancelUseCase {
        return ScrapCancelUseCase(scrapRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetQuestionDetailUseCase(questionRepository: QuestionRepository): GetQuestionDetailUseCase {
        return GetQuestionDetailUseCase(questionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetQuestionCommentListUseCase(questionRepository: QuestionRepository): GetQuestionCommentListUseCase {
        return GetQuestionCommentListUseCase(questionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideSendQuestionCommentUseCase(questionRepository: QuestionRepository): SendQuestionCommentUseCase {
        return SendQuestionCommentUseCase(questionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideLikeQuestionUseCase(questionRepository: QuestionRepository): LikeQuestionUseCase {
        return LikeQuestionUseCase(questionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideDislikeQuestionUseCase(questionRepository: QuestionRepository): DislikeQuestionUseCase {
        return DislikeQuestionUseCase(questionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideLikeQuestionCommentUseCase(questionRepository: QuestionRepository): LikeQuestionCommentUseCase {
        return LikeQuestionCommentUseCase(questionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideDislikeQuestionCommentUseCase(questionRepository: QuestionRepository): DislikeQuestionCommentUseCase {
        return DislikeQuestionCommentUseCase(questionRepository)
    }
}