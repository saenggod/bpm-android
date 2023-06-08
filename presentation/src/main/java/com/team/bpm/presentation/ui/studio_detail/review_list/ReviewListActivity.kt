package com.team.bpm.presentation.ui.studio_detail.review_list

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.team.bpm.presentation.base.BaseComponentActivityV2
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.*
import com.team.bpm.presentation.compose.theme.Transparent
import com.team.bpm.presentation.model.BottomSheetButton
import com.team.bpm.presentation.ui.studio_detail.writing_review.WritingReviewActivity
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ReviewListActivity : BaseComponentActivityV2() {

    @Composable
    override fun InitComposeUi() {
        ReviewListActivityContent()
    }

    companion object {
        const val KEY_STUDIO_ID = "studio_id"

        fun newIntent(
            context: Context,
            studioId: Int
        ): Intent {
            return Intent(context, ReviewListActivity::class.java).putExtra(KEY_STUDIO_ID, studioId)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ReviewListActivityContent(
    viewModel: ReviewListViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel)
    val context = getLocalContext()
    val lifecycleEvent = rememberLifecycleEvent()
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
        LaunchedEffect(lifecycleEvent) {
            event.invoke(ReviewListContract.Event.GetReviewList)
        }
    }

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is ReviewListContract.Effect.ShowToast -> {
                    context.showToast(effect.text)
                }

                is ReviewListContract.Effect.RefreshReviewList -> {
                    event.invoke(ReviewListContract.Event.GetReviewList)
                }

                is ReviewListContract.Effect.GoToWriteReview -> {
                    context.startActivity(WritingReviewActivity.newIntent(context, effect.studioId))
                }
            }
        }
    }

    with(state) {
        LaunchedEffect(isBottomSheetShowing) {
            if (isBottomSheetShowing) {
                bottomSheetState.show()
            } else {
                bottomSheetState.hide()
            }
        }

        LaunchedEffect(bottomSheetState.isVisible) {
            if (!bottomSheetState.isVisible) {
                event.invoke(ReviewListContract.Event.OnBottomSheetHide)
            }
        }

        ModalBottomSheetLayout(
            modifier = Modifier
                .imePadding()
                .windowInsetsPadding(insets = WindowInsets.systemBars.only(sides = WindowInsetsSides.Vertical)),
            sheetState = bottomSheetState,
            sheetBackgroundColor = Transparent,
            sheetContent = {
                BPMBottomSheet {
                    bottomSheetButton?.let { bottomSheetButton ->
                        BottomSheetButtonComposable(
                            button = bottomSheetButton,
                            onClick = {
                                when (bottomSheetButton) {
                                    BottomSheetButton.DELETE_POST -> ReviewListContract.Event.OnClickDeleteReview
                                    BottomSheetButton.REPORT_POST -> ReviewListContract.Event.OnClickReportReview
                                    else -> null
                                }?.let { it ->
                                    event.invoke(it)
                                }
                            }
                        )
                    }
                }
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White)
                ) {
                    item {
                        ScreenHeader(header = "리뷰 전체보기")
                    }

                    item {
                        ReviewListHeader(
                            isShowingImageReviewsOnly = isReviewListShowingImageReviewsOnly,
                            isSortedByLike = isReviewListSortedByLike,
                            onClickShowImageReviewsOnlyOrNot = {
                                event.invoke(
                                    if (isReviewListShowingImageReviewsOnly) ReviewListContract.Event.OnClickShowNotOnlyImageReviews
                                    else ReviewListContract.Event.OnClickShowImageReviewsOnly
                                )
                            },
                            onClickSortOrderByLike = { event.invoke(ReviewListContract.Event.OnClickSortByLike) },
                            onClickSortOrderByDate = { event.invoke(ReviewListContract.Event.OnClickSortByDate) },
                            onClickWriteReview = { event.invoke(ReviewListContract.Event.OnClickWriteReview) }
                        )
                    }

                    items(reviewList) { review ->
                        ReviewComposable(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            review = review,
                            onClickLike = { reviewId -> event.invoke(ReviewListContract.Event.OnClickReviewLike(reviewId)) },
                            onClickActionButton = { event.invoke(ReviewListContract.Event.OnClickReviewActionButton(review)) }
                        )
                    }
                }

                if (isLoading) {
                    LoadingScreen()
                }

                if (isReportDialogShowing) {
                    TextFieldDialog(
                        title = "신고 사유를 작성해주세요",
                        onDismissRequest = { event.invoke(ReviewListContract.Event.OnClickDismissReportDialog) },
                        onClickCancel = { event.invoke(ReviewListContract.Event.OnClickDismissReportDialog) },
                        onClickConfirm = { reason -> event.invoke(ReviewListContract.Event.OnClickSendReviewReport(reason)) }
                    )
                }

                if (isNoticeDialogShowing) {
                    noticeDialogContent?.let { noticeDialogContent ->
                        NoticeDialog(
                            title = null,
                            content = noticeDialogContent,
                            onDismissRequest = { event.invoke(ReviewListContract.Event.OnClickDismissNoticeDialog) },
                            onClickConfirm = { event.invoke(ReviewListContract.Event.OnClickDismissNoticeDialog) }
                        )
                    }
                }
            }
        }
    }
}