package com.team.bpm.presentation.ui.main.bodyshape.detail

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
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
import com.team.bpm.presentation.model.BottomSheetButton
import com.team.bpm.presentation.ui.main.bodyshape.detail.posting.BodyShapeDetailPostingActivity
import com.team.bpm.presentation.util.calculatedFromNow
import com.team.bpm.presentation.util.clickableWithoutRipple
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class BodyShapeDetailActivity : BaseComponentActivityV2() {
    @Composable
    override fun InitComposeUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        BodyShapeDetailActivityContent()
    }

    companion object {

        const val KEY_BUNDLE = "bundle"
        const val KEY_ALBUM_ID = "album_id"
        const val KEY_BODY_SHAPE_ID = "body_shape_id"

        fun newIntent(
            context: Context,
            albumId: Int,
            bodyShapeId: Int
        ): Intent {
            return Intent(context, BodyShapeDetailActivity::class.java).apply {
                putExtra(
                    KEY_BUNDLE, bundleOf(
                        KEY_ALBUM_ID to albumId,
                        KEY_BODY_SHAPE_ID to bodyShapeId
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalGlideComposeApi::class, ExperimentalMaterialApi::class)
@Composable
private fun BodyShapeDetailActivityContent(
    viewModel: BodyShapeDetailViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel)
    val context = getLocalContext()
    val scrollState = rememberScrollState()
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val editBodyShapeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == BodyShapeDetailPostingActivity.RESULT_OK) {
                event.invoke(BodyShapeDetailContract.Event.GetBodyShapeDetail)
            }
        }
    )

    LaunchedEffect(Unit) {
        event.invoke(BodyShapeDetailContract.Event.GetBodyShapeDetail)
    }

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is BodyShapeDetailContract.Effect.ShowToast -> {
                    context.showToast(effect.text)
                }

                is BodyShapeDetailContract.Effect.GoBack -> {
                    context.finish()
                }

                is BodyShapeDetailContract.Effect.GoToEdit -> {
                    editBodyShapeLauncher.launch(BodyShapeDetailPostingActivity.newIntent(context, effect.albumId, effect.bodyShapeId))
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
                event.invoke(BodyShapeDetailContract.Event.OnBottomSheetHide)
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
                    bottomSheetButtonList.forEach { bottomSheetButton ->
                        BottomSheetButtonComposable(
                            button = bottomSheetButton,
                            onClick = {
                                when (bottomSheetButton) {
                                    BottomSheetButton.DELETE_POST -> BodyShapeDetailContract.Event.OnClickDeleteBodyShape
                                    BottomSheetButton.EDIT_POST -> BodyShapeDetailContract.Event.OnClickEditBodyShape
                                    else -> null
                                }?.let {
                                    event.invoke(
                                        it
                                    )
                                }
                            }
                        )
                    }
                }
            }
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(state = scrollState)
                        .background(color = GrayColor11)
                ) {
                    Column(modifier = Modifier.background(color = Color.White)) {
                        ScreenHeader(header = "") // TODO : 날짜

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
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    GlideImage(
                                        modifier = Modifier
                                            .clip(shape = CircleShape)
                                            .size(24.dp),
                                        model = bodyShape?.author?.profilePath ?: "",
                                        contentDescription = "profileImage"
                                    )

                                    BPMSpacer(width = 8.dp)

                                    Text(
                                        text = bodyShape?.author?.nickname ?: "",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        letterSpacing = 0.sp
                                    )
                                }

                                Row(modifier = Modifier.align(Alignment.CenterVertically)) {
                                    Text(
                                        text = bodyShape?.createdAt?.calculatedFromNow() ?: "",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        letterSpacing = 0.sp,
                                        color = GrayColor3
                                    )

                                    BPMSpacer(width = 8.dp)

                                    Icon(
                                        modifier = Modifier.clickableWithoutRipple {
                                            event.invoke(
                                                BodyShapeDetailContract.Event.OnClickBodyShapeActionButton
                                            )
                                        },
                                        painter = painterResource(id = R.drawable.ic_edit),
                                        contentDescription = "editIcon",
                                        tint = GrayColor4
                                    )
                                }
                            }
                        }

                        Divider(color = GrayColor9)

                        bodyShape?.filesPath?.let { images ->
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
                                            contentDescription = "bodyShapeImage",
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

                        BPMSpacer(height = 20.dp)

                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Text(
                                text = bodyShape?.content ?: "",
                                fontWeight = FontWeight.Normal,
                                fontSize = 13.sp,
                                letterSpacing = 0.sp,
                                lineHeight = 19.sp
                            )

                            BPMSpacer(height = 25.dp)
                        }
                    }
                }
            }

            if (isLoading) {
                LoadingScreen()
            }

            if (isNoticeDialogShowing) {
                NoticeDialog(
                    title = null,
                    content = noticeDialogContent,
                    onDismissRequest = { event.invoke(BodyShapeDetailContract.Event.OnClickDismissNoticeDialog) }
                )
            }

            if (isNoticeToQuitDialogShowing) {
                NoticeDialog(
                    title = null,
                    content = noticeToQuitDialogContent,
                    onDismissRequest = { event.invoke(BodyShapeDetailContract.Event.OnClickDismissNoticeToQuitDialog) }
                )
            }
        }
    }
}