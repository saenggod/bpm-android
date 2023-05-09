package com.team.bpm.presentation.ui.studio_detail.review_detail

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.team.bpm.presentation.R
import com.team.bpm.presentation.base.BaseComponentActivityV2
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.*
import com.team.bpm.presentation.compose.theme.*
import com.team.bpm.presentation.util.dateOnly
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ReviewDetailActivity : BaseComponentActivityV2() {
    @Composable
    override fun InitComposeUi() {
        ReviewDetailActivityContent()
    }

    companion object {

        const val KEY_BUNDLE = "bundle"
        const val KEY_STUDIO_ID = "studio_id"
        const val KEY_REVIEW_ID = "review_id"

        fun newIntent(context: Context, studioId: Int, reviewId: Int): Intent {
            return Intent(context, ReviewDetailActivity::class.java).apply {
                putExtra(
                    KEY_BUNDLE, bundleOf(
                        KEY_STUDIO_ID to studioId,
                        KEY_REVIEW_ID to reviewId
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalGlideComposeApi::class)
@Composable
private fun ReviewDetailActivityContent(
    viewModel: ReviewDetailViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel)
    val context = LocalContext.current as BaseComponentActivityV2

    LaunchedEffect(Unit) {
        event.invoke(ReviewDetailContract.Event.GetReviewDetail)
    }

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is ReviewDetailContract.Effect.ShowToast -> {
                    context.showToast(effect.text)
                }
            }
        }
    }

    with(state) {
        val scrollState = rememberScrollState()

        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = scrollState)
                    .background(color = GrayColor11)
            ) {
                Column(modifier = Modifier.background(color = Color.White)) {
                    ScreenHeader(header = "리뷰 전체보기")

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(55.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp),
                            horizontalArrangement = SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.align(CenterVertically),
                                verticalAlignment = CenterVertically
                            ) {
                                GlideImage(
                                    modifier = Modifier
                                        .clip(shape = CircleShape)
                                        .size(24.dp),
                                    model = review?.author?.profilePath ?: "",
                                    contentDescription = "profileImage"
                                )

                                BPMSpacer(width = 8.dp)

                                Text(
                                    text = review?.author?.nickname ?: "",
                                    fontWeight = SemiBold,
                                    fontSize = 14.sp,
                                    letterSpacing = 0.sp
                                )
                            }

                            Row(modifier = Modifier.align(CenterVertically)) {
                                Text(
                                    text = review?.createdAt?.dateOnly() ?: "",
                                    fontWeight = SemiBold,
                                    fontSize = 12.sp,
                                    letterSpacing = 0.sp,
                                    color = GrayColor3
                                )

                                BPMSpacer(width = 8.dp)

                                Text(
                                    text = "신고",
                                    fontWeight = Medium,
                                    fontSize = 12.sp,
                                    letterSpacing = 0.sp,
                                    color = GrayColor3
                                )
                            }
                        }
                    }

                    Divider(color = GrayColor9)

                    review?.recommends?.let { recommends ->
                        BPMSpacer(height = 10.dp)

                        FlowRow(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            mainAxisSpacing = 4.dp,
                            crossAxisSpacing = 6.dp
                        ) {
                            recommends.forEach { keyword ->
                                ReviewKeywordChip(text = keyword)
                            }
                        }

                        BPMSpacer(height = 10.dp)

                        Divider(color = GrayColor9)
                    }

                    review?.filesPath?.let { images ->
                        if (images.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(0.85f)
                            ) {
                                val horizontalPagerState = rememberPagerState()

                                HorizontalPager(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(0.85f),
                                    state = horizontalPagerState,
                                    count = images.size
                                ) { index ->
                                    GlideImage(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(0.85f),
                                        model = images[index],
                                        contentDescription = "studioImage",
                                        contentScale = Crop
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .padding(
                                            start = 16.dp,
                                            bottom = 16.dp
                                        )
                                        .clip(RoundedCornerShape(40.dp))
                                        .width(42.dp)
                                        .height(25.dp)
                                        .background(color = FilteredWhiteColor)
                                        .align(Alignment.BottomStart)
                                ) {
                                    Text(
                                        modifier = Modifier.align(Center),
                                        text = "${images.size}/${horizontalPagerState.currentPage + 1}",
                                        fontWeight = Normal,
                                        fontSize = 12.sp,
                                        letterSpacing = 2.sp
                                    )
                                }
                            }
                        }
                    }

                    BPMSpacer(height = 20.dp)

                    review?.rating?.let { rating ->
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Row {
                                for (i in 1..5) {
                                    Image(
                                        modifier = Modifier.size(15.dp),
                                        painter = painterResource(
                                            id = if (i.toDouble() <= rating) R.drawable.ic_star_small_filled
                                            else if (i.toDouble() > rating && rating > i - 1) R.drawable.ic_star_small_half
                                            else R.drawable.ic_star_small_empty
                                        ),
                                        contentDescription = "starIcon"
                                    )
                                }
                            }

                            BPMSpacer(height = 10.dp)
                        }
                    }

                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = review?.content ?: "",
                            fontWeight = Normal,
                            fontSize = 13.sp,
                            letterSpacing = 0.sp,
                            lineHeight = 19.sp
                        )

                        BPMSpacer(height = 20.dp)

                        LikeButton(
                            liked = liked ?: false,
                            likeCount = likeCount ?: 0,
                            onClick = { event.invoke(ReviewDetailContract.Event.OnClickLike) }
                        )

                        BPMSpacer(height = 25.dp)
                    }
                }
            }
        }

        if (isLoading) {
            LoadingScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
}