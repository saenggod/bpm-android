package com.team.bpm.presentation.ui.main.studio.detail.writing_review

import android.content.Context
import android.content.Intent
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.flowlayout.FlowRow
import com.team.bpm.presentation.R
import com.team.bpm.presentation.base.BaseComponentActivityV2
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.*
import com.team.bpm.presentation.compose.theme.*
import com.team.bpm.presentation.ui.main.studio.detail.review_detail.ReviewDetailActivity
import com.team.bpm.presentation.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class WritingReviewActivity : BaseComponentActivityV2() {
    @Composable
    override fun InitComposeUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WritingReviewActivityContent()
    }

    companion object {
        const val KEY_STUDIO_ID = "studio_id"

        fun newIntent(
            context: Context,
            studioId: Int
        ): Intent {
            return Intent(context, WritingReviewActivity::class.java).apply {
                putExtra(KEY_STUDIO_ID, studioId)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun WritingReviewActivityContent(
    viewModel: WritingReviewViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel)
    val context = getLocalContext()
    val ratingState = remember { mutableStateOf(0.0) }
    val contentTextState = remember { mutableStateOf("") }
    val imageLauncher = initImageLauncher(
        context = context,
        onSuccess = { uris, images ->
            event.invoke(WritingReviewContract.Event.OnImagesAdded(uris.zip(images)))
        },
        onFailure = {

        }
    )

    LaunchedEffect(Unit) {
        event.invoke(WritingReviewContract.Event.GetStudio)
        event.invoke(WritingReviewContract.Event.GetKeywordList)
    }

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is WritingReviewContract.Effect.ShowToast -> {
                    context.showToast(effect.text)
                }

                is WritingReviewContract.Effect.AddImages -> {
                    imageLauncher.launch(PickVisualMediaRequest())
                }

                is WritingReviewContract.Effect.GoToReviewDetail -> {
                    context.startActivity(ReviewDetailActivity.newIntent(context, effect.studioId, effect.reviewId))
                    context.finish()
                }
            }
        }
    }

    with(state) {
        Box {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .windowInsetsPadding(insets = WindowInsets.systemBars.only(sides = WindowInsetsSides.Vertical))
                    .imePadding()
                    .fillMaxWidth()
                    .verticalScroll(state = scrollState)
                    .background(color = Color.White)
                    .addFocusCleaner(focusManager = LocalFocusManager.current)
            ) {
                ScreenHeader(header = "리뷰 작성하기")

                BPMSpacer(height = 20.dp)

                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(shape = RoundedCornerShape(12.dp))
                        .fillMaxWidth()
                        .height(104.dp)
                        .background(color = GrayColor11)
                        .border(
                            width = 1.dp,
                            color = GrayColor9,
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(all = 16.dp)
                            .fillMaxWidth()
                            .height(74.dp)
                    ) {
                        GlideImage(
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(12.dp))
                                .size(74.dp),
                            model = studio?.filesPath?.get(0) ?: "",
                            contentDescription = "studioProfileImage",
                            contentScale = ContentScale.Crop
                        )

                        BPMSpacer(width = 10.dp)

                        Column(
                            modifier = Modifier.height(74.dp),
                            verticalArrangement = SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = studio?.name ?: "",
                                    fontWeight = Medium,
                                    fontSize = 14.sp,
                                    letterSpacing = 0.sp
                                )

                                BPMSpacer(height = 6.dp)

                                Text(
                                    text = studio?.content ?: "",
                                    fontWeight = Normal,
                                    fontSize = 11.sp,
                                    letterSpacing = 0.sp,
                                    color = GrayColor3
                                )
                            }

                            Row(verticalAlignment = CenterVertically) {
                                repeat(5) {
                                    Icon(
                                        modifier = Modifier.size(12.dp),
                                        painter = painterResource(id = R.drawable.ic_star_small_empty),
                                        contentDescription = "starIcon",
                                        tint = GrayColor6
                                    )

                                    BPMSpacer(width = 2.dp)
                                }

                                BPMSpacer(width = 6.dp)

                                Text(
                                    text = "${studio?.rating?.clip() ?: 0.0}",
                                    fontWeight = Normal,
                                    fontSize = 11.sp,
                                    letterSpacing = 0.sp,
                                    color = GrayColor3
                                )
                            }
                        }
                    }
                }

                BPMSpacer(height = 35.dp)

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "이 정도 만족했어요",
                    fontWeight = SemiBold,
                    fontSize = 16.sp,
                    letterSpacing = 0.sp
                )

                BPMSpacer(height = 8.dp)

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "만족한 정도를 별점으로 보여주세요",
                    fontWeight = Medium,
                    fontSize = 12.sp,
                    letterSpacing = 0.2.sp,
                    color = GrayColor4
                )

                BPMSpacer(height = 14.dp)

                Divider(color = GrayColor13)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                ) {
                    val ratingDraggableAreaWidthState = remember { mutableStateOf(0) }

                    Row(
                        modifier = Modifier.align(Center),
                        horizontalArrangement = spacedBy(8.dp)
                    ) {
                        for (i in 1..5) {
                            Image(
                                modifier = Modifier.size(36.dp),
                                painter = painterResource(
                                    id = if (i.toDouble() <= ratingState.value) R.drawable.ic_star_large_filled
                                    else if (i.toDouble() > ratingState.value && ratingState.value > i - 1) R.drawable.ic_star_large_half
                                    else R.drawable.ic_star_large_empty
                                ),
                                contentDescription = "starIcon"
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .align(Center)
                            .onGloballyPositioned { coordinates ->
                                ratingDraggableAreaWidthState.value = coordinates.size.width
                            }
                            .pointerInput(Unit) {
                                detectDragGestures { change, _ ->
                                    ratingState.value =
                                        if (change.position.x >= ratingDraggableAreaWidthState.value) 5.0
                                        else (if (change.position.x <= 0) 0f
                                        else (change.position.x / ratingDraggableAreaWidthState.value) * 5).toDouble()
                                }
                            }
                    ) {
                        for (i in 1..10) {
                            Box(
                                modifier = Modifier
                                    .width(18.dp)
                                    .height(36.dp)
                                    .clickableWithoutRipple { ratingState.value = i * 0.5 }
                            )

                            if (i % 2 == 0 && i != 10) {
                                BPMSpacer(width = 8.dp)
                            }
                        }
                    }
                }

                Divider(
                    thickness = 8.dp,
                    color = GrayColor11
                )

                BPMSpacer(height = 35.dp)

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "이런 점을 추천해요",
                    fontWeight = SemiBold,
                    fontSize = 16.sp,
                    letterSpacing = 0.sp
                )

                BPMSpacer(height = 8.dp)

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "최대 5개까지 선택가능해요",
                    fontWeight = Medium,
                    fontSize = 12.sp,
                    letterSpacing = 0.2.sp,
                    color = GrayColor4
                )

                BPMSpacer(height = 14.dp)

                Divider(color = GrayColor13)

                BPMSpacer(height = 20.dp)

                FlowRow(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    mainAxisSpacing = 8.dp,
                    crossAxisSpacing = 10.dp
                ) {
                    recommendKeywordMap.forEach { mapItem ->
                        ClickableKeywordChip(
                            keyword = mapItem.key,
                            isChosen = mapItem.value,
                            onClick = { event(WritingReviewContract.Event.OnClickKeywordChip(mapItem.key)) }
                        )
                    }
                }

                BPMSpacer(height = 20.dp)

                Divider(
                    thickness = 8.dp,
                    color = GrayColor11
                )

                BPMSpacer(height = 35.dp)

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "이런 경험이었어요",
                    fontWeight = SemiBold,
                    fontSize = 16.sp,
                    letterSpacing = 0.sp
                )

                BPMSpacer(height = 8.dp)

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "사진과 간단한 후기를 적어주세요 (최대 5장)",
                    fontWeight = Medium,
                    fontSize = 12.sp,
                    letterSpacing = 0.2.sp,
                    color = GrayColor4
                )

                BPMSpacer(height = 14.dp)

                Divider(color = GrayColor13)

                BPMSpacer(height = 15.dp)

                LazyRow(
                    horizontalArrangement = spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    if (imageList.size < 5) {
                        item {
                            ImagePlaceHolder(
                                image = null,
                                onClick = { event.invoke(WritingReviewContract.Event.OnClickImagePlaceHolder) }
                            )
                        }
                    }

                    itemsIndexed(imageList, key = { _, pair ->
                        pair.first
                    }) { index, pair ->
                        ImagePlaceHolder(
                            image = pair.second,
                            onClick = {},
                            onClickRemove = { event.invoke(
                                WritingReviewContract.Event.OnClickRemoveImage(
                                    index
                                )
                            ) }
                        )
                    }
                }

                BPMSpacer(height = 20.dp)

                BPMTextField(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textState = contentTextState,
                    singleLine = false,
                    label = "바디프로필 촬영 경험담을 들려주세요",
                    limit = 300,
                    minHeight = 262.dp,
                    hint = "내용을 입력해주세요."
                )

                BPMSpacer(height = 50.dp)

                RoundedCornerButton(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(48.dp),
                    text = "저장하기",
                    textColor = MainBlackColor,
                    buttonColor = MainGreenColor,
                    onClick = { event.invoke(
                        WritingReviewContract.Event.OnClickSubmit(
                            ratingState.value,
                            contentTextState.value
                        )
                    ) }
                )

                BPMSpacer(height = 12.dp)
            }

            if (isLoading) {
                LoadingScreen()
            }
        }
    }
}