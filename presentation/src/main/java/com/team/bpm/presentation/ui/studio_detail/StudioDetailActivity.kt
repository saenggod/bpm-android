package com.team.bpm.presentation.ui.studio_detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.team.bpm.presentation.R
import com.team.bpm.presentation.base.BaseComponentActivity
import com.team.bpm.presentation.base.BaseViewModel
import com.team.bpm.presentation.compose.*
import com.team.bpm.presentation.compose.theme.*
import com.team.bpm.presentation.util.clickableWithoutRipple
import com.team.bpm.presentation.util.clip
import com.team.bpm.presentation.util.dateOnly
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import kotlin.math.roundToInt

@AndroidEntryPoint
class StudioDetailActivity : BaseComponentActivity() {
    override val viewModel: BaseViewModel
        get() = TODO("Not yet implemented")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initComposeUi {
            StudioDetailActivityContent()
        }
    }

    override fun initUi() = Unit

    override fun setupCollect() {

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

@OptIn(ExperimentalPagerApi::class, ExperimentalGlideComposeApi::class)
@Composable
private fun StudioDetailActivityContent(
    viewModel: StudioDetailViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val tabState = remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    val showExpandedKeywordColumn = remember { mutableStateOf(false) }
    val keywordColumnHeightState = animateDpAsState(targetValue = if (showExpandedKeywordColumn.value) 234.dp else 138.dp)
    val expandIconRotateState = animateFloatAsState(targetValue = if (showExpandedKeywordColumn.value) 180f else 0f)
    val reviewTabScrollAmountState = remember { mutableStateOf(0) }
    val reviewHeaderPositionState = remember { mutableStateOf(0f) }
    val screenHeightPx = LocalConfiguration.current.screenHeightDp.dp.toPx()

    tabState.value = if (remember { derivedStateOf { scrollState.value >= reviewTabScrollAmountState.value } }.value) 1 else 0

    with(viewModel.state.collectAsStateWithLifecycle().value) {
        Box(modifier = Modifier.background(color = Color.White)) {
            Column(
                modifier = Modifier
                    .padding(top = 95.dp)
                    .verticalScroll(state = scrollState)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
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
                            count = studio?.filesPath?.size ?: 0,
                            state = horizontalPagerState
                        ) { index ->
                            GlideImage(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(0.85f),
                                model = studio?.filesPath?.get(index) ?: "",
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
                                .align(BottomStart)
                        ) {
                            Text(
                                modifier = Modifier.align(Center),
                                text = "${studio?.filesPath?.size}/${horizontalPagerState.currentPage + 1}",
                                fontWeight = Normal,
                                fontSize = 12.sp,
                                letterSpacing = 2.sp
                            )
                        }
                    }

                    BPMSpacer(height = 20.dp)

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Column {
                            Row(verticalAlignment = CenterVertically) {
                                Text(
                                    text = studio?.firstTag ?: "",
                                    fontWeight = Normal,
                                    fontSize = 12.sp,
                                    letterSpacing = 0.sp
                                )

                                Icon(
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

                            BPMSpacer(height = 8.dp)

                            Text(
                                text = studio?.name ?: "",
                                fontWeight = SemiBold,
                                fontSize = 19.sp,
                                letterSpacing = 0.sp
                            )

                            BPMSpacer(height = 6.dp)

                            Text(
                                text = studio?.content ?: "",
                                fontSize = 13.sp,
                                fontWeight = Normal,
                                letterSpacing = 0.sp,
                                color = GrayColor3
                            )

                            BPMSpacer(height = 8.dp)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = CenterVertically,
                                horizontalArrangement = SpaceBetween
                            ) {
                                Row {
                                    if (studio?.rating != null) {
                                        for (i in 1..5) {
                                            Image(
                                                painter = painterResource(
                                                    id = if (i.toDouble() <= studio.rating!!) R.drawable.ic_star_small_filled
                                                    else if (i.toDouble() > studio.rating!! && studio.rating!! > i - 1) R.drawable.ic_star_small_half
                                                    else R.drawable.ic_star_small_empty
                                                ),
                                                contentDescription = "starIcon"
                                            )

                                            BPMSpacer(width = 2.dp)
                                        }
                                    }

                                    BPMSpacer(width = 8.dp)

                                    Text(
                                        text = "${studio?.rating?.clip() ?: 0}",
                                        fontSize = 14.sp,
                                        fontWeight = Normal,
                                        letterSpacing = 0.sp,
                                        color = GrayColor3
                                    )
                                }

                                Text(
                                    text = "후기 ${studio?.reviewCount ?: 0}개",
                                    fontWeight = Normal,
                                    fontSize = 12.sp,
                                    letterSpacing = 0.sp,
                                    style = TextStyle(textDecoration = TextDecoration.Underline)
                                )
                            }
                        }

                        Image(
                            modifier = Modifier
                                .size(22.dp)
                                .align(TopEnd)
                                .clickableWithoutRipple { viewModel.onClickLike() },
                            painter = painterResource(id = if (isLiked) R.drawable.ic_heart_active else R.drawable.ic_heart_inactive),
                            contentDescription = "heartImage"
                        )
                    }

                    BPMSpacer(height = 36.dp)

                    RoundedCornerButton(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(48.dp),
                        text = "전화 걸기",
                        textColor = Color.White,
                        buttonColor = Color.Black,
                        onClick = { viewModel.onClickCall() }
                    )

                    BPMSpacer(height = 36.dp)

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

                        Text(
                            modifier = Modifier.clickableWithoutRipple { viewModel.onClickInfoEditSuggestion() },
                            text = "정보수정 제안",
                            fontWeight = Medium,
                            fontSize = 14.sp,
                            letterSpacing = 0.sp,
                            color = GrayColor4
                        )
                    }

                    Divider(color = GrayColor8)

                    BPMSpacer(height = 24.dp)

                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = studio?.address ?: "",
                            fontWeight = Medium,
                            fontSize = 16.sp,
                            letterSpacing = 0.sp
                        )

                        BPMSpacer(height = 8.dp)

                        Text(
                            text = "입구가 작아요. 아이스크림 가게 옆 쪽문으로 들어오세요!",
                            fontWeight = Normal,
                            fontSize = 12.sp,
                            letterSpacing = 0.sp,
                            color = GrayColor4
                        )

                        BPMSpacer(height = 12.dp)

                        if (studio?.latitude != null &&
                            studio.longitude != null
                        ) {
                            Box {
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
                                        .align(TopCenter),
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
                                        .clickableWithoutRipple { viewModel.onClickMap() }
                                )
                            }
                        }

                        BPMSpacer(height = 12.dp)

                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutLinedRoundedCornerButton(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                text = "주소 복사",
                                textColor = Color.Black,
                                buttonColor = Color.White,
                                outLineColor = GrayColor6,
                                onClick = { viewModel.onClickCopyAddress() }
                            )

                            BPMSpacer(width = 8.dp)

                            RoundedCornerButton(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                text = "길찾기",
                                textColor = Color.Black,
                                buttonColor = SubGreenColor,
                                onClick = { viewModel.onClickShowCourse() }
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
                            text = "마지막 업데이트 : ${studio?.updatedAt?.dateOnly() ?: ""}",
                            fontWeight = Medium,
                            fontSize = 14.sp,
                            letterSpacing = 0.sp,
                            color = GrayColor5
                        )
                    }

                    Divider(color = GrayColor13)

                    BPMSpacer(height = 24.dp)

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    ) {
                        ConvenienceInfo(
                            type = "전화번호",
                            detail = studio?.phone ?: ""
                        )

                        BPMSpacer(height = 12.dp)

                        ConvenienceInfo(
                            type = "SNS",
                            detail = studio?.sns ?: ""
                        )

                        BPMSpacer(height = 12.dp)

                        ConvenienceInfo(
                            type = "영업시간",
                            detail = studio?.openHours ?: ""
                        )

                        BPMSpacer(height = 12.dp)

                        ConvenienceInfo(
                            type = "가격정보",
                            detail = studio?.price ?: ""
                        )
                    }

                    BPMSpacer(height = 25.dp)

                    Divider(
                        thickness = 8.dp,
                        color = GrayColor11
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .onGloballyPositioned { layoutCoordinates ->
                                val position = layoutCoordinates.positionInParent().y.roundToInt()
                                if (reviewTabScrollAmountState.value != position) {
                                    reviewTabScrollAmountState.value = position
                                }
                            }
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .align(CenterStart),
                            text = "이런 점을 추천해요",
                            fontWeight = SemiBold,
                            fontSize = 16.sp,
                            letterSpacing = 0.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Divider(color = GrayColor13)

                    BPMSpacer(height = 24.dp)

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clip(shape = RoundedCornerShape(12.dp))
                            .fillMaxWidth()
                            .background(color = GrayColor10)
                    ) {
                        BPMSpacer(height = 15.dp)

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .height(keywordColumnHeightState.value)
                        ) {
                            /*
                              repeat(studio) { number ->
                                  when (number + 1) {
                                      1 -> BestKeyword(
                                          number = number + 1,
                                          keyword = "친절해요",
                                          count = 13,
                                          backgroundColor = Color.Black,
                                          textColor = Color.White
                                      )
                                      2 -> BestKeyword(
                                          number = number + 1,
                                          keyword = "제공하는 컨셉이 다양해요",
                                          count = 9,
                                          backgroundColor = GrayColor3,
                                          textColor = Color.White
                                      )
                                      3 -> BestKeyword(
                                          number = number + 1,
                                          keyword = "요청 사항을 잘 들어주세요",
                                          count = 6,
                                          backgroundColor = GrayColor7,
                                          textColor = GrayColor0
                                      )
                                      4 -> BestKeyword(
                                          number = number + 1,
                                          keyword = "주차하기 편해요",
                                          count = 7 - number + 1,
                                          backgroundColor = Color.White,
                                          textColor = Color.Black
                                      )
                                      5 -> BestKeyword(
                                          number = number + 1,
                                          keyword = "시설이 깔끔해요",
                                          count = 7 - number + 1,
                                          backgroundColor = Color.White,
                                          textColor = Color.Black
                                      )
                                  }

                                  if (number != 4) {
                                      BPMSpacer(height = 6.dp)
                                  }
                              }
                              */
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp)
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickableWithoutRipple { showExpandedKeywordColumn.value = !showExpandedKeywordColumn.value }
                                    .align(Center)
                                    .rotate(expandIconRotateState.value),
                                painter = painterResource(id = R.drawable.ic_arrow_down),
                                contentDescription = "expandColumnIcon",
                                tint = GrayColor5
                            )
                        }
                    }

                    BPMSpacer(height = 25.dp)

                    Divider(
                        thickness = 8.dp,
                        color = GrayColor11
                    )
                }

                Column {
                    ReviewListHeader(
                        modifier = Modifier.onGloballyPositioned { coordinates -> reviewHeaderPositionState.value = coordinates.positionInWindow().y },
                        reviewCount = studio?.reviewCount ?: 0,
                        onClickOrderByDate = {},
                        onClickOrderByLike = {},
                        onClickWriteReview = { viewModel.onClickWriteReview() }
                    )

                    if (reviews != null) {
                        Box {
                            if (reviews.isNotEmpty()) {
                                Column {
                                    repeat(5) { index ->
                                        ReviewComposable(
                                            modifier = Modifier.padding(horizontal = 16.dp),
                                            review = reviews[index]
                                        )
                                    }
                                }

                                if (reviews.size > 5) {
                                    Box(
                                        modifier = Modifier
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
                                            .align(BottomCenter)
                                    ) {
                                        RoundedCornerButton(
                                            modifier = Modifier
                                                .padding(
                                                    vertical = 12.dp,
                                                    horizontal = 16.dp
                                                )
                                                .fillMaxWidth()
                                                .height(48.dp)
                                                .align(BottomCenter),
                                            text = "더보기",
                                            textColor = Color.White,
                                            buttonColor = Color.Black,
                                            onClick = { viewModel.onClickMoreReview() }
                                        )
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
                                                .clickable { viewModel.onClickWriteReview() }
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
                    }
                }
            }

            Column {
                ScreenHeader(studio?.name ?: "")

                Row(modifier = Modifier.fillMaxWidth()) {
                    StudioDetailTab(
                        modifier = Modifier.weight(1f),
                        text = "상품설명",
                        tabIndex = 0,
                        tabState = tabState,
                        onClick = { scope.launch { scrollState.animateScrollTo(0) } }
                    )

                    StudioDetailTab(
                        modifier = Modifier.weight(1f),
                        text = "리뷰",
                        tabIndex = 1,
                        tabState = tabState,
                        onClick = { scope.launch { scrollState.animateScrollTo(reviewTabScrollAmountState.value) } }
                    )
                }
            }

            if (remember { derivedStateOf { reviewHeaderPositionState.value >= screenHeightPx } }.value) {
                Box(
                    modifier = Modifier
                        .padding(
                            bottom = 25.dp,
                            end = 16.dp
                        )
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .clip(shape = RoundedCornerShape(50.dp))
                        .border(
                            width = 1.dp,
                            color = GrayColor3,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .height(36.dp)
                        .background(color = Color.White)
                        .align(BottomEnd)
                        .clickable { scope.launch { viewModel.onClickWriteReview() } },
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
        }
    }
}

@Composable
private fun StudioDetailTab(
    modifier: Modifier,
    text: String,
    tabIndex: Int,
    tabState: MutableState<Int>,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .clickable { onClick() }
    ) {
        Text(
            modifier = Modifier.align(Center),
            text = text,
            fontWeight = SemiBold,
            fontSize = 15.sp,
            letterSpacing = 0.sp,
            color = if (tabState.value == tabIndex) Color.Black else GrayColor6
        )

        Divider(
            modifier = Modifier.align(BottomCenter),
            thickness = 2.dp,
            color = if (tabState.value == tabIndex) Color.Black else Color.White
        )
    }
}

@Composable
private fun ConvenienceInfo(
    type: String,
    detail: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = SpaceBetween,
        verticalAlignment = CenterVertically
    ) {
        Text(
            text = type,
            fontWeight = Medium,
            fontSize = 13.sp,
            letterSpacing = 0.sp
        )

        Text(
            text = detail,
            fontWeight = Normal,
            fontSize = 12.sp,
            letterSpacing = 0.sp,
            color = GrayColor3
        )
    }
}

@Composable
private fun BestKeyword(
    number: Int,
    keyword: String,
    count: Int,
    backgroundColor: Color,
    textColor: Color
) {
    Row(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(7.dp))
            .fillMaxWidth()
            .height(42.dp)
            .background(color = backgroundColor),
        horizontalArrangement = SpaceBetween,
        verticalAlignment = CenterVertically
    ) {
        Row(modifier = Modifier.padding(start = 16.dp)) {
            Text(
                text = "${number}위",
                fontFamily = pyeongchang,
                fontWeight = Bold,
                fontSize = 12.sp,
                letterSpacing = 0.sp,
                color = textColor
            )

            BPMSpacer(width = 6.dp)

            Text(
                text = keyword,
                fontFamily = pyeongchang,
                fontWeight = Normal,
                fontSize = 12.sp,
                letterSpacing = 0.sp,
                color = textColor
            )
        }

        Text(
            modifier = Modifier.padding(end = 16.dp),
            text = "+${count}",
            fontWeight = Medium,
            fontSize = 12.sp,
            letterSpacing = 0.sp,
            color = textColor
        )
    }
}