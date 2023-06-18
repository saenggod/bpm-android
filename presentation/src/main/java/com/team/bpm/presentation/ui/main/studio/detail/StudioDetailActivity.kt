package com.team.bpm.presentation.ui.main.studio.detail

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus.Denied
import com.google.accompanist.permissions.PermissionStatus.Granted
import com.google.accompanist.permissions.rememberPermissionState
import com.team.bpm.presentation.R
import com.team.bpm.presentation.base.BaseComponentActivityV2
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.*
import com.team.bpm.presentation.compose.theme.*
import com.team.bpm.presentation.model.BottomSheetButton
import com.team.bpm.presentation.model.StudioDetailTabType
import com.team.bpm.presentation.ui.main.studio.detail.review_list.ReviewListActivity
import com.team.bpm.presentation.ui.main.studio.detail.writing_review.WritingReviewActivity
import com.team.bpm.presentation.util.calculatedFromNow
import com.team.bpm.presentation.util.clickableWithoutRipple
import com.team.bpm.presentation.util.clip
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

@AndroidEntryPoint
class StudioDetailActivity : BaseComponentActivityV2() {
    @Composable
    override fun InitComposeUi() {
        StudioDetailActivityContent()
    }

    companion object {

        const val KEY_STUDIO_ID = "studio_id"

        fun newIntent(context: Context, studioId: Int): Intent {
            return Intent(context, StudioDetailActivity::class.java).apply {
                putExtra(KEY_STUDIO_ID, studioId)
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalGlideComposeApi::class, ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Composable
private fun StudioDetailActivityContent(
    viewModel: StudioDetailViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel)
    val context = getLocalContext()
    val scrollState = rememberScrollState()
    val heightFromTopToInfo = remember { mutableStateOf(0) }
    val callPermissionLauncher = rememberPermissionState(Manifest.permission.CALL_PHONE)
    val scrollPosition = remember { mutableStateOf(0) }
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        event.invoke(StudioDetailContract.Event.GetUserId)
    }

    val lifecycleEvent = rememberLifecycleEvent()

    if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
        LaunchedEffect(lifecycleEvent) {
            event.invoke(StudioDetailContract.Event.GetStudioDetail)
            event.invoke(StudioDetailContract.Event.GetReviewList)
        }
    } else if (lifecycleEvent == Lifecycle.Event.ON_PAUSE) {
        scrollPosition.value = scrollState.value
    }

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is StudioDetailContract.Effect.ShowToast -> {
                    context.showToast(effect.text)
                }

                is StudioDetailContract.Effect.Quit -> {
                    context.finish()
                }

                is StudioDetailContract.Effect.ScrollToInfoTab -> {
                    scrollState.animateScrollTo(0)
                }

                is StudioDetailContract.Effect.ScrollToReviewTab -> {
                    scrollState.animateScrollTo(heightFromTopToInfo.value)
                }

                is StudioDetailContract.Effect.Call -> {
                    when (callPermissionLauncher.status) {
                        is Granted -> {
                            context.startActivity(Intent(Intent.ACTION_CALL).apply {
                                data = Uri.parse("tel:${effect.number}")
                            })
                        }

                        is Denied -> {
                            callPermissionLauncher.launchPermissionRequest()
                        }
                    }
                }

                is StudioDetailContract.Effect.CopyAddressToClipboard -> {
                    (context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(ClipData.newPlainText("address", effect.address))
                }

                is StudioDetailContract.Effect.LaunchNavigationApp -> {
                    val navigationIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=${effect.address}"))
                    context.startActivity(navigationIntent)
                }

                is StudioDetailContract.Effect.RefreshStudioDetail -> {
                    event.invoke(StudioDetailContract.Event.GetStudioDetail)
                }

                is StudioDetailContract.Effect.RefreshReviewList -> {
                    event.invoke(StudioDetailContract.Event.GetReviewList)
                }

                is StudioDetailContract.Effect.GoToWriteReview -> {
                    context.startActivity(WritingReviewActivity.newIntent(context, effect.studioId))
                }

                is StudioDetailContract.Effect.GoToReviewList -> {
                    context.startActivity(ReviewListActivity.newIntent(context, effect.studioId))
                }
            }
        }
    }

    with(state) {
        val screenHeight = remember { mutableStateOf(0f) }
        val layoutHeight = remember { mutableStateOf(0) }
        val tabByScrollState = remember { mutableStateOf(StudioDetailTabType.INFO) }
        screenHeight.value = LocalConfiguration.current.screenHeightDp.dp.toPx()

        LaunchedEffect(tabByScrollState.value) {
            when (tabByScrollState.value) {
                StudioDetailTabType.INFO -> {
                    event.invoke(StudioDetailContract.Event.OnScrolledAtInfoArea)
                }

                StudioDetailTabType.REVIEW -> {
                    event.invoke(StudioDetailContract.Event.OnScrolledAtReviewArea)
                }
            }
        }

        LaunchedEffect(isBottomSheetShowing) {
            if (isBottomSheetShowing) {
                bottomSheetState.show()
            } else {
                bottomSheetState.hide()
            }
        }

        LaunchedEffect(bottomSheetState.isVisible) {
            if (!bottomSheetState.isVisible) {
                event.invoke(StudioDetailContract.Event.OnBottomSheetHide)
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
                                    BottomSheetButton.DELETE_POST -> StudioDetailContract.Event.OnClickDeleteReview
                                    BottomSheetButton.REPORT_POST -> StudioDetailContract.Event.OnClickReportReview
                                    else -> null
                                }?.let {
                                    event.invoke(it)
                                }
                            }
                        )
                    }
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .onGloballyPositioned { layoutHeight.value = it.size.height }
                ) {
                    BPMSpacer(height = 94.dp)

                    Column(modifier = Modifier.onGloballyPositioned { heightFromTopToInfo.value = it.size.height }) {
                        studio?.filesPath?.let { images ->
                            Box {
                                val imagePagerState = rememberPagerState()

                                HorizontalPager(
                                    count = images.size,
                                    state = imagePagerState,
                                ) { index ->
                                    GlideImage(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(0.84f),
                                        model = images[index],
                                        contentDescription = "studioImage",
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
                                        modifier = Modifier.align(Center),
                                        text = "${images.size}/${imagePagerState.currentPage + 1}",
                                        fontWeight = Normal,
                                        fontSize = 12.sp,
                                        letterSpacing = 2.sp
                                    )
                                }
                            }
                        }

                        BPMSpacer(height = 20.dp)

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = SpaceBetween
                            ) {
                                Row(verticalAlignment = CenterVertically) {
                                    Text(
                                        text = studio?.firstTag ?: "",
                                        fontWeight = Normal,
                                        fontSize = 12.sp,
                                        letterSpacing = 0.sp
                                    )

                                    Icon(
                                        modifier = Modifier.padding(all = 2.dp),
                                        painter = painterResource(id = R.drawable.ic_arrow_right),
                                        contentDescription = "tagDepthIcon"
                                    )

                                    Text(
                                        text = studio?.secondTag ?: "",
                                        fontWeight = Normal,
                                        fontSize = 12.sp,
                                        letterSpacing = 0.sp
                                    )
                                }

                                Image(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clickableWithoutRipple { event.invoke(StudioDetailContract.Event.OnClickScrap) },
                                    painter = painterResource(id = if (studio?.scrapped == true) R.drawable.ic_scrap_active else R.drawable.ic_scrap_inactive),
                                    contentDescription = "likeButtonImage"
                                )
                            }

                            Text(
                                modifier = Modifier.padding(top = 12.dp),
                                text = studio?.name ?: "",
                                fontWeight = SemiBold,
                                fontSize = 19.sp,
                                letterSpacing = 0.sp
                            )

                            Text(
                                text = studio?.content ?: "",
                                fontSize = 13.sp,
                                fontWeight = Normal,
                                letterSpacing = 0.sp,
                                color = GrayColor3
                            )

                            Row(
                                modifier = Modifier
                                    .padding(top = 12.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = SpaceBetween
                            ) {
                                studio?.rating?.let { rating ->
                                    Row {
                                        for (i in 1..5) {
                                            Image(
                                                painter = painterResource(
                                                    id = if (i.toDouble() <= rating) R.drawable.ic_star_small_filled
                                                    else if (i.toDouble() > rating && rating > i - 1) R.drawable.ic_star_small_half
                                                    else R.drawable.ic_star_small_empty
                                                ),
                                                contentDescription = "ratingStarImage"
                                            )

                                            BPMSpacer(width = 2.dp)
                                        }

                                        Text(
                                            text = rating.clip(),
                                            fontSize = 14.sp,
                                            fontWeight = Normal,
                                            letterSpacing = 0.sp,
                                            color = GrayColor3
                                        )
                                    }
                                }

                                Text(
                                    modifier = Modifier.clickableWithoutRipple { event.invoke(
                                        StudioDetailContract.Event.OnClickReviewTab
                                    ) },
                                    text = "리뷰 ${studio?.reviewCount ?: 0}개",
                                    fontWeight = Normal,
                                    fontSize = 12.sp,
                                    letterSpacing = 0.sp,
                                    style = TextStyle(textDecoration = TextDecoration.Underline)
                                )
                            }

                            RoundedCornerButton(
                                modifier = Modifier
                                    .padding(top = 30.dp)
                                    .fillMaxWidth()
                                    .height(48.dp),
                                buttonColor = MainBlackColor,
                                textColor = Color.White,
                                text = "전화 걸기",
                                onClick = { studio?.phone?.let { event.invoke(
                                    StudioDetailContract.Event.OnClickCall(
                                        it
                                    )
                                ) } }
                            )
                        }

                        BPMSpacer(height = 25.dp)

                        Divider(
                            thickness = 8.dp,
                            color = GrayColor11
                        )

                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(55.dp),
                            horizontalArrangement = SpaceBetween,
                            verticalAlignment = CenterVertically
                        ) {
                            Text(
                                text = "위치 정보",
                                fontWeight = SemiBold,
                                fontSize = 16.sp,
                                letterSpacing = 0.sp
                            )
                        }

                        Divider(
                            thickness = 1.dp,
                            color = GrayColor13
                        )

                        BPMSpacer(height = 20.dp)

                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Text(
                                text = studio?.address ?: "",
                                fontSize = 16.sp,
                                fontWeight = Medium,
                                letterSpacing = 0.sp
                            )

                            Text(
                                modifier = Modifier.padding(top = 4.dp),
                                text = "주소에 관한 설명",
                                fontSize = 12.sp,
                                fontWeight = Normal,
                                letterSpacing = 0.sp,
                                color = GrayColor4
                            )

                            if (studio?.latitude != null && studio.longitude != null) {
                                Box(modifier = Modifier.padding(top = 12.dp)) {
                                    AndroidView(
                                        modifier = Modifier
                                            .clip(shape = RoundedCornerShape(12.dp))
                                            .fillMaxWidth()
                                            .height(180.dp),
                                        factory = { context ->
                                            MapView(context).apply {
                                                setMapCenterPoint(
                                                    MapPoint.mapPointWithGeoCoord(
                                                        studio.latitude ?: 37.5663,
                                                        studio.longitude ?: 126.9779
                                                    ), false
                                                )
                                            }
                                        }
                                    )

                                    Column(
                                        modifier = Modifier
                                            .height(90.dp)
                                            .align(Alignment.TopCenter),
                                        verticalArrangement = Arrangement.Bottom
                                    ) {
                                        Image(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .align(CenterHorizontally),
                                            painter = painterResource(id = R.drawable.ic_marker),
                                            contentDescription = "mapMarkerIcon"
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp)
                                            .clickableWithoutRipple { }
                                    )
                                }
                            }

                            BPMSpacer(height = 12.dp)

                            Row(modifier = Modifier.fillMaxWidth()) {
                                RoundedCornerButton(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp),
                                    text = "주소 복사",
                                    textColor = MainBlackColor,
                                    buttonColor = Color.White,
                                    borderColor = GrayColor6,
                                    onClick = { studio?.address?.let { event.invoke(
                                        StudioDetailContract.Event.OnClickCopyAddress(it)
                                    ) } }
                                )

                                BPMSpacer(width = 8.dp)

                                RoundedCornerButton(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp),
                                    text = "길찾기",
                                    textColor = MainBlackColor,
                                    buttonColor = MainGreenColor,
                                    onClick = { studio?.address?.let { event.invoke(
                                        StudioDetailContract.Event.OnClickNavigate(it)
                                    ) } }
                                )
                            }
                        }

                        BPMSpacer(height = 25.dp)

                        Divider(
                            thickness = 8.dp,
                            color = GrayColor11
                        )

                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(55.dp),
                            horizontalArrangement = SpaceBetween,
                            verticalAlignment = CenterVertically
                        ) {
                            Text(
                                text = "편의 정보",
                                fontWeight = SemiBold,
                                fontSize = 16.sp,
                                letterSpacing = 0.sp
                            )

                            Text(
                                text = "마지막 업데이트 : ${studio?.updatedAt?.calculatedFromNow() ?: ""}",
                                fontWeight = Medium,
                                fontSize = 14.sp,
                                letterSpacing = 0.sp,
                                color = GrayColor4
                            )
                        }

                        Divider(
                            thickness = 1.dp,
                            color = GrayColor13
                        )

                        BPMSpacer(height = 20.dp)

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            verticalArrangement = spacedBy(12.dp)
                        ) {
                            ConvenienceInformation(title = "전화번호", body = studio?.phone ?: "")
                            ConvenienceInformation(title = "SNS", body = studio?.sns ?: "")
                            ConvenienceInformation(title = "영업시간", body = studio?.openHours ?: "")
                            ConvenienceInformation(title = "가격정보", body = studio?.price ?: "")
                        }

                        BPMSpacer(height = 25.dp)

                        Divider(
                            thickness = 8.dp,
                            color = GrayColor11
                        )

                        if (studio?.topRecommends != null && studio.topRecommends?.isNotEmpty() == true) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth()
                                    .height(55.dp),
                                horizontalArrangement = SpaceBetween,
                                verticalAlignment = CenterVertically
                            ) {
                                Text(
                                    text = "이런 점을 추천해요",
                                    fontWeight = SemiBold,
                                    fontSize = 16.sp,
                                    letterSpacing = 0.sp
                                )
                            }

                            Divider(
                                thickness = 1.dp,
                                color = GrayColor13
                            )

                            BPMSpacer(height = 20.dp)

                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .fillMaxWidth()
                                    .background(color = GrayColor10)
                            ) {
                                val topRecommendListSize = studio.topRecommends!!.size
                                val topRecommendListHeightState = animateDpAsState(
                                    targetValue =
                                    if (isTopRecommendListExpanded) {
                                        (topRecommendListSize * 42 + (topRecommendListSize - 1) * 6).dp
                                    } else {
                                        when (topRecommendListSize) {
                                            1 -> {
                                                42.dp
                                            }

                                            2 -> {
                                                90.dp
                                            }

                                            else -> {
                                                138.dp
                                            }
                                        }
                                    }
                                )

                                Column(
                                    modifier = Modifier
                                        .padding(
                                            top = 14.dp,
                                            start = 14.dp,
                                            end = 14.dp
                                        )
                                        .height(topRecommendListHeightState.value),
                                    verticalArrangement = spacedBy(6.dp)
                                ) {
                                    studio.topRecommends!!.forEachIndexed { index, recommend ->
                                        RecommendTag(
                                            backgroundColor = when (index) {
                                                0 -> MainBlackColor
                                                1 -> GrayColor3
                                                2 -> GrayColor7
                                                else -> Color.White
                                            },
                                            textColor = when (index) {
                                                in 0..1 -> Color.White
                                                else -> MainBlackColor
                                            },
                                            rank = index + 1,
                                            tag = recommend.first,
                                            count = recommend.second
                                        )
                                    }
                                }

                                if (topRecommendListSize > 3) {
                                    Icon(
                                        modifier = Modifier
                                            .padding(
                                                top = 6.dp,
                                                bottom = 4.dp
                                            )
                                            .size(18.dp)
                                            .clickableWithoutRipple {
                                                if (isTopRecommendListExpanded) {
                                                    event.invoke(StudioDetailContract.Event.OnClickCollapseTopRecommendList)
                                                } else {
                                                    event.invoke(StudioDetailContract.Event.OnClickExpandTopRecommendList)
                                                }
                                            }
                                            .align(CenterHorizontally)
                                            .rotate(if (isTopRecommendListExpanded) 180f else 0f),
                                        painter = painterResource(id = R.drawable.ic_arrow_down),
                                        contentDescription = "expandColumnIcon",
                                        tint = GrayColor5
                                    )
                                } else {
                                    BPMSpacer(height = 14.dp)
                                }
                            }

                            BPMSpacer(height = 25.dp)

                            Divider(
                                thickness = 8.dp,
                                color = GrayColor11
                            )
                        }
                    }

                    ReviewListHeader(
                        modifier = Modifier.onGloballyPositioned { tabByScrollState.value = if (it.positionInWindow().y - 300 > screenHeight.value / 2f) StudioDetailTabType.INFO else StudioDetailTabType.REVIEW },
                        isShowingImageReviewsOnly = isReviewListShowingImageReviewsOnly,
                        isSortedByLike = isReviewListSortedByLike,
                        onClickShowImageReviewsOnlyOrNot = {
                            event.invoke(
                                if (isReviewListShowingImageReviewsOnly) StudioDetailContract.Event.OnClickShowNotOnlyImageReviews
                                else StudioDetailContract.Event.OnClickShowImageReviewsOnly
                            )
                        },
                        onClickSortOrderByLike = { event.invoke(StudioDetailContract.Event.OnClickSortByLike) },
                        onClickSortOrderByDate = { event.invoke(StudioDetailContract.Event.OnClickSortByDate) },
                        onClickWriteReview = { event.invoke(StudioDetailContract.Event.OnClickWriteReview) }
                    )

                    Box {
                        reviewList.let { reviewList ->
                            if (reviewList.isNotEmpty()) {
                                Box {
                                    Column {
                                        reviewList.filter {
                                            if (isReviewListShowingImageReviewsOnly) {
                                                it.filesPath?.isNotEmpty() == true
                                            } else {
                                                true
                                            }
                                        }.sortedByDescending {
                                            if (isReviewListSortedByLike) {
                                                it.likeCount
                                            } else {
                                                null
                                            }
                                        }.sortedByDescending {
                                            if (!isReviewListSortedByLike) {
                                                it.createdAt
                                            } else {
                                                null
                                            }
                                        }.forEachIndexed { index, review ->
                                            if (index < 5) {
                                                ReviewComposable(
                                                    review = review,
                                                    onClickLike = { reviewId -> event.invoke(
                                                        StudioDetailContract.Event.OnClickReviewLikeButton(
                                                            reviewId
                                                        )
                                                    ) },
                                                    onClickActionButton = { event.invoke(
                                                        StudioDetailContract.Event.OnClickReviewActionButton(
                                                            review
                                                        )
                                                    ) }
                                                )
                                            }
                                        }
                                    }

                                    if (reviewList.size > 5) {
                                        Box(
                                            modifier = Modifier
                                                .align(BottomCenter)
                                                .fillMaxWidth()
                                                .height(250.dp)
                                                .background(
                                                    brush = Brush.verticalGradient(
                                                        listOf(
                                                            Color(0X53FFFFFF),
                                                            Color(0X73FFFFFF),
                                                            Color(0XA2FFFFFF),
                                                            Color(0XD9FFFFFF),
                                                            Color(0XF2FFFFFF),
                                                        )
                                                    )
                                                )
                                        ) {
                                            RoundedCornerButton(
                                                modifier = Modifier
                                                    .padding(
                                                        horizontal = 16.dp,
                                                        vertical = 12.dp
                                                    )
                                                    .fillMaxWidth()
                                                    .height(48.dp)
                                                    .align(BottomCenter),
                                                text = "더보기",
                                                textColor = Color.White,
                                                buttonColor = MainBlackColor,
                                                onClick = { event.invoke(StudioDetailContract.Event.OnClickMoreReviews) }
                                            )
                                        }
                                    }
                                }
                            } else {
                                Box(modifier = Modifier.size(360.dp)) {
                                    Column(
                                        modifier = Modifier.align(Center),
                                        horizontalAlignment = CenterHorizontally
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.shoulder_man),
                                            contentDescription = "shoulderManImage"
                                        )

                                        BPMSpacer(height = 10.dp)

                                        Text(
                                            text = "아직 등록된 리뷰가 없어요\n첫 번째 리뷰를 남겨주세요",
                                            fontWeight = Medium,
                                            fontSize = 12.sp,
                                            letterSpacing = 0.sp,
                                            color = GrayColor5
                                        )

                                        BPMSpacer(height = 18.dp)

                                        Box(
                                            modifier = Modifier
                                                .clip(shape = RoundedCornerShape(50.dp))
                                                .width(130.dp)
                                                .height(40.dp)
                                                .background(color = MainGreenColor)
                                                .clickable { event.invoke(StudioDetailContract.Event.OnClickWriteReview) }
                                        ) {
                                            Text(
                                                modifier = Modifier.align(Center),
                                                text = "리뷰 등록하기",
                                                fontWeight = SemiBold,
                                                fontSize = 12.sp,
                                                letterSpacing = 0.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        if (isReviewListLoading) {
                            LoadingBlock()
                        }
                    }
                }

                Column {
                    ScreenHeader(header = studio?.name ?: "")

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                    ) {
                        Tab(
                            text = "상품 설명",
                            focused = focusedTab == StudioDetailTabType.INFO,
                            onClick = { event.invoke(StudioDetailContract.Event.OnClickInfoTab) }
                        )

                        Tab(
                            text = "리뷰",
                            focused = focusedTab == StudioDetailTabType.REVIEW,
                            onClick = { event.invoke(StudioDetailContract.Event.OnClickReviewTab) }
                        )
                    }
                }

                if (tabByScrollState.value == StudioDetailTabType.INFO) {
                    Box(
                        modifier = Modifier
                            .padding(
                                bottom = 25.dp,
                                end = 16.dp
                            )
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(500.dp)
                            )
                            .clip(shape = RoundedCornerShape(500.dp))
                            .border(
                                width = 1.dp,
                                color = GrayColor3,
                                shape = RoundedCornerShape(500.dp)
                            )
                            .height(36.dp)
                            .background(color = Color.White)
                            .align(Alignment.BottomEnd)
                            .clickable { event.invoke(StudioDetailContract.Event.OnClickReviewTab) },
                    ) {
                        Row(
                            modifier = Modifier.align(Center),
                            horizontalArrangement = spacedBy(4.dp),
                            verticalAlignment = CenterVertically
                        ) {
                            BPMSpacer(width = 14.dp)

                            Text(
                                modifier = Modifier.padding(vertical = 12.dp),
                                text = "리뷰 바로 작성하기",
                                fontWeight = SemiBold,
                                fontSize = 12.sp,
                                letterSpacing = 0.sp,
                                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                            )

                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_down_small),
                                contentDescription = "downToReviewIcon",
                                tint = GrayColor6
                            )

                            BPMSpacer(width = 14.dp)
                        }
                    }
                }

                if (isLoading) {
                    LoadingScreen()
                }

                if (isReportDialogShowing) {
                    val dialogFocusRequester = remember { FocusRequester() }

                    TextFieldDialog(
                        title = "신고 사유를 작성해주세요",
                        focusRequester = dialogFocusRequester,
                        onDismissRequest = { event.invoke(StudioDetailContract.Event.OnClickDismissReportDialog) },
                        onClickCancel = { event.invoke(StudioDetailContract.Event.OnClickDismissReportDialog) },
                        onClickConfirm = { reason -> event.invoke(
                            StudioDetailContract.Event.OnClickSendReviewReport(
                                reason
                            )
                        ) }
                    )

                    LaunchedEffect(Unit) {
                        focusManager.clearFocus()
                        dialogFocusRequester.requestFocus()
                    }
                }

                if (isNoticeDialogShowing) {
                    NoticeDialog(
                        title = null,
                        content = noticeDialogContent,
                        onDismissRequest = { event.invoke(StudioDetailContract.Event.OnClickDismissNoticeDialog) }
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.Tab(
    text: String,
    focused: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(42.dp)
            .clickable { onClick() }
    ) {
        Text(
            modifier = Modifier.align(Center),
            text = text,
            fontSize = 15.sp,
            fontWeight = SemiBold,
            letterSpacing = 0.sp
        )

        Divider(
            modifier = Modifier.align(BottomCenter),
            thickness = 2.dp,
            color = if (focused) MainBlackColor else Color.White
        )
    }
}

@Composable
private fun ConvenienceInformation(
    title: String,
    body: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = SpaceBetween
    ) {
        Text(
            text = title,
            fontWeight = Medium,
            fontSize = 13.sp,
            letterSpacing = 0.sp
        )

        Text(
            text = body,
            fontWeight = Normal,
            fontSize = 12.sp,
            letterSpacing = 0.sp,
            color = GrayColor3
        )
    }
}

@Composable
private fun RecommendTag(
    backgroundColor: Color,
    textColor: Color,
    rank: Int,
    tag: String,
    count: Int
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(7.dp))
            .fillMaxWidth()
            .height(42.dp)
            .background(color = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .align(Center)
                .padding(horizontal = 17.dp)
                .fillMaxWidth(),
            verticalAlignment = CenterVertically,
            horizontalArrangement = SpaceBetween
        ) {
            Row {
                Text(
                    text = "${rank}위",
                    fontWeight = Bold,
                    fontSize = 12.sp,
                    letterSpacing = 0.sp,
                    color = textColor
                )

                BPMSpacer(width = 6.dp)

                Text(
                    text = tag,
                    fontFamily = pyeongchang,
                    fontWeight = Normal,
                    fontSize = 12.sp,
                    letterSpacing = 0.sp,
                    color = textColor
                )
            }

            Text(
                text = "+${count}",
                fontWeight = Medium,
                fontSize = 12.sp,
                letterSpacing = 0.sp,
                color = textColor
            )
        }
    }
}