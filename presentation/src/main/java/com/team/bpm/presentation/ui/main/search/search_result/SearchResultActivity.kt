package com.team.bpm.presentation.ui.main.search.search_result

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.bpm.presentation.R
import com.team.bpm.presentation.base.BaseComponentActivityV2
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.*
import com.team.bpm.presentation.compose.theme.*
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
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        event.invoke(SearchResultContract.Event.GetSearchResult)
    }

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is SearchResultContract.Effect.ShowToast -> {
                    context.showToast(effect.text)
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
                            keyboardActions = KeyboardActions(onDone = {
                                focusManager.clearFocus()
                                if (searchTextState.value.isNotEmpty()) {
                                    event.invoke(SearchResultContract.Event.OnClickSearch(searchTextState.value))
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
                                                event.invoke(SearchResultContract.Event.OnClickSearch(searchTextState.value))
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

                if (isFiltering) {

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
                                            .clickableWithoutRipple { }
                                            .clip(RoundedCornerShape(30.dp))
                                            .height(30.dp)
                                            .border(
                                                width = 1.dp,
                                                shape = RoundedCornerShape(30.dp),
                                                color = if (filteredRegion != null) GrayColor4 else GrayColor8
                                            )
                                            .background(if (filteredRegion != null) MainGreenColor else GrayColor11),
                                        verticalAlignment = CenterVertically
                                    ) {
                                        BPMSpacer(width = 15.dp)

                                        Text(
                                            text = filteredRegion ?: "지역",
                                            fontWeight = Medium,
                                            fontSize = 13.sp,
                                            letterSpacing = 0.sp,
                                            color = GrayColor2
                                        )

                                        BPMSpacer(width = 2.dp)

                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_arrow_down_small),
                                            contentDescription = "regionFilterButtonIcon",
                                            tint = if (isFiltered) GrayColor4 else GrayColor6
                                        )

                                        BPMSpacer(width = 12.dp)
                                    }

                                    BPMSpacer(width = 8.dp)

                                    Row(
                                        modifier = Modifier
                                            .clickableWithoutRipple { }
                                            .clip(RoundedCornerShape(30.dp))
                                            .height(30.dp)
                                            .border(
                                                width = 1.dp,
                                                shape = RoundedCornerShape(30.dp),
                                                color = if (filteredRegion != null) GrayColor4 else GrayColor8
                                            )
                                            .background(if (filteredRegion != null) MainGreenColor else GrayColor11),
                                        verticalAlignment = CenterVertically
                                    ) {
                                        BPMSpacer(width = 15.dp)

                                        Text(
                                            text = if (filteredKeywordList.isNotEmpty() && filteredKeywordList.size > 1) {
                                                "${filteredKeywordList[0]} 외 ${filteredKeywordList.size - 1}개"
                                            } else if (filteredKeywordList.isNotEmpty()) {
                                                filteredKeywordList[0]
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
                                            tint = if (isFiltered) GrayColor4 else GrayColor6
                                        )

                                        BPMSpacer(width = 12.dp)
                                    }
                                }

                                Icon(
                                    modifier = Modifier
                                        .padding(end = 17.dp)
                                        .clickableWithoutRipple { },
                                    painter = painterResource(id = if (isStuckState.value) R.drawable.ic_search else R.drawable.ic_filter),
                                    contentDescription = "filterIcon",
                                    tint = if (isFiltered) GrayColor3 else GrayColor7
                                )
                            }

                            Divider(color = GrayColor9)
                        }
                    }

                    items(studioList) { studio ->
                        StudioComposable(
                            studio = studio,
                            onClickStudio = {},
                            onClickScrapButton = {}
                        )
                    }
                }
            }

            if (isLoading) {
                LoadingScreen()
            }
        }
    }
}