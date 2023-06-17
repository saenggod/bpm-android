package com.team.bpm.presentation.di

import com.team.bpm.domain.repository.*
import com.team.bpm.domain.usecase.community.*
import com.team.bpm.domain.usecase.eye_body.WriteEyeBodyUseCase
import com.team.bpm.domain.usecase.review.GetKeywordListUseCase
import com.team.bpm.domain.usecase.mypage.GetMainTabIndexUseCase
import com.team.bpm.domain.usecase.mypage.SetMainTabIndexUseCase
import com.team.bpm.domain.usecase.question.*
import com.team.bpm.domain.usecase.register_studio.RegisterStudioUseCase
import com.team.bpm.domain.usecase.register_studio.register_location.GetAddressNameUseCase
import com.team.bpm.domain.usecase.review.*
import com.team.bpm.domain.usecase.schedule.EditScheduleUseCase
import com.team.bpm.domain.usecase.schedule.GetScheduleUseCase
import com.team.bpm.domain.usecase.schedule.MakeScheduleUseCase
import com.team.bpm.domain.usecase.search.GetFilteredStudioListUseCase
import com.team.bpm.domain.usecase.search.GetRecentSearchListUseCase
import com.team.bpm.domain.usecase.search.SetRecentSearchListUseCase
import com.team.bpm.domain.usecase.search_studio.SearchStudioUseCase
import com.team.bpm.domain.usecase.sign_up.SignUpUseCase
import com.team.bpm.domain.usecase.splash.GetKakaoIdUseCase
import com.team.bpm.domain.usecase.splash.SetKakaoIdUseCase
import com.team.bpm.domain.usecase.studio.GetMyScrapListUseCase
import com.team.bpm.domain.usecase.studio.GetStudioDetailUseCase
import com.team.bpm.domain.usecase.studio.ScrapCancelUseCase
import com.team.bpm.domain.usecase.studio.ScrapUseCase
import com.team.bpm.domain.usecase.user.GetUserIdUseCase
import com.team.bpm.domain.usecase.user.SetUserIdUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    /* 계정 */

    @Provides
    @ViewModelScoped
    fun provideGetKakaoIdUseCase(splashRepository: SplashRepository): GetKakaoIdUseCase {
        return GetKakaoIdUseCase(splashRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideSetKakaoIdUseCase(splashRepository: SplashRepository): SetKakaoIdUseCase {
        return SetKakaoIdUseCase(splashRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideSignUpUseCase(signUpRepository: SignUpRepository): SignUpUseCase {
        return SignUpUseCase(signUpRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetUserIdUseCase(userRepository: UserRepository): GetUserIdUseCase {
        return GetUserIdUseCase(userRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideSetUserIdUseCase(userRepository: UserRepository) : SetUserIdUseCase {
        return SetUserIdUseCase(userRepository)
    }

    /* 스튜디오 */

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
    fun provideGetMyScrapListUseCase(studioRepository: StudioRepository): GetMyScrapListUseCase {
        return GetMyScrapListUseCase(studioRepository)
    }

    /* 일정 */

    @Provides
    @ViewModelScoped
    fun provideMakeScheduleUseCase(scheduleRepository: ScheduleRepository): MakeScheduleUseCase {
        return MakeScheduleUseCase(scheduleRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideEditScheduleUseCase(scheduleRepository: ScheduleRepository): EditScheduleUseCase {
        return EditScheduleUseCase(scheduleRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetScheduleUseCase(scheduleRepository: ScheduleRepository): GetScheduleUseCase {
        return GetScheduleUseCase(scheduleRepository)
    }

    /* 리뷰 */

    @Provides
    @ViewModelScoped
    fun provideGetKeywordUseCase(reviewRepository: ReviewRepository): GetKeywordListUseCase {
        return GetKeywordListUseCase(reviewRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideWriteReviewUseCase(reviewRepository: ReviewRepository): WriteReviewUseCase {
        return WriteReviewUseCase(reviewRepository)
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

    /* 커뮤니티 */

    @Provides
    @ViewModelScoped
    fun provideGetCommunityListUseCase(communityRepository: CommunityRepository): GetCommunityListUseCase {
        return GetCommunityListUseCase(communityRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideWriteCommunityUseCase(communityRepository: CommunityRepository): WriteCommunityUseCase {
        return WriteCommunityUseCase(communityRepository)
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
    fun provideWriteCommunityCommentUseCase(communityRepository: CommunityRepository): WriteCommunityCommentUseCase {
        return WriteCommunityCommentUseCase(communityRepository)
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

    /* 질문 */

    @Provides
    @ViewModelScoped
    fun provideGetQuestionListUseCase(questionRepository: QuestionRepository): GetQuestionListUseCase {
        return GetQuestionListUseCase(questionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideWriteQuestionUseCase(questionRepository: QuestionRepository): WriteQuestionUseCase {
        return WriteQuestionUseCase(questionRepository)
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
    fun provideWriteQuestionCommentUseCase(questionRepository: QuestionRepository): WriteQuestionCommentUseCase {
        return WriteQuestionCommentUseCase(questionRepository)
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

    // 마이페이지 - 내 질문 리스트
    @Provides
    @ViewModelScoped
    fun provideMyGetQuestionListUseCase(questionRepository: QuestionRepository): GetMyQuestionListUseCase {
        return GetMyQuestionListUseCase(questionRepository)
    }

    /* 눈바디 */

    @Provides
    @ViewModelScoped
    fun provideWriteEyeBodyUseCase(eyeBodyRepository: EyeBodyRepository): WriteEyeBodyUseCase {
        return WriteEyeBodyUseCase(eyeBodyRepository)
    }

    /* 마이페이지 */

    @Provides
    @ViewModelScoped
    fun provideSetMainTabIndexUseCase(myPageRepository: MyPageRepository): SetMainTabIndexUseCase {
        return SetMainTabIndexUseCase(myPageRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetMainTabIndexUseCase(myPageRepository: MyPageRepository): GetMainTabIndexUseCase {
        return GetMainTabIndexUseCase(myPageRepository)
    }

    /* 검색 */

    @Provides
    @ViewModelScoped
    fun provideGetRecentSearchListUseCase(searchRepository: SearchRepository): GetRecentSearchListUseCase {
        return GetRecentSearchListUseCase(searchRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideSetRecentSearchListUseCase(searchRepository: SearchRepository): SetRecentSearchListUseCase {
        return SetRecentSearchListUseCase(searchRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetFilteredStudioListUseCase(searchRepository: SearchRepository): GetFilteredStudioListUseCase {
        return GetFilteredStudioListUseCase(searchRepository)
    }
}