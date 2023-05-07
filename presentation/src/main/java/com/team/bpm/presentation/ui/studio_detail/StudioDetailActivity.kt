package com.team.bpm.presentation.ui.studio_detail

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.team.bpm.presentation.R
import com.team.bpm.presentation.base.BaseComponentActivityV2
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.*
import com.team.bpm.presentation.compose.theme.*
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

@OptIn(ExperimentalPagerApi::class, ExperimentalGlideComposeApi::class)
@Composable
private fun StudioDetailActivityContent(
    viewModel: StudioDetailViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel)
    val context = LocalContext.current as BaseComponentActivityV2

    LaunchedEffect(Unit) {
        event.invoke(StudioDetailContract.Event.GetStudioDetailData)
    }

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is StudioDetailContract.Effect.ShowToast -> {
                    context.showToast(effect.message)
                }
            }
        }
    }

    with(state) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                ScreenHeader(header = studio?.name ?: "")

                Row(modifier = Modifier.fillMaxWidth()) {
                    Tab(
                        text = "상품 설명",
                        focused = true
                    )

                    Tab(
                        text = "리뷰",
                        focused = false
                    )
                }


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
                                .clickableWithoutRipple { }, // TODO : OnClickHeartIcon
                            painter = painterResource(id = R.drawable.ic_heart_inactive), // TODO : Will be modified when api changed.
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
                        onClick = {}
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

                    Text(
                        text = "정보 수정 제안",
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

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = studio?.address ?: "",
                        fontSize = 16.sp,
                        fontWeight = Medium,
                        letterSpacing = 0.sp
                    )

                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "주소에 관한 설명", // TODO : Request about this
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
                                        .align(Alignment.CenterHorizontally),
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
                            onClick = {}
                        )

                        BPMSpacer(width = 8.dp)

                        RoundedCornerButton(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            text = "길찾기",
                            textColor = MainBlackColor,
                            buttonColor = MainGreenColor,
                            onClick = {}
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
                        text = "마지막 업데이트 : ${studio?.updatedAt ?: ""}",
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

                if (studio?.tagList != null) {
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
                        Column(
                            modifier = Modifier
                                .padding(
                                    top = 14.dp,
                                    start = 14.dp,
                                    end = 14.dp
                                ),
                            verticalArrangement = spacedBy(6.dp)
                        ) {
                            studio.tagList.forEachIndexed { index, tag ->
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
                                    tag = tag,
                                    count = 1 // TODO : request
                                )
                            }
                        }

                        Icon(
                            modifier = Modifier
                                .padding(
                                    top = 6.dp,
                                    bottom = 4.dp
                                )
                                .size(18.dp)
                                .clickableWithoutRipple {}
                                .align(CenterHorizontally),
//                            .rotate(expandIconRotateState.value),
                            painter = painterResource(id = R.drawable.ic_arrow_down),
                            contentDescription = "expandColumnIcon",
                            tint = GrayColor5
                        )
                    }

                    BPMSpacer(height = 25.dp)

                    Divider(
                        thickness = 8.dp,
                        color = GrayColor11
                    )
                }

                ReviewListHeader(
                    onClickOrderByLike = {},
                    onClickOrderByDate = {},
                    onClickWriteReview = {}
                )
            }


            if (isLoading) {
                LoadingScreen()
            }
        }
    }
}

@Composable
private fun RowScope.Tab(
    text: String,
    focused: Boolean
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(42.dp)
    ) {
        Text(
            modifier = Modifier.align(Center),
            text = text,
            fontSize = 15.sp,
            fontWeight = SemiBold,
            letterSpacing = 0.sp
        )

        Divider(
            modifier = Modifier.align(Alignment.BottomCenter),
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