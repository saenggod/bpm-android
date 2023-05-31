package com.team.bpm.presentation.di

import com.team.bpm.domain.repository.*
import com.team.bpm.domain.usecase.community.DeleteCommunityCommentUseCase
import com.team.bpm.domain.usecase.community.DeleteCommunityUseCase
import com.team.bpm.domain.usecase.community.GetCommunityCommentListUseCase
import com.team.bpm.domain.usecase.community.GetCommunityDetailUseCase
import com.team.bpm.domain.usecase.community.ReportCommunityCommentUseCase
import com.team.bpm.domain.usecase.community.ReportCommunityUseCase
import com.team.bpm.domain.usecase.community.SendCommunityCommentUseCase
import com.team.bpm.domain.usecase.community.SendCommunityUseCase
import com.team.bpm.domain.usecase.community.DislikeCommunityCommentUseCase
import com.team.bpm.domain.usecase.community.DislikeCommunityUseCase
import com.team.bpm.domain.usecase.community.LikeCommunityCommentUseCase
import com.team.bpm.domain.usecase.community.LikeCommunityUseCase
import com.team.bpm.domain.usecase.eye_body.SendEyeBodyUseCase
import com.team.bpm.domain.usecase.question.DeleteQuestionCommentUseCase
import com.team.bpm.domain.usecase.question.DeleteQuestionUseCase
import com.team.bpm.domain.usecase.question.GetQuestionCommentListUseCase
import com.team.bpm.domain.usecase.question.GetQuestionDetailUseCase
import com.team.bpm.domain.usecase.question.ReportQuestionCommentUseCase
import com.team.bpm.domain.usecase.question.ReportQuestionUseCase
import com.team.bpm.domain.usecase.question.SendQuestionCommentUseCase
import com.team.bpm.domain.usecase.question.SendQuestionUseCase
import com.team.bpm.domain.usecase.question.DislikeQuestionCommentUseCase
import com.team.bpm.domain.usecase.question.DislikeQuestionUseCase
import com.team.bpm.domain.usecase.question.LikeQuestionCommentUseCase
import com.team.bpm.domain.usecase.question.LikeQuestionUseCase
import com.team.bpm.domain.usecase.register_studio.RegisterStudioUseCase
import com.team.bpm.domain.usecase.register_studio.register_location.GetAddressNameUseCase
import com.team.bpm.domain.usecase.review.DeleteReviewUseCase
import com.team.bpm.domain.usecase.review.GetReviewDetailUseCase
import com.team.bpm.domain.usecase.review.GetReviewListUseCase
import com.team.bpm.domain.usecase.review.ReportReviewUseCase
import com.team.bpm.domain.usecase.review.SendReviewUseCase
import com.team.bpm.domain.usecase.review.DislikeReviewUseCase
import com.team.bpm.domain.usecase.review.LikeReviewUseCase
import com.team.bpm.domain.usecase.schedule.GetScheduleUseCase
import com.team.bpm.domain.usecase.schedule.SaveScheduleUseCase
import com.team.bpm.domain.usecase.studio.ScrapCancelUseCase
import com.team.bpm.domain.usecase.studio.ScrapUseCase
import com.team.bpm.domain.usecase.search_studio.SearchStudioUseCase
import com.team.bpm.domain.usecase.sign_up.SignUpUseCase
import com.team.bpm.domain.usecase.splash.GetKakaoUserIdUseCase
import com.team.bpm.domain.usecase.splash.SetKakaoUserIdUseCase
import com.team.bpm.domain.usecase.studio.GetStudioDetailUseCase
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
    fun provideSearchStudioUseCase(searchStudioRepository: SearchStudioRepository): SearchStudioUseCase {
        return SearchStudioUseCase(searchStudioRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideStudioDetailUseCase(studioRepository: StudioRepository): GetStudioDetailUseCase {
        return GetStudioDetailUseCase(studioRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideScrapUseCase(studioRepository: StudioRepository): ScrapUseCase {
        return ScrapUseCase(studioRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideScrapCancelUseCase(studioRepository: StudioRepository): ScrapCancelUseCase {
        return ScrapCancelUseCase(studioRepository)
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
    fun provideSendReviewUseCase(reviewRepository: ReviewRepository): SendReviewUseCase {
        return SendReviewUseCase(reviewRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideDeleteReviewUseCase(reviewRepository: ReviewRepository): DeleteReviewUseCase {
        return DeleteReviewUseCase(reviewRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetReviewListUseCase(reviewRepository: ReviewRepository): GetReviewListUseCase {
        return GetReviewListUseCase(reviewRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetReviewDetailUseCase(reviewRepository: ReviewRepository): GetReviewDetailUseCase {
        return GetReviewDetailUseCase(reviewRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideReportReviewUseCase(reviewRepository: ReviewRepository): ReportReviewUseCase {
        return ReportReviewUseCase(reviewRepository)
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
    fun provideSendCommunityUseCase(communityRepository: CommunityRepository): SendCommunityUseCase {
        return SendCommunityUseCase(communityRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideDeleteCommunityUseCase(communityRepository: CommunityRepository): DeleteCommunityUseCase {
        return DeleteCommunityUseCase(communityRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetCommunityUseCase(communityRepository: CommunityRepository): GetCommunityDetailUseCase {
        return GetCommunityDetailUseCase(communityRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideReportCommunityUseCase(communityRepository: CommunityRepository): ReportCommunityUseCase {
        return ReportCommunityUseCase(communityRepository)
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
    fun provideSendCommunityCommentUseCase(communityRepository: CommunityRepository): SendCommunityCommentUseCase {
        return SendCommunityCommentUseCase(communityRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideDeleteCommunityCommentUseCase(communityRepository: CommunityRepository): DeleteCommunityCommentUseCase {
        return DeleteCommunityCommentUseCase(communityRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetCommunityCommentListUseCase(communityRepository: CommunityRepository): GetCommunityCommentListUseCase {
        return GetCommunityCommentListUseCase(communityRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideReportCommunityCommentUseCase(communityRepository: CommunityRepository): ReportCommunityCommentUseCase {
        return ReportCommunityCommentUseCase(communityRepository)
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
    fun provideSendQuestionUseCase(questionRepository: QuestionRepository): SendQuestionUseCase {
        return SendQuestionUseCase(questionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideDeleteQuestionUseCase(questionRepository: QuestionRepository): DeleteQuestionUseCase {
        return DeleteQuestionUseCase(questionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetQuestionDetailUseCase(questionRepository: QuestionRepository): GetQuestionDetailUseCase {
        return GetQuestionDetailUseCase(questionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideReportQuestionUseCase(questionRepository: QuestionRepository): ReportQuestionUseCase {
        return ReportQuestionUseCase(questionRepository)
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
    fun provideSendQuestionCommentUseCase(questionRepository: QuestionRepository): SendQuestionCommentUseCase {
        return SendQuestionCommentUseCase(questionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideDeleteQuestionCommentUseCase(questionRepository: QuestionRepository): DeleteQuestionCommentUseCase {
        return DeleteQuestionCommentUseCase(questionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetQuestionCommentListUseCase(questionRepository: QuestionRepository): GetQuestionCommentListUseCase {
        return GetQuestionCommentListUseCase(questionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideReportQuestionCommentUseCase(questionRepository: QuestionRepository): ReportQuestionCommentUseCase {
        return ReportQuestionCommentUseCase(questionRepository)
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

    @Provides
    @ViewModelScoped
    fun provideSendEyeBodyUseCase(eyeBodyRepository: EyeBodyRepository): SendEyeBodyUseCase {
        return SendEyeBodyUseCase(eyeBodyRepository)
    }
}