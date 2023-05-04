package com.team.bpm.presentation.ui.main.community.community_posting

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.bpm.presentation.base.BaseComponentActivityV2
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.BPMTextField
import com.team.bpm.presentation.compose.Header
import com.team.bpm.presentation.compose.ImagePlaceHolder
import com.team.bpm.presentation.compose.RoundedCornerButton
import com.team.bpm.presentation.compose.theme.MainBlackColor
import com.team.bpm.presentation.compose.theme.MainGreenColor
import com.team.bpm.presentation.util.convertUriToBitmap
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
    val context = LocalContext.current as BaseComponentActivityV2

    val addImageLauncher = rememberLauncherForActivityResult(
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
                event.invoke(CommunityPostingContract.Event.OnImagesAdded(images.mapIndexed { index, image ->
                    Pair(uris[index], image.asImageBitmap())
                }))
            }.onFailure {

            }
        })

    LaunchedEffect(Unit) {
        // TODO : Call Api
    }

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is CommunityPostingContract.Effect.GoBack -> {
                    context.finish()
                }
                is CommunityPostingContract.Effect.AddImages -> {
                    addImageLauncher.launch(PickVisualMediaRequest())
                }
                is CommunityPostingContract.Effect.RemoveImage -> {

                }
            }
        }
    }


    with(state) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = SpaceBetween
        ) {
            Column {
                Header(
                    title = "커뮤니티 글 작성하기",
                    onClickBackButton = { event.invoke(CommunityPostingContract.Event.OnClickBackButton) }
                )

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
                            onClickRemove = { event.invoke(CommunityPostingContract.Event.OnClickRemoveImage(index)) }
                        )
                    }
                }

                val bodyTextState = remember { mutableStateOf("") }

                BPMTextField(
                    modifier = Modifier
                        .padding(top = 22.dp)
                        .padding(horizontal = 16.dp),
                    textState = bodyTextState,
                    minHeight = 180.dp,
                    label = "내용을 적어주세요",
                    limit = 300,
                    singleLine = true,
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
                onClick = { }
            )
        }
    }
}