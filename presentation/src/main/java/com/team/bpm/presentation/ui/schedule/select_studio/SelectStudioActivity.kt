package com.team.bpm.presentation.ui.schedule.select_studio

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.team.bpm.domain.model.Studio
import com.team.bpm.presentation.R
import com.team.bpm.presentation.base.BaseComponentActivityV2
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.BPMSpacer
import com.team.bpm.presentation.compose.BPMTextField
import com.team.bpm.presentation.compose.LoadingScreen
import com.team.bpm.presentation.compose.RoundedCornerButton
import com.team.bpm.presentation.compose.getLocalContext
import com.team.bpm.presentation.compose.theme.*
import com.team.bpm.presentation.ui.schedule.ScheduleActivity
import com.team.bpm.presentation.ui.schedule.select_studio.SelectStudioActivity.Companion.RESULT_OK
import com.team.bpm.presentation.util.clickableWithoutRipple
import com.team.bpm.presentation.util.clip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SelectStudioActivity : BaseComponentActivityV2() {
    @Composable
    override fun InitComposeUi() {
        SelectStudioActivityContent()
    }

    companion object {
        const val RESULT_OK = 200

        fun newIntent(context: Context): Intent {
            return Intent(context, SelectStudioActivity::class.java)
        }
    }
}

@Composable
private fun SelectStudioActivityContent(
    viewModel: SelectStudioViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel)
    val context = getLocalContext()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is SelectStudioContract.Effect.Finish -> {
                    context.setResult(RESULT_OK, Intent().putExtra(ScheduleActivity.KEY_STUDIO, effect.studio))
                    context.finish()
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
            if (studioList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 56.dp,
                            bottom = 76.dp
                        )
                ) {
                    items(studioList) { studioList ->
                        StudioWithCheckBox(
                            studio = studioList,
                            selectedStudioId = selectedStudio?.id ?: -1,
                            onClick = { studio -> event.invoke(SelectStudioContract.Event.OnClickStudio(studio)) }
                        )
                    }
                }
            }

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    verticalAlignment = CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(start = 14.dp)
                            .size(26.dp)
                            .align(CenterVertically)
                            .clickableWithoutRipple { context.finish() },
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = ""
                    )

                    val searchTextState = remember { mutableStateOf("") }

                    BPMTextField(
                        modifier = Modifier.padding(horizontal = 14.dp),
                        textState = searchTextState,
                        label = null,
                        limit = null,
                        singleLine = true,
                        hint = null,
                        isExtendable = false,
                        minHeight = 40.dp,
                        iconPadding = 12.dp,
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                            if (searchTextState.value.isNotEmpty()) {
                                event.invoke(SelectStudioContract.Event.OnClickSearch(searchTextState.value))
                            }
                        }),
                        icon = { hasFocus ->
                            Icon(
                                modifier = Modifier
                                    .padding(end = 4.dp)
                                    .size(32.dp)
                                    .align(Alignment.CenterEnd)
                                    .clickableWithoutRipple {
                                        if (searchTextState.value.isNotEmpty()) {
                                            event.invoke(SelectStudioContract.Event.OnClickSearch(searchTextState.value))
                                        }
                                    },
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = "searchIconButton",
                                tint = if (hasFocus) GrayColor2 else GrayColor5
                            )
                        }
                    )
                }
            }

            if (isLoading) {
                LoadingScreen()
            }

            if (studioList.isNotEmpty()) {
                RoundedCornerButton(
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp,
                            vertical = 14.dp
                        )
                        .fillMaxWidth()
                        .height(48.dp)
                        .align(BottomCenter),
                    text = "선택 완료",
                    textColor = MainBlackColor,
                    buttonColor = MainGreenColor,
                    borderColor = if (selectedStudio != null) MainGreenColor else GrayColor9,
                    enabled = selectedStudio != null,
                    onClick = { event.invoke(SelectStudioContract.Event.OnClickComplete) }
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun StudioWithCheckBox(
    studio: Studio,
    selectedStudioId: Int,
    onClick: (Studio) -> Unit
) {
    val isSelected = studio.id == selectedStudioId

    with(studio) {
        Column(modifier = Modifier.fillMaxWidth()) {
            BPMSpacer(height = 18.dp)

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = CenterVertically
                ) {
                    Text(
                        text = name ?: "",
                        fontWeight = SemiBold,
                        fontSize = 15.sp,
                        letterSpacing = 0.sp
                    )

                    Icon(
                        modifier = Modifier.clickable { onClick(studio) },
                        painter = painterResource(id = R.drawable.ic_check_box),
                        contentDescription = "checkBoxIcon",
                        tint = if (isSelected) MainBlackColor else GrayColor7
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
                        text = "스크랩 ",
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
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
                                .align(Alignment.Center),
                            text = "친절해요",
                            fontWeight = Normal,
                            fontSize = 10.sp,
                            letterSpacing = 0.sp,
                            color = GrayColor1
                        )
                    }

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
                                .align(Alignment.Center),
                            text = "소통이 빨라요",
                            fontWeight = Normal,
                            fontSize = 10.sp,
                            letterSpacing = 0.sp,
                            color = GrayColor1
                        )
                    }

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
                                .align(Alignment.Center),
                            text = "깨끗해요",
                            fontWeight = Normal,
                            fontSize = 10.sp,
                            letterSpacing = 0.sp,
                            color = GrayColor1
                        )
                    }
                }

                BPMSpacer(height = 10.dp)
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = spacedBy(4.dp),
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
                        contentScale = Crop
                    )
                }
            }

            BPMSpacer(height = 22.dp)

            Divider(color = GrayColor10)
        }
    }
}