package com.team.bpm.presentation.ui.main.community.community_detail

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.team.bpm.presentation.R
import com.team.bpm.presentation.base.BaseComponentActivityV2
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.BPMSpacer
import com.team.bpm.presentation.compose.LikeButton
import com.team.bpm.presentation.compose.LoadingScreen
import com.team.bpm.presentation.compose.ScreenHeader
import com.team.bpm.presentation.compose.theme.FilteredWhiteColor
import com.team.bpm.presentation.compose.theme.GrayColor10
import com.team.bpm.presentation.compose.theme.GrayColor13
import com.team.bpm.presentation.compose.theme.GrayColor4
import com.team.bpm.presentation.compose.theme.GrayColor5
import com.team.bpm.presentation.util.dateOnly
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CommunityDetailActivity : BaseComponentActivityV2() {
    @Composable
    override fun InitComposeUi() {
        CommunityDetailActivityContent()
    }

    companion object {
        const val KEY_POST_ID = "post_id"

        fun newIntent(context: Context, postId: Int): Intent {
            return Intent(context, CommunityDetailActivity::class.java).apply {
                putExtra(KEY_POST_ID, postId)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
private fun CommunityDetailActivityContent(
    viewModel: CommunityDetailViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel)
    val context = LocalContext.current as BaseComponentActivityV2

    LaunchedEffect(Unit) {
        event.invoke(CommunityDetailContract.Event.GetCommunityDetail)
    }

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is CommunityDetailContract.Effect.ShowToast -> {

                }
            }
        }
    }

    with(state) {
        Box(modifier = Modifier.fillMaxSize()) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                ScreenHeader(header = "커뮤니티")

                Column(modifier = Modifier.height(56.dp)) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(55.dp),
                        horizontalArrangement = SpaceBetween,
                        verticalAlignment = CenterVertically
                    ) {
                        Row(verticalAlignment = CenterVertically) {
                            GlideImage(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(24.dp),
                                model = post?.author?.profilePath,
                                contentScale = ContentScale.FillBounds,
                                contentDescription = "authorImage"
                            )

                            BPMSpacer(width = 8.dp)

                            Text(
                                text = post?.author?.nickname ?: "",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                letterSpacing = 0.sp
                            )
                        }

                        Row(verticalAlignment = CenterVertically) {
                            Text(
                                text = post?.createdAt?.dateOnly() ?: "",
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                letterSpacing = 0.sp,
                                color = GrayColor5
                            )

                            BPMSpacer(width = 8.dp)

                            Icon(
                                painter = painterResource(id = R.drawable.ic_edit),
                                contentDescription = "editIcon",
                                tint = GrayColor4
                            )
                        }
                    }

                    Divider(
                        thickness = 1.dp,
                        color = GrayColor13
                    )
                }

                post?.filesPath?.let { images ->
                    if (images.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                        ) {
                            val horizontalPagerState = rememberPagerState()

                            HorizontalPager(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f),
                                state = horizontalPagerState,
                                pageCount = images.size
                            ) { index ->
                                GlideImage(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f),
                                    model = images[index],
                                    contentDescription = "postImage",
                                    contentScale = ContentScale.Crop
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
                                    modifier = Modifier.align(Alignment.Center),
                                    text = "${images.size}/${horizontalPagerState.currentPage + 1}",
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    letterSpacing = 2.sp
                                )
                            }
                        }
                    }
                }

                Text(
                    modifier = Modifier
                        .padding(
                            top = 20.dp,
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 16.dp
                        )
                        .fillMaxWidth(),
                    text = post?.content ?: "",
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    letterSpacing = 0.sp
                )

                BPMSpacer(height = 8.dp)

                LikeButton(
                    modifier = Modifier.padding(start = 20.dp),
                    liked = false,
                    likeCount = 0,
                    onClick = {}
                )

                BPMSpacer(height = 28.dp)

                Divider(
                    thickness = 1.dp,
                    color = GrayColor10
                )
            }

            if (isLoading) {
                LoadingScreen()
            }
        }
    }
}