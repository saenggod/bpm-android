package com.team.bpm.presentation.ui.studio_detail.review_list

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.team.bpm.presentation.base.BaseComponentActivityV2
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.LoadingScreen
import com.team.bpm.presentation.compose.ReviewComposable
import com.team.bpm.presentation.compose.ReviewListHeader
import com.team.bpm.presentation.compose.ScreenHeader
import com.team.bpm.presentation.compose.rememberLifecycleEvent
import com.team.bpm.presentation.ui.studio_detail.writing_review.WritingReviewActivity
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

        fun newIntent(context: Context, studioId: Int): Intent {
            return Intent(context, ReviewListActivity::class.java).putExtra(KEY_STUDIO_ID, studioId)
        }
    }
}

@Composable
private fun ReviewListActivityContent(
    viewModel: ReviewListViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel)
    val context = LocalContext.current as BaseComponentActivityV2
    val lifecycleEvent = rememberLifecycleEvent()

    if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
        LaunchedEffect(lifecycleEvent) {
            event.invoke(ReviewListContract.Event.GetReviewList)
        }
    }
    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is ReviewListContract.Effect.GoToWriteReview -> {
                    context.startActivity(WritingReviewActivity.newIntent(context, effect.studioId))
                }
            }
        }
    }

    with(state) {
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
                        review = review
                    )
                }
            }

            if (isLoading) {
                LoadingScreen()
            }
        }
    }
}