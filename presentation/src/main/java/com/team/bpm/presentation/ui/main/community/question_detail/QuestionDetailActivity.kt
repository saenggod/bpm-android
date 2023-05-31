package com.team.bpm.presentation.ui.main.community.question_detail

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
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
import com.team.bpm.presentation.R
import com.team.bpm.presentation.base.BaseComponentActivityV2
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.*
import com.team.bpm.presentation.compose.theme.*
import com.team.bpm.presentation.util.addFocusCleaner
import com.team.bpm.presentation.util.clickableWithoutRipple
import com.team.bpm.presentation.util.dateOnly
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt

@AndroidEntryPoint
class QuestionDetailActivity : BaseComponentActivityV2() {
    @Composable
    override fun InitComposeUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        QuestionDetailActivityContent()
    }

    companion object {
        const val KEY_QUESTION_ID = "question_id"

        fun newIntent(context: Context, questionId: Int): Intent {
            return Intent(context, QuestionDetailActivity::class.java).apply {
                putExtra(KEY_QUESTION_ID, questionId)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
private fun QuestionDetailActivityContent(
    viewModel: QuestionDetailViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel)
    val context = getLocalContext()
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val commentTextFieldState = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        event.invoke(QuestionDetailContract.Event.GetQuestionDetail)
        event.invoke(QuestionDetailContract.Event.GetCommentList)
    }

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is QuestionDetailContract.Effect.ShowToast -> {
                    context.showToast(effect.text)
                }
                is QuestionDetailContract.Effect.RefreshCommentList -> {
                    commentTextFieldState.value = ""
                    focusManager.clearFocus()
                    event.invoke(QuestionDetailContract.Event.GetCommentList)
                }
                is QuestionDetailContract.Effect.ExpandBottomSheet -> {
                    bottomSheetState.show()
                }
                is QuestionDetailContract.Effect.ShowKeyboard -> {
                    bottomSheetState.hide()
                    focusRequester.requestFocus()
                }
            }
        }
    }

    with(state) {
        ModalBottomSheetLayout(
            modifier = Modifier
                .imePadding()
                .windowInsetsPadding(insets = WindowInsets.systemBars.only(sides = WindowInsetsSides.Vertical))
                .addFocusCleaner(focusManager = focusManager),
            sheetState = bottomSheetState,
            sheetBackgroundColor = Transparent,
            sheetContent = {
                Column(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = 12.dp,
                                topEnd = 12.dp
                            )
                        )
                        .fillMaxWidth()
                        .height(136.dp)
                        .background(Color.White)
                ) {
                    BPMSpacer(height = 8.dp)

                    Box(
                        modifier = Modifier
                            .align(CenterHorizontally)
                            .clip(RoundedCornerShape(30.dp))
                            .background(GrayColor4)
                            .width(56.dp)
                            .height(4.dp)
                    )

                    BPMSpacer(height = 16.dp)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .clickableWithoutRipple { event.invoke(QuestionDetailContract.Event.OnClickWriteCommentOnComment) }
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .align(CenterStart),
                            text = "대댓글 달기",
                            fontWeight = Medium,
                            fontSize = 14.sp,
                            letterSpacing = 0.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .align(CenterStart),
                            text = "신고하기",
                            fontWeight = Medium,
                            fontSize = 14.sp,
                            letterSpacing = 0.sp
                        )
                    }
                }
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .padding(bottom = 54.dp)
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                ) {
                    ScreenHeader(header = "질문")

                    Column(modifier = Modifier.height(56.dp)) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(55.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                GlideImage(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(24.dp),
                                    model = question?.author?.profilePath,
                                    contentScale = ContentScale.FillBounds,
                                    contentDescription = "authorImage"
                                )

                                BPMSpacer(width = 8.dp)

                                Text(
                                    text = question?.author?.nickname ?: "",
                                    fontWeight = SemiBold,
                                    fontSize = 14.sp,
                                    letterSpacing = 0.sp
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = question?.createdAt?.dateOnly() ?: "",
                                    fontWeight = Medium,
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

                    question?.filesPath?.let { images ->
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
                                        fontWeight = Normal,
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
                        text = question?.content ?: "",
                        fontWeight = Normal,
                        fontSize = 13.sp,
                        letterSpacing = 0.sp
                    )

                    BPMSpacer(height = 8.dp)

                    LikeButton(
                        modifier = Modifier.padding(start = 20.dp),
                        liked = liked ?: false,
                        likeCount = likeCount ?: 0,
                        onClick = { event.invoke(QuestionDetailContract.Event.OnClickLike) }
                    )

                    BPMSpacer(height = 28.dp)

                    Divider(
                        thickness = 4.dp,
                        color = GrayColor10
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .align(CenterStart),
                            text = "댓글 $commentsCount",
                            fontSize = 16.sp,
                            fontWeight = SemiBold,
                            letterSpacing = 0.sp
                        )
                    }

                    Divider(
                        thickness = 1.dp,
                        color = GrayColor10
                    )

                    commentList?.let {
                        Column(
                            modifier = Modifier
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 20.dp
                                )
                        ) {
                            val redirectCommentScrollPosition = remember { mutableStateOf(0) }

                            it.forEach { comment ->
                                CommentComposable(
                                    modifier = Modifier
                                        .onGloballyPositioned {
                                            if (redirectCommentId == comment.id) {
                                                redirectCommentScrollPosition.value = it.positionInRoot().y.roundToInt()
                                            }
                                        }
                                        .background(color = if (parentCommentId == comment.id) HighlightColor else Color.White),
                                    comment = comment,
                                    onClickLike = { commentId -> event.invoke(QuestionDetailContract.Event.OnClickCommentLike(commentId)) },
                                    onClickActionButton = { commentId ->
                                        focusManager.clearFocus()
                                        if (comment.parentId == null) {
                                            event.invoke(QuestionDetailContract.Event.OnClickCommentActionButton(commentId))
                                        } else {
                                            comment.parentId?.let { parentCommentId -> event.invoke(QuestionDetailContract.Event.OnClickCommentActionButton(parentCommentId)) }
                                        }
                                    }
                                )

                                BPMSpacer(height = 22.dp)
                            }
                        }
                    } ?: run {
                        LoadingBlock(modifier = Modifier.height(300.dp))
                    }
                }

                Box(
                    modifier = Modifier
                        .align(BottomCenter)
                        .fillMaxWidth()
                        .heightIn(min = 54.dp)
                        .background(color = Color.White)
                ) {
                    BPMTextField(
                        modifier = Modifier
                            .padding(
                                horizontal = 16.dp,
                                vertical = 10.dp
                            )
                            .focusRequester(focusRequester),
                        textState = commentTextFieldState,
                        label = null,
                        limit = null,
                        radius = 8.dp,
                        minHeight = 34.dp,
                        singleLine = true,
                        hint = "댓글을 입력해보세요",
                        icon = { hasFocus ->
                            Icon(
                                modifier = Modifier
                                    .padding(
                                        top = 12.dp,
                                        end = 16.dp
                                    )
                                    .size(20.dp)
                                    .align(TopEnd)
                                    .clickableWithoutRipple {
                                        if (commentTextFieldState.value.isNotEmpty()) {
                                            event.invoke(QuestionDetailContract.Event.OnClickSendComment(parentId = parentCommentId, comment = commentTextFieldState.value))
                                        }
                                    },
                                painter = painterResource(id = R.drawable.ic_send_comment),
                                contentDescription = "sendIconButton",
                                tint = if (hasFocus) GrayColor2 else GrayColor5
                            )
                        },
                        iconSize = 20.dp
                    )
                }

                if (isLoading) {
                    LoadingScreen()
                }
            }
        }
    }
}