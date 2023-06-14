package com.team.bpm.presentation.ui.main.lounge.community.detail

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInWindow
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
import com.team.bpm.presentation.model.BottomSheetButton
import com.team.bpm.presentation.model.ReportType
import com.team.bpm.presentation.util.addFocusCleaner
import com.team.bpm.presentation.util.calculatedFromNow
import com.team.bpm.presentation.util.clickableWithoutRipple
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class CommunityDetailActivity : BaseComponentActivityV2() {
    @Composable
    override fun InitComposeUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        CommunityDetailActivityContent()
    }

    companion object {
        const val KEY_COMMUNITY_ID = "community_id"

        fun newIntent(
            context: Context,
            communityId: Int
        ): Intent {
            return Intent(context, CommunityDetailActivity::class.java).apply {
                putExtra(KEY_COMMUNITY_ID, communityId)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
private fun CommunityDetailActivityContent(
    viewModel: CommunityDetailViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel)
    val context = getLocalContext()
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val commentTextFieldState = remember { mutableStateOf("") }
    val commentFocusRequester = remember { FocusRequester() }
    val commentScrollPosition = remember { mutableStateOf(0) }
    val textFieldPosition = remember { mutableStateOf(0) }
    val commentHeight = remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        event.invoke(CommunityDetailContract.Event.GetUserId)
        event.invoke(CommunityDetailContract.Event.GetCommunityDetail)
        event.invoke(CommunityDetailContract.Event.GetCommentList)
    }

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is CommunityDetailContract.Effect.ShowToast -> {
                    context.showToast(effect.text)
                }

                is CommunityDetailContract.Effect.RefreshCommentList -> {
                    commentTextFieldState.value = ""
                    focusManager.clearFocus()
                    event.invoke(CommunityDetailContract.Event.GetCommentList)
                }

                is CommunityDetailContract.Effect.GoToCommunityList -> {
                    context.finish()
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
                event.invoke(CommunityDetailContract.Event.OnBottomSheetHide)
            }
        }

        ModalBottomSheetLayout(
            modifier = Modifier
                .imePadding()
                .windowInsetsPadding(insets = WindowInsets.systemBars.only(sides = WindowInsetsSides.Vertical))
                .addFocusCleaner(focusManager = focusManager),
            sheetState = bottomSheetState,
            sheetBackgroundColor = Transparent,
            sheetContent = {
                BPMBottomSheet {
                    bottomSheetButtonList.forEach { bottomSheetButton ->
                        BottomSheetButtonComposable(
                            button = bottomSheetButton,
                            onClick = {
                                when (bottomSheetButton) {
                                    BottomSheetButton.DELETE_POST -> CommunityDetailContract.Event.OnClickDeleteCommunity
                                    BottomSheetButton.REPORT_POST -> CommunityDetailContract.Event.OnClickReportCommunity
                                    BottomSheetButton.DELETE_COMMENT -> CommunityDetailContract.Event.OnClickDeleteComment
                                    BottomSheetButton.REPORT_COMMENT -> CommunityDetailContract.Event.OnClickReportComment
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
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .padding(bottom = 54.dp)
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
                                    model = community?.author?.profilePath,
                                    contentScale = ContentScale.FillBounds,
                                    contentDescription = "authorImage"
                                )

                                BPMSpacer(width = 8.dp)

                                Text(
                                    text = community?.author?.nickname ?: "",
                                    fontWeight = SemiBold,
                                    fontSize = 14.sp,
                                    letterSpacing = 0.sp
                                )
                            }

                            Row(verticalAlignment = CenterVertically) {
                                Text(
                                    text = community?.createdAt?.calculatedFromNow() ?: "",
                                    fontWeight = Medium,
                                    fontSize = 13.sp,
                                    letterSpacing = 0.sp,
                                    color = GrayColor5
                                )

                                BPMSpacer(width = 8.dp)

                                Icon(
                                    modifier = Modifier.clickableWithoutRipple { event.invoke(CommunityDetailContract.Event.OnClickCommunityActionButton) },
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

                    community?.filesPath?.let { images ->
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

                    Text(
                        modifier = Modifier
                            .padding(
                                top = 20.dp,
                                start = 20.dp,
                                end = 20.dp,
                                bottom = 16.dp
                            )
                            .fillMaxWidth(),
                        text = community?.content ?: "",
                        fontWeight = Normal,
                        fontSize = 13.sp,
                        letterSpacing = 0.sp
                    )

                    BPMSpacer(height = 8.dp)

                    LikeButton(
                        modifier = Modifier.padding(start = 20.dp),
                        liked = liked ?: false,
                        likeCount = likeCount ?: 0,
                        onClick = { event.invoke(CommunityDetailContract.Event.OnClickLike) }
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

                    BPMSpacer(height = 20.dp)

                    if (isCommentListLoading) {
                        LoadingBlock()
                    } else {
                        commentList.forEach { comment ->
                            CommentComposable(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .onGloballyPositioned {
                                        if (commentIdToScroll == comment.id) {
                                            commentScrollPosition.value = it.positionInParent().y.roundToInt()
                                            commentHeight.value = it.size.height
                                        }
                                    }
                                    .background(color = if (selectedComment?.id == comment.id) HighlightColor else Color.White),
                                comment = comment,
                                onClickLike = { comment.id?.let { commentId -> event.invoke(CommunityDetailContract.Event.OnClickCommentLike(commentId)) } },
                                onClickActionButton = {
                                    comment.id?.let { commentId ->
                                        comment.author?.id?.let {
                                            event.invoke(
                                                CommunityDetailContract.Event.OnClickCommentActionButton(
                                                    comment = comment,
                                                    parentCommentId = comment.parentId ?: commentId
                                                )
                                            )
                                        }
                                    }
                                }
                            )

                            BPMSpacer(height = 22.dp)

                            LaunchedEffect(Unit) {
                                scrollState.animateScrollTo(commentScrollPosition.value - commentHeight.value * 3)
                            }
                        }
                    }
                }

                val scope = rememberCoroutineScope()

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
                            .focusRequester(commentFocusRequester)
                            .onGloballyPositioned { coordinates ->
                                scope.launch {
                                    delay(400L)
                                    textFieldPosition.value = coordinates.positionInWindow().y.roundToInt()
                                }
                            },
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
                                            event.invoke(CommunityDetailContract.Event.OnClickSendComment(comment = commentTextFieldState.value))
                                        }
                                    },
                                painter = painterResource(id = R.drawable.ic_send_comment),
                                contentDescription = "sendIconButton",
                                tint = if (hasFocus) GrayColor2 else GrayColor5
                            )
                        },
                        iconPadding = 20.dp
                    )
                }

                if (isLoading) {
                    LoadingScreen()
                }

                if (isReportDialogShowing) {
                    val dialogFocusRequester = remember { FocusRequester() }

                    reportType?.let { reportType ->
                        TextFieldDialog(
                            title = "신고 사유를 작성해주세요",
                            focusRequester = dialogFocusRequester,
                            onDismissRequest = { event.invoke(CommunityDetailContract.Event.OnClickDismissReportDialog) },
                            onClickCancel = { event.invoke(CommunityDetailContract.Event.OnClickDismissReportDialog) },
                            onClickConfirm = { reason ->
                                event.invoke(
                                    when (reportType) {
                                        ReportType.POST -> CommunityDetailContract.Event.OnClickSendCommunityReport(reason)
                                        ReportType.COMMENT -> CommunityDetailContract.Event.OnClickSendCommentReport(reason)
                                    }
                                )
                            }
                        )
                    }

                    LaunchedEffect(Unit) {
                        dialogFocusRequester.requestFocus()
                    }
                }

                if (isNoticeDialogShowing) {
                    NoticeDialog(
                        title = null,
                        content = noticeDialogContent,
                        onDismissRequest = { event.invoke(CommunityDetailContract.Event.OnClickDismissNoticeDialog) }
                    )
                }

                if (isNoticeToQuitDialogShowing) {
                    NoticeDialog(
                        title = null,
                        content = noticeToQuitDialogContent,
                        onDismissRequest = { event.invoke(CommunityDetailContract.Event.OnClickDismissNoticeToQuitDialog) }
                    )
                }
            }
        }
    }
}