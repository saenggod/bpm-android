package com.team.bpm.presentation.ui.main.mypage.myscrap

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.bpm.presentation.base.BaseComponentActivityV2
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.LoadingScreen
import com.team.bpm.presentation.compose.ScreenHeader
import com.team.bpm.presentation.compose.StudioComposable
import com.team.bpm.presentation.compose.getLocalContext
import com.team.bpm.presentation.compose.theme.GrayColor11
import com.team.bpm.presentation.compose.theme.GrayColor5
import com.team.bpm.presentation.ui.main.studio.detail.StudioDetailActivity
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MyScrapActivity : BaseComponentActivityV2() {

    @Composable
    override fun InitComposeUi() {
        MyScrapActivityContent()
    }

    companion object {

        fun newIntent(context : Context) : Intent {
            return Intent(context, MyScrapActivity::class.java)
        }
    }
}

@Composable
private fun MyScrapActivityContent(
    viewModel: MyScrapViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel)
    val context = getLocalContext()

    LaunchedEffect(Unit) {
        event.invoke(MyScrapContract.Event.GetMyScrapList)
    }

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is MyScrapContract.Effect.ShowToast -> {
                    context.showToast(effect.text)
                }

                is MyScrapContract.Effect.RefreshMyScrapList -> {
                    event.invoke(MyScrapContract.Event.GetMyScrapList)
                }

                is MyScrapContract.Effect.GoToStudioDetail -> {
                    context.startActivity(StudioDetailActivity.newIntent(context, effect.studioId))
                }
            }
        }
    }

    with(state) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                ScreenHeader(header = "스크랩")

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .background(color = GrayColor11)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .align(CenterStart),
                        text = "${scrapCount}개의 스크랩",
                        fontWeight = Medium,
                        fontSize = 13.sp,
                        letterSpacing = 0.sp,
                        color = GrayColor5
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .padding(top = 102.dp)
                    .fillMaxSize()
            ) {
                items(myScrapList) { studio ->
                    StudioComposable(
                        studio = studio,
                        onClickStudio = { studioId ->
                            event.invoke(MyScrapContract.Event.OnClickStudio(studioId))
                        },
                        onClickScrapButton = { studioId ->
                            event.invoke(MyScrapContract.Event.OnClickCancelScrap(studioId))
                        }
                    )
                }
            }

            if (isLoading) {
                LoadingScreen()
            }
        }
    }
}