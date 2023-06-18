package com.team.bpm.presentation.ui.main.studio.search.result

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.team.bpm.presentation.R
import com.team.bpm.presentation.base.BaseComponentActivityV2
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.*
import com.team.bpm.presentation.compose.theme.*
import com.team.bpm.presentation.ui.main.studio.detail.StudioDetailActivity
import com.team.bpm.presentation.util.clickableWithoutRipple
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SearchResultActivity : BaseComponentActivityV2() {

    @Composable
    override fun InitComposeUi() {
        SearchResultActivityContent()
    }

    companion object {
        const val KEY_SEARCH = "search"

        fun newIntent(
            context: Context,
            search: String
        ): Intent {
            return Intent(context, SearchResultActivity::class.java).apply {
                putExtra(KEY_SEARCH, search)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SearchResultActivityContent(viewModel: SearchResultViewModel = hiltViewModel()) {
    val (state, event, effect) = use(viewModel)
    val context = getLocalContext()

    LaunchedEffect(Unit) {
        event.invoke(SearchResultContract.Event.GetSearchResult)
    }

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is SearchResultContract.Effect.ShowToast -> {
                    context.showToast(effect.text)
                }

                is SearchResultContract.Effect.GoToSearch -> {
                    context.finish()
                }

                is SearchResultContract.Effect.GoToStudioDetail -> {
                    context.startActivity(StudioDetailActivity.newIntent(context, effect.studioId))
                }
            }
        }
    }

    with(state) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
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
                            onClick = { event.invoke(SearchResultContract.Event.OnClickSearch) },
                            modifier = Modifier.padding(
                                start = 17.dp,
                                end = 12.dp
                            ),
                            textState = searchTextState,
                            label = null,
                            limit = null,
                            singleLine = true,
                            hint = null,
                            isExtendable = false,
                            minHeight = 40.dp,
                            iconPadding = 12.dp,
                            icon = { hasFocus ->
                                Icon(
                                    modifier = Modifier
                                        .padding(end = 4.dp)
                                        .size(32.dp)
                                        .align(Alignment.CenterEnd),
                                    painter = painterResource(id = R.drawable.ic_search),
                                    contentDescription = "searchIconButton",
                                    tint = if (hasFocus) GrayColor2 else GrayColor5
                                )
                            }
                        )
                    }
                }

                if (isFiltering) {
                    item {
                        val screenHeightDp = LocalConfiguration.current.screenHeightDp
                        Column(modifier = Modifier.fillMaxSize()) {
                            Box {
                                Divider(
                                    modifier = Modifier.align(BottomCenter),
                                    color = GrayColor8
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    verticalAlignment = CenterVertically
                                ) {
                                    BPMSpacer(width = 16.dp)

                                    Row(modifier = Modifier.weight(1f)) {
                                        Tab(
                                            text = "지역",
                                            focused = isRegionFiltering,
                                            onClick = { event.invoke(SearchResultContract.Event.OnClickRegionTab) }
                                        )

                                        Tab(
                                            text = "키워드",
                                            focused = !isRegionFiltering,
                                            onClick = { event.invoke(SearchResultContract.Event.OnClickKeywordTab) }
                                        )
                                    }

                                    BPMSpacer(width = 16.dp)

                                    Divider(
                                        modifier = Modifier
                                            .width(1.dp)
                                            .height(16.dp),
                                        color = GrayColor6
                                    )

                                    BPMSpacer(width = 16.dp)

                                    Row(
                                        modifier = Modifier.clickableWithoutRipple { event.invoke(
                                            SearchResultContract.Event.OnClickReset
                                        ) },
                                        verticalAlignment = CenterVertically
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_reset),
                                            contentDescription = "resetIcon",
                                            tint = MainBlackColor
                                        )

                                        BPMSpacer(width = 4.dp)

                                        Text(
                                            text = "초기화",
                                            fontWeight = Normal,
                                            fontSize = 14.sp,
                                            letterSpacing = 0.sp,
                                            color = MainBlackColor
                                        )
                                    }

                                    BPMSpacer(width = 16.dp)
                                }
                            }

                            if (isRegionFiltering) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .height((screenHeightDp - 184).dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .width(96.dp)
                                            .fillMaxHeight(),
                                        horizontalAlignment = CenterHorizontally
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(48.dp)
                                        ) {
                                            Text(
                                                modifier = Modifier.align(Center),
                                                text = "서울",
                                                fontWeight = SemiBold,
                                                fontSize = 15.sp,
                                                letterSpacing = 0.sp,
                                                color = MainBlackColor
                                            )
                                        }
                                    }

                                    Divider(
                                        modifier = Modifier
                                            .width(1.dp)
                                            .fillMaxHeight(),
                                        color = GrayColor8
                                    )

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight()
                                    ) {
                                        stringArrayResource(id = R.array.seoul).forEach { secondRegion ->
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(48.dp)
                                                    .background(color = if (state.secondRegion == secondRegion) MainGreenColor else Color.White)
                                                    .clickableWithoutRipple { event.invoke(
                                                        SearchResultContract.Event.OnClickSecondRegion(
                                                            secondRegion
                                                        )
                                                    ) }
                                            ) {
                                                Text(
                                                    modifier = Modifier
                                                        .padding(start = 24.dp)
                                                        .align(CenterStart),
                                                    text = secondRegion,
                                                    fontWeight = if (state.secondRegion == secondRegion) Bold else Normal,
                                                    fontSize = 14.sp,
                                                    letterSpacing = 0.sp,
                                                    color = if (state.secondRegion == secondRegion) MainBlackColor else GrayColor2
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (recommendKeywordMap.isEmpty()) {
                                    LoadingBlock()
                                    LaunchedEffect(Unit) {
                                        event.invoke(SearchResultContract.Event.GetKeywordList)
                                    }
                                } else {
                                    FlowRow(
                                        modifier = Modifier
                                            .padding(
                                                top = 20.dp,
                                                start = 16.dp,
                                                end = 16.dp
                                            )
                                            .fillMaxWidth()
                                            .height((screenHeightDp - 204).dp),
                                        mainAxisSpacing = 7.dp,
                                        crossAxisSpacing = 12.dp
                                    ) {
                                        recommendKeywordMap.forEach { mapItem ->
                                            ClickableKeywordChip(
                                                keyword = mapItem.key,
                                                isChosen = mapItem.value,
                                                onClick = { event(
                                                    SearchResultContract.Event.OnClickKeywordChip(
                                                        mapItem.key
                                                    )
                                                ) }
                                            )
                                        }
                                    }
                                }
                            }

                            RoundedCornerButton(
                                modifier = Modifier
                                    .padding(
                                        horizontal = 16.dp,
                                        vertical = 14.dp
                                    )
                                    .fillMaxWidth()
                                    .height(48.dp),
                                text = "필터 적용하기",
                                textColor = Color.White,
                                buttonColor = MainBlackColor,
                                onClick = {
                                    event.invoke(
                                        SearchResultContract.Event.OnClickSetFilter(
                                            recommendKeywordMap.filter { it.value }.keys.map {
                                                it.id ?: 0
                                            },
                                            secondRegion?.let { "$firstRegion $it" } ?: firstRegion
                                        )
                                    )
                                }
                            )
                        }
                    }
                } else {
                    stickyHeader {
                        val isStuckState = remember { mutableStateOf(false) }
                        Column(Modifier.onGloballyPositioned { coordinates -> isStuckState.value = coordinates.positionInRoot().y == 0f }) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                                    .background(color = Color.White),
                                horizontalArrangement = SpaceBetween,
                                verticalAlignment = CenterVertically
                            ) {
                                Row {
                                    Row(
                                        modifier = Modifier
                                            .padding(start = 16.dp)
                                            .clickableWithoutRipple { event.invoke(
                                                SearchResultContract.Event.OnClickRegionFilter
                                            ) }
                                            .clip(RoundedCornerShape(30.dp))
                                            .height(30.dp)
                                            .border(
                                                width = 1.dp,
                                                shape = RoundedCornerShape(30.dp),
                                                color = if (secondRegion != null) GrayColor4 else GrayColor8
                                            )
                                            .background(if (secondRegion != null) MainGreenColor else GrayColor11),
                                        verticalAlignment = CenterVertically
                                    ) {
                                        BPMSpacer(width = 15.dp)

                                        Text(
                                            text = secondRegion ?: "지역",
                                            fontWeight = Medium,
                                            fontSize = 13.sp,
                                            letterSpacing = 0.sp,
                                            color = GrayColor2
                                        )

                                        BPMSpacer(width = 2.dp)

                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_arrow_down_small),
                                            contentDescription = "regionFilterButtonIcon",
                                            tint = if (secondRegion != null) GrayColor4 else GrayColor6
                                        )

                                        BPMSpacer(width = 12.dp)
                                    }

                                    BPMSpacer(width = 8.dp)

                                    Row(
                                        modifier = Modifier
                                            .clickableWithoutRipple { event.invoke(
                                                SearchResultContract.Event.OnClickKeywordFilter
                                            ) }
                                            .clip(RoundedCornerShape(30.dp))
                                            .height(30.dp)
                                            .border(
                                                width = 1.dp,
                                                shape = RoundedCornerShape(30.dp),
                                                color = if (recommendKeywordMap.count { it.value } > 0) GrayColor4 else GrayColor8
                                            )
                                            .background(if (recommendKeywordMap.count { it.value } > 0) MainGreenColor else GrayColor11),
                                        verticalAlignment = CenterVertically
                                    ) {
                                        BPMSpacer(width = 15.dp)

                                        Text(
                                            text = if (recommendKeywordMap.count { it.value } > 1) {
                                                "${recommendKeywordMap.filter { it.value }.keys.first().keyword} 외 ${recommendKeywordMap.count { it.value } - 1}개"
                                            } else if (recommendKeywordMap.count { it.value } == 1) {
                                                recommendKeywordMap.filter { it.value }.keys.first().keyword ?: ""
                                            } else {
                                                "키워드"
                                            },
                                            fontWeight = Medium,
                                            fontSize = 13.sp,
                                            letterSpacing = 0.sp,
                                            color = GrayColor2
                                        )

                                        BPMSpacer(width = 2.dp)

                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_arrow_down_small),
                                            contentDescription = "keywordFilterButtonIcon",
                                            tint = if (recommendKeywordMap.isNotEmpty()) GrayColor4 else GrayColor6
                                        )

                                        BPMSpacer(width = 12.dp)
                                    }
                                }

                                Icon(
                                    modifier = Modifier
                                        .padding(end = 17.dp)
                                        .clickableWithoutRipple { event.invoke(if (isStuckState.value) SearchResultContract.Event.OnClickSearch else SearchResultContract.Event.OnClickFilter) },
                                    painter = painterResource(id = if (isStuckState.value) R.drawable.ic_search else R.drawable.ic_filter),
                                    contentDescription = "filterIcon",
                                    tint = if (isFiltered) GrayColor3 else GrayColor7
                                )
                            }

                            Divider(color = GrayColor9)
                        }
                    }

                    if (studioList.isNotEmpty()) {
                        itemsIndexed(studioList) { index, studio ->
                            StudioComposable(
                                studio = studio,
                                onClickStudio = { studioId -> event.invoke(
                                    SearchResultContract.Event.OnClickStudio(
                                        studioId
                                    )
                                ) },
                                onClickScrapButton = { studioId -> event.invoke(
                                    SearchResultContract.Event.OnClickScrap(
                                        studioId,
                                        index
                                    )
                                ) }
                            )
                        }
                    } else {
                        item {
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
                                        text = "아직 해당 업체 정보가 없어요\n첫 등록을 부탁드려요",
                                        fontWeight = Medium,
                                        fontSize = 12.sp,
                                        letterSpacing = 0.sp,
                                        textAlign = TextAlign.Center,
                                        color = GrayColor5
                                    )

                                    BPMSpacer(height = 15.dp)

                                    Box(
                                        modifier = Modifier
                                            .clip(shape = RoundedCornerShape(50.dp))
                                            .background(color = MainGreenColor)
                                            .clickable {}
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .padding(
                                                    horizontal = 14.dp,
                                                    vertical = 12.dp
                                                )
                                                .align(Center),
                                            text = "새 업체 등록하기",
                                            fontWeight = SemiBold,
                                            fontSize = 12.sp,
                                            letterSpacing = 0.sp,
                                            color = MainBlackColor
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
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
    focused: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .height(50.dp)
            .clickableWithoutRipple { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                modifier = Modifier.align(Center),
                text = text,
                fontSize = 14.sp,
                fontWeight = if (focused) SemiBold else Medium,
                letterSpacing = 0.sp,
                color = if (focused) MainBlackColor else GrayColor6
            )
        }

        Divider(
            thickness = 2.dp,
            color = if (focused) MainBlackColor else Color.Transparent
        )
    }
}