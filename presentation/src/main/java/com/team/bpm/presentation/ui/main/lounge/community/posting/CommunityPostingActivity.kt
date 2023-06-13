package com.team.bpm.presentation.ui.main.lounge.community.posting

import android.content.Context
import android.content.Intent
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.bpm.presentation.base.BaseComponentActivityV2
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.*
import com.team.bpm.presentation.compose.theme.MainBlackColor
import com.team.bpm.presentation.compose.theme.MainGreenColor
import com.team.bpm.presentation.ui.main.lounge.community.detail.CommunityDetailActivity
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CommunityPostingActivity : BaseComponentActivityV2() {
    @Composable
    override fun InitComposeUi() {
        CommunityPostingActivityContent()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, CommunityPostingActivity::class.java)
        }
    }
}

@Composable
private fun CommunityPostingActivityContent(
    viewModel: CommunityPostingViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel)
    val context = getLocalContext()
    val imageLauncher = initImageLauncher(
        context = getLocalContext(),
        onSuccess = { uris, images ->
            event.invoke(CommunityPostingContract.Event.OnImagesAdded(uris.zip(images)))
        },
        onFailure = {

        }
    )
    val contentTextState = remember { mutableStateOf("") }

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is CommunityPostingContract.Effect.ShowToast -> {
                    context.showToast(effect.text)
                }

                is CommunityPostingContract.Effect.AddImages -> {
                    imageLauncher.launch(PickVisualMediaRequest())
                }

                is CommunityPostingContract.Effect.RedirectToCommunity -> {
                    context.startActivity(CommunityDetailActivity.newIntent(context, effect.communityId))
                    context.finish()
                }
            }
        }
    }

    with(state) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = SpaceBetween
            ) {
                Column {
                    ScreenHeader(header = "커뮤니티 글 작성하기")

                    LazyRow(
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = spacedBy(14.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        if (imageList.size < 5) {
                            item {
                                ImagePlaceHolder(
                                    image = null,
                                    onClick = { event.invoke(CommunityPostingContract.Event.OnClickImagePlaceHolder) }
                                )
                            }
                        }

                        itemsIndexed(imageList, key = { _, pair ->
                            pair.first
                        }) { index, pair ->
                            ImagePlaceHolder(
                                image = pair.second,
                                onClick = {},
                                onClickRemove = {
                                    event.invoke(
                                        CommunityPostingContract.Event.OnClickRemoveImage(
                                            index
                                        )
                                    )
                                }
                            )
                        }
                    }

                    BPMTextField(
                        modifier = Modifier
                            .padding(top = 22.dp)
                            .padding(horizontal = 16.dp),
                        textState = contentTextState,
                        minHeight = 180.dp,
                        label = "내용을 적어주세요",
                        limit = 300,
                        singleLine = false,
                        hint = "내용을 입력해주세요",
                    )
                }

                RoundedCornerButton(
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp,
                            vertical = 14.dp
                        )
                        .fillMaxWidth()
                        .height(48.dp),
                    text = "저장하기",
                    textColor = MainBlackColor,
                    buttonColor = MainGreenColor,
                    onClick = { event.invoke(CommunityPostingContract.Event.OnClickSubmit(contentTextState.value)) }
                )
            }

            if (isLoading) {
                LoadingScreen()
            }
        }
    }
}