package com.team.bpm.presentation.compose

import android.content.Context
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.model.Keyword
import com.team.bpm.domain.model.Review
import com.team.bpm.domain.model.Studio
import com.team.bpm.presentation.R
import com.team.bpm.presentation.base.BaseComponentActivityV2
import com.team.bpm.presentation.compose.theme.*
import com.team.bpm.presentation.model.BottomSheetButton
import com.team.bpm.presentation.ui.studio_detail.review_detail.ReviewDetailActivity
import com.team.bpm.presentation.util.calculatedFromNow
import com.team.bpm.presentation.util.clickableWithoutRipple
import com.team.bpm.presentation.util.clip
import com.team.bpm.presentation.util.convertUriToBitmap


@Composable
fun ScreenHeader(
    header: String,
    actionBlock: @Composable (() -> Unit)? = null
) {
    val context = LocalContext.current as ComponentActivity

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Icon(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(26.dp)
                    .align(CenterStart)
                    .clickableWithoutRipple { context.finish() },
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = ""
            )

            Text(
                modifier = Modifier.align(Center),
                text = header,
                fontWeight = SemiBold,
                fontSize = 18.sp,
                letterSpacing = 0.sp
            )

            if (actionBlock != null) {
                Box(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .align(CenterEnd)
                ) {
                    actionBlock()
                }
            }
        }

        Divider(
            thickness = 1.dp,
            color = GrayColor8
        )
    }
}

@Composable
fun TextFieldColorProvider(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalTextSelectionColors.provides(textSelectionColor())) {
        content()
    }
}

@Composable
fun BPMSpacer(
    height: Dp = 0.dp,
    width: Dp = 0.dp
) {
    Spacer(
        modifier = Modifier
            .height(height)
            .width(width)
    )
}

@Composable
inline fun RoundedCornerButton(
    modifier: Modifier,
    text: String,
    textColor: Color,
    buttonColor: Color,
    borderColor: Color? = null,
    enabled: Boolean? = true,
    crossinline onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = borderColor ?: buttonColor,
                shape = RoundedCornerShape(8.dp)
            )
            .background(color = if (enabled == true) buttonColor else GrayColor9)
            .clickable { onClick() }
    ) {
        Text(
            modifier = Modifier.align(Center),
            text = text,
            color = if (enabled == true) textColor else GrayColor7,
            fontWeight = SemiBold,
            fontSize = 16.sp,
            letterSpacing = 0.sp
        )
    }
}

@Composable
inline fun OutLinedRoundedCornerButton(
    modifier: Modifier,
    text: String,
    textColor: Color,
    buttonColor: Color,
    outLineColor: Color,
    crossinline onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = outLineColor,
                shape = RoundedCornerShape(8.dp)
            )
            .background(color = buttonColor)
            .clickable { onClick() }
    ) {
        Text(
            modifier = Modifier.align(Center),
            text = text,
            color = textColor,
            fontWeight = SemiBold,
            fontSize = 16.sp,
            letterSpacing = 0.sp
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun StudioComposable(
    studio: Studio,
    onClickStudio: (Int) -> Unit,
    onClickScrapButton: (Int) -> Unit
) {
    with(studio) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickableWithoutRipple { studio.id?.let { onClickStudio(it) } }
        ) {
            BPMSpacer(height = 18.dp)

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = SpaceBetween,
                    verticalAlignment = CenterVertically
                ) {
                    Text(
                        text = name ?: "",
                        fontWeight = SemiBold,
                        fontSize = 15.sp,
                        letterSpacing = 0.sp
                    )

                    Image(
                        modifier = Modifier.clickableWithoutRipple { studio.id?.let { onClickScrapButton(it) } },
                        painter = painterResource(id = R.drawable.ic_scrap_active),
                        contentDescription = "scrapIcon",
                    )
                }

                BPMSpacer(height = 2.dp)

                Text(
                    text = content ?: "",
                    fontWeight = Medium,
                    fontSize = 12.sp,
                    letterSpacing = 0.sp,
                    color = GrayColor4
                )

                BPMSpacer(height = 2.dp)

                Row(verticalAlignment = CenterVertically) {
                    Icon(
                        modifier = Modifier.size(12.dp),
                        painter = painterResource(id = R.drawable.ic_star_small_filled),
                        contentDescription = "starIcon",
                        tint = MainGreenColor
                    )

                    BPMSpacer(width = 4.dp)

                    Text(
                        text = rating?.clip()!!,
                        fontWeight = Medium,
                        fontSize = 12.sp,
                        letterSpacing = 0.sp,
                        color = GrayColor4
                    )

                    BPMSpacer(width = 12.dp)

                    Text(
                        text = "리뷰 ",
                        fontWeight = Medium,
                        fontSize = 12.sp,
                        letterSpacing = 0.sp,
                        color = GrayColor4
                    )

                    Text(
                        text = "${reviewCount ?: 0}",
                        fontWeight = Medium,
                        fontSize = 12.sp,
                        letterSpacing = 0.sp,
                        color = GrayColor1
                    )

                    BPMSpacer(width = 12.dp)

                    Text(
                        text = "스크랩 수 ",
                        fontWeight = Medium,
                        fontSize = 12.sp,
                        letterSpacing = 0.sp,
                        color = GrayColor4
                    )

                    Text(
                        text = "${scrapCount ?: 0}",
                        fontWeight = Medium,
                        fontSize = 12.sp,
                        letterSpacing = 0.sp,
                        color = GrayColor1
                    )
                }

                BPMSpacer(height = 8.dp)
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                studio.topRecommends?.map { it.first }?.let { keywords ->
                    items(keywords) { keyword ->
                        Box(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = GrayColor8,
                                    shape = RoundedCornerShape(60.dp)
                                )
                                .height(26.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp)
                                    .align(Center),
                                text = keyword,
                                fontWeight = Normal,
                                fontSize = 10.sp,
                                letterSpacing = 0.sp,
                                color = GrayColor1
                            )
                        }
                    }
                }
            }

            BPMSpacer(height = 10.dp)

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.spacedBy(4.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(filesPath!!) { image ->
                    GlideImage(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(10.dp))
                            .width(120.dp)
                            .height(150.dp),
                        model = image,
                        contentDescription = "studioImage",
                        contentScale = ContentScale.Crop
                    )
                }
            }

            BPMSpacer(height = 22.dp)

            Divider(color = GrayColor10)
        }
    }
}

@Composable
fun BPMTextField(
    modifier: Modifier = Modifier,
    textState: MutableState<String>,
    label: String?,
    limit: Int?,
    radius: Dp = 12.dp,
    minHeight: Dp = 40.dp,
    iconPadding: Dp = 0.dp,
    singleLine: Boolean,
    isExtendable: Boolean = true,
    hint: String?,
    onClick: (() -> Unit)? = null,
    allocatedErrorCode: String? = null,
    occurredErrorCode: String? = null,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    icon: @Composable (BoxScope.(Boolean) -> Unit)? = null
) {
    Column(modifier = modifier.background(color = Color.White)) {
        if (label != null || limit != null) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .fillMaxWidth()
            ) {
                if (label != null) {
                    Text(
                        modifier = Modifier.align(BottomStart),
                        text = label,
                        fontWeight = Medium,
                        fontSize = 12.sp,
                        letterSpacing = 0.sp,
                        color = GrayColor4
                    )
                }

                if (limit != null) {
                    Text(
                        modifier = Modifier.align(BottomEnd),
                        text = "${textState.value.length}/$limit",
                        fontWeight = Medium,
                        fontSize = 10.sp,
                        letterSpacing = 0.sp,
                        color = GrayColor4
                    )
                }
            }

            BPMSpacer(height = 10.dp)
        }

        val hasFocus = remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = minHeight)
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(radius),
                    color = if (hasFocus.value) GrayColor1 else GrayColor6
                )
                .clickableWithoutRipple { onClick?.invoke() }
        ) {
            if (hint != null && textState.value.isEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(
                            horizontal = 14.dp,
                            vertical = 12.dp
                        )
                        .heightIn(min = minHeight - 24.dp)
                        .align(TopStart),
                    text = hint,
                    fontWeight = Medium,
                    fontSize = 13.sp,
                    letterSpacing = 0.sp,
                    color = GrayColor6
                )
            }

            if (icon != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(minHeight)
                ) {
                    icon(hasFocus.value)
                }
            }

            if (onClick == null) {
                CompositionLocalProvider(LocalTextSelectionColors.provides(textSelectionColor())) {
                    BasicTextField(
                        modifier = Modifier
                            .padding(
                                start = 14.dp,
                                end = if (icon == null) 14.dp else 20.dp + iconPadding,
                            )
                            .padding(vertical = 12.dp)
                            .fillMaxWidth()
                            .heightIn(min = minHeight - 24.dp, max = if (isExtendable) Dp.Unspecified else minHeight - 24.dp)
                            .align(Center)
                            .onFocusChanged { hasFocus.value = it.hasFocus },
                        value = textState.value,
                        onValueChange = { if (limit != null && textState.value.length >= limit) textState.value else textState.value = it },
                        singleLine = singleLine,
                        keyboardOptions = keyboardOptions,
                        keyboardActions = keyboardActions,
                        cursorBrush = SolidColor(MainBlackColor),
                        textStyle = TextStyle(
                            fontWeight = Medium,
                            fontSize = 13.sp,
                            letterSpacing = 0.sp,
                            color = MainBlackColor
                        )
                    )
                }
            } else {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 14.dp,
                            end = if (icon == null) 14.dp else 14.dp + iconPadding,
                        )
                        .padding(vertical = 8.dp)
                        .align(TopStart),
                    text = textState.value,
                    fontWeight = Medium,
                    fontSize = 13.sp,
                    letterSpacing = 0.sp,
                    color = MainBlackColor
                )
            }
        }

        if (occurredErrorCode != null && allocatedErrorCode == occurredErrorCode) {
            Text(
                modifier = Modifier.padding(
                    top = 6.dp,
                    start = 6.dp
                ),
                text = errorMessage ?: "",
                fontWeight = Medium,
                fontSize = 12.sp,
                letterSpacing = 0.sp,
                color = Color.Red
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ReviewComposable(
    modifier: Modifier = Modifier,
    review: Review,
    onClickLike: (Int) -> Unit,
    onClickActionButton: () -> Unit
) {
    val context = getLocalContext()

    with(review) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clickableWithoutRipple {
                    review.studio?.id?.let { studioId ->
                        review.id?.let { reviewId ->
                            context.startActivity(
                                ReviewDetailActivity.newIntent(
                                    context = context,
                                    studioId = studioId,
                                    reviewId = reviewId
                                )
                            )
                        }
                    }
                }
        ) {
            BPMSpacer(height = 16.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = SpaceBetween,
                verticalAlignment = CenterVertically
            ) {
                Row(verticalAlignment = CenterVertically) {
                    GlideImage(
                        modifier = Modifier.size(24.dp),
                        model = author?.profilePath ?: "",
                        contentDescription = "profileImage"
                    )

                    BPMSpacer(width = 8.dp)

                    Text(
                        text = author?.nickname ?: "",
                        fontWeight = SemiBold,
                        fontSize = 14.sp,
                        letterSpacing = 0.sp
                    )
                }

                Text(
                    text = createdAt?.calculatedFromNow() ?: "",
                    fontWeight = Medium,
                    fontSize = 12.sp,
                    letterSpacing = 0.5.sp
                )
            }

            BPMSpacer(height = 12.dp)

            rating?.let {
                Row {
                    for (i in 1..5) {
                        Image(
                            modifier = Modifier.size(15.dp),
                            painter = painterResource(
                                id = if (i.toDouble() <= it) R.drawable.ic_star_small_filled
                                else if (i.toDouble() > it && it > i - 1) R.drawable.ic_star_small_half
                                else R.drawable.ic_star_small_empty
                            ),
                            contentDescription = "starIcon"
                        )
                    }
                }
            }

            BPMSpacer(height = 14.dp)

            recommends?.let {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    items(it) { keyword ->
                        ReadOnlyKeywordChip(text = keyword)
                    }
                }
            }

            BPMSpacer(height = 14.dp)

            filesPath?.let {
                Row(modifier = Modifier.fillMaxWidth()) {
                    repeat(it.size) { index ->
                        GlideImage(
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp),
                            model = it[index],
                            contentDescription = "reviewImage",
                            contentScale = ContentScale.Crop
                        )

                        BPMSpacer(width = 4.dp)
                    }

                    repeat(5 - it.size) { index ->
                        Box(modifier = Modifier.weight(1f))

                        if (index == 5 - it.size - 1) {
                            BPMSpacer(width = 4.dp)
                        }
                    }
                }
            }

            BPMSpacer(height = 10.dp)

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = content ?: "",
                fontWeight = Normal,
                fontSize = 13.sp,
                letterSpacing = 0.sp,
                maxLines = 4,
                lineHeight = 19.sp,
                overflow = TextOverflow.Ellipsis
            )

            BPMSpacer(height = 25.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = CenterVertically,
                horizontalArrangement = SpaceBetween
            ) {
                LikeButton(
                    liked = liked ?: false,
                    likeCount = likeCount ?: 0,
                    onClick = { review.id?.let { reviewId -> onClickLike(reviewId) } }
                )

                Icon(
                    modifier = Modifier.clickableWithoutRipple { onClickActionButton() },
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "editIcon",
                    tint = GrayColor4
                )
            }
        }

        BPMSpacer(height = 20.dp)

        Divider(color = GrayColor13)
    }
}

@Composable
inline fun LikeButton(
    modifier: Modifier = Modifier,
    liked: Boolean,
    likeCount: Int,
    crossinline onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .height(28.dp)
            .border(
                width = 1.dp,
                color = if (liked) MainBlackColor else GrayColor9,
                shape = RoundedCornerShape(12.dp)
            )
            .background(color = if (liked) MainBlackColor else Color.White)
            .clickableWithoutRipple { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .align(Center),
            verticalAlignment = CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_like),
                contentDescription = "likeIcon",
                tint = if (liked) MainGreenColor else MainBlackColor
            )

            BPMSpacer(width = 4.dp)

            Text(
                text = "좋아요",
                fontWeight = Medium,
                fontSize = 12.sp,
                letterSpacing = 0.sp,
                color = if (liked) MainGreenColor else MainBlackColor
            )

            BPMSpacer(width = 4.dp)

            Text(
                text = "$likeCount",
                fontWeight = SemiBold,
                fontSize = 12.sp,
                letterSpacing = 0.sp,
                color = if (liked) MainGreenColor else MainBlackColor
            )
        }
    }
}

@Composable
inline fun ClickableKeywordChip(
    keyword: Keyword,
    isChosen: Boolean,
    crossinline onClick: () -> Unit
) {
    Text(
        modifier = Modifier
            .clip(RoundedCornerShape(60.dp))
            .background(color = if (isChosen) MainGreenColor else GrayColor9)
            .padding(
                horizontal = 12.dp,
                vertical = 8.dp
            )
            .clickableWithoutRipple { onClick() },
        text = keyword.keyword ?: "",
        fontWeight = Medium,
        fontSize = 12.sp,
        letterSpacing = 0.sp,
        color = if (isChosen) MainBlackColor else GrayColor4
    )
}

@Composable
fun ReadOnlyKeywordChip(
    text: String
) {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(60.dp))
            .background(color = GrayColor9),
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = CenterVertically
        ) {
            Text(
                text = text,
                fontWeight = Normal,
                fontSize = 12.sp,
                color = GrayColor3,
                letterSpacing = 0.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        }
    }
}

@Composable
inline fun ReviewListHeader(
    modifier: Modifier = Modifier,
    isShowingImageReviewsOnly: Boolean,
    isSortedByLike: Boolean,
    crossinline onClickShowImageReviewsOnlyOrNot: () -> Unit,
    crossinline onClickSortOrderByLike: () -> Unit,
    crossinline onClickSortOrderByDate: () -> Unit,
    crossinline onClickWriteReview: () -> Unit
) {
    Column {
        Row(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(55.dp),
            horizontalArrangement = SpaceBetween,
            verticalAlignment = CenterVertically
        ) {
            Text(
                text = "업체 리뷰",
                fontWeight = SemiBold,
                fontSize = 16.sp,
                letterSpacing = 0.sp
            )

            Text(
                modifier = Modifier.clickableWithoutRipple { onClickWriteReview() },
                text = "리뷰 작성하기",
                fontWeight = Medium,
                fontSize = 14.sp,
                letterSpacing = 0.sp,
                color = GrayColor4
            )
        }

        Divider(color = GrayColor13)

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(55.dp),
            horizontalArrangement = SpaceBetween,
            verticalAlignment = CenterVertically
        ) {
            Row(modifier = Modifier.clickableWithoutRipple { onClickShowImageReviewsOnlyOrNot() }) {
                Icon(
                    modifier = Modifier
                        .align(CenterVertically),
                    painter = painterResource(id = R.drawable.ic_check_field),
                    contentDescription = "checkFieldIcon",
                    tint = if (isShowingImageReviewsOnly) MainBlackColor else GrayColor7
                )

                BPMSpacer(width = 8.dp)

                Text(
                    text = "사진 리뷰만 보기",
                    fontWeight = Medium,
                    fontSize = 14.sp,
                    letterSpacing = 0.sp
                )
            }

            Row {
                Text(
                    modifier = Modifier.clickableWithoutRipple { onClickSortOrderByLike() },
                    text = "좋아요순",
                    fontWeight = Medium,
                    fontSize = 14.sp,
                    letterSpacing = 0.sp,
                    color = if (isSortedByLike) MainBlackColor else GrayColor6
                )

                BPMSpacer(width = 20.dp)

                Divider(
                    modifier = Modifier
                        .height(12.dp)
                        .width(1.dp)
                        .align(CenterVertically),
                    color = GrayColor3
                )

                BPMSpacer(width = 20.dp)

                Text(
                    modifier = Modifier.clickableWithoutRipple { onClickSortOrderByDate() },
                    text = "최신순",
                    fontWeight = Medium,
                    fontSize = 14.sp,
                    letterSpacing = 0.sp,
                    color = if (!isSortedByLike) MainBlackColor else GrayColor6
                )
            }
        }

        Divider(color = GrayColor13)
    }
}

@Composable
inline fun ImagePlaceHolder(
    image: ImageBitmap?,
    crossinline onClick: () -> Unit,
    noinline onClickRemove: (() -> Unit)? = null
) {
    val imageState = remember { mutableStateOf(image) }
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.size(105.dp)) {
        Box(modifier = Modifier
            .size(100.dp)
            .background(color = GrayColor10)
            .align(BottomStart)
            .clickableWithoutRipple {
                onClick()
                focusManager.clearFocus()
            }
        ) {
            imageState.value?.let {
                Image(
                    bitmap = it,
                    contentDescription = "image",
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                Image(
                    modifier = Modifier.align(Center),
                    painter = painterResource(id = R.drawable.ic_add_image),
                    contentDescription = "addImage"
                )
            }
        }

        if (imageState.value != null) {
            Box(modifier = Modifier
                .shadow(
                    elevation = 2.dp,
                    shape = CircleShape
                )
                .clip(shape = CircleShape)
                .size(20.dp)
                .background(color = Color.White)
                .align(Alignment.TopEnd)
                .clickableWithoutRipple { onClickRemove?.invoke() }
            ) {
                Icon(
                    modifier = Modifier
                        .size(8.dp)
                        .align(Center),
                    painter = painterResource(id = R.drawable.ic_remove),
                    contentDescription = "removeImageIcon"
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
inline fun CommentComposable(
    modifier: Modifier = Modifier,
    comment: Comment,
    crossinline onClickLike: () -> Unit,
    crossinline onClickActionButton: () -> Unit
) {
    with(comment) {
        Row(modifier = modifier.fillMaxWidth()) {
            if (comment.parentId != null) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_child_comment),
                    contentDescription = "childCommentIcon",
                    tint = GrayColor7
                )

                BPMSpacer(width = 12.dp)
            }

            GlideImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(26.dp),
                model = comment.author?.profilePath,
                contentDescription = "authorImage",
                contentScale = ContentScale.FillBounds
            )

            BPMSpacer(width = 8.dp)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 73.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(22.dp),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = SpaceBetween
                ) {
                    Row(verticalAlignment = CenterVertically) {
                        Text(
                            text = author?.nickname ?: "",
                            fontWeight = SemiBold,
                            fontSize = 12.sp,
                            letterSpacing = 0.sp,
                            color = GrayColor3
                        )

                        BPMSpacer(width = 4.dp)

                        Icon(
                            painter = painterResource(id = R.drawable.ic_dot),
                            contentDescription = "dotIcon",
                            tint = GrayColor3
                        )

                        BPMSpacer(width = 4.dp)

                        Text(
                            text = createdAt?.calculatedFromNow() ?: "",
                            fontWeight = SemiBold,
                            fontSize = 12.sp,
                            letterSpacing = 0.sp,
                            color = GrayColor3
                        )
                    }

                    Icon(
                        modifier = Modifier.clickableWithoutRipple { onClickActionButton() },
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "editIcon",
                        tint = GrayColor4
                    )
                }

                BPMSpacer(height = 5.dp)

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = body ?: "",
                    fontWeight = Normal,
                    fontSize = 13.sp,
                    letterSpacing = 0.sp
                )

                BPMSpacer(height = 10.dp)

                Row(
                    modifier = Modifier.clickableWithoutRipple { onClickLike() },
                    verticalAlignment = CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_like),
                        contentDescription = "likeIcon",
                        tint = if (liked == true) MainBlackColor else GrayColor8
                    )

                    BPMSpacer(width = 4.dp)

                    Text(
                        text = "${likeCount ?: 0}",
                        fontWeight = Medium,
                        fontSize = 13.sp,
                        letterSpacing = 0.sp,
                        color = GrayColor1
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0x50000000))
            .clickableWithoutRipple { }
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Center),
            color = MainGreenColor
        )
    }
}

@Composable
fun LoadingBlock(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Center),
            color = MainGreenColor
        )
    }
}

@Composable
fun initImageLauncher(context: Context, onSuccess: (List<Uri>, List<ImageBitmap>) -> Unit, onFailure: (Throwable) -> Unit) =
    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(5),
        onResult = { uris ->
            runCatching {
                uris.map { uri ->
                    convertUriToBitmap(
                        contentResolver = context.contentResolver,
                        uri = uri
                    )
                }
            }.onSuccess { images ->
                onSuccess(uris, images.map { it.asImageBitmap() })
            }.onFailure {
                onFailure(it)
            }
        }
    )

@Composable
fun rememberLifecycleEvent(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current): Lifecycle.Event {
    var lifecycleEvent by remember { mutableStateOf(Lifecycle.Event.ON_ANY) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycleEvent = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    return lifecycleEvent
}

@Composable
fun BPMBottomSheet(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    topStart = 12.dp,
                    topEnd = 12.dp
                )
            )
            .fillMaxWidth()
            .background(Color.White)
    ) {
        BPMSpacer(height = 8.dp)

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(30.dp))
                .background(GrayColor4)
                .width(56.dp)
                .height(4.dp)
        )

        BPMSpacer(height = 16.dp)

        content()
    }
}

@Composable
fun BottomSheetButtonComposable(
    button: BottomSheetButton,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .clickableWithoutRipple { onClick() }
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp)
                .align(CenterStart),
            text = button.text,
            fontWeight = Medium,
            fontSize = 14.sp,
            letterSpacing = 0.sp
        )
    }
}

@Composable
fun getLocalContext(): BaseComponentActivityV2 {
    return LocalContext.current as BaseComponentActivityV2
}

@Composable
fun Dp.toPx() = with(LocalDensity.current) { this@toPx.toPx() }


@Composable
fun Int.toDp() = with(LocalDensity.current) { this@toDp.toDp() }