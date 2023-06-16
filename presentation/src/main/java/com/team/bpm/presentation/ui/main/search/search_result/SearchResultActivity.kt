package com.team.bpm.presentation.ui.main.search.search_result

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.bpm.presentation.R
import com.team.bpm.presentation.base.BaseComponentActivityV2
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.BPMTextField
import com.team.bpm.presentation.compose.StudioComposable
import com.team.bpm.presentation.compose.getLocalContext
import com.team.bpm.presentation.compose.theme.GrayColor2
import com.team.bpm.presentation.compose.theme.GrayColor5
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
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(start = 14.dp)
                            .size(26.dp)
                            .align(Alignment.CenterVertically)
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

            items(studioList) { studio ->
                StudioComposable(
                    studio = studio,
                    onClickStudio = {},
                    onClickScrapButton = {}
                )
            }
        }
    }
}