package com.team.bpm.presentation.ui.main.mypage.question_posting

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.bpm.presentation.base.BaseComponentActivity
import com.team.bpm.presentation.base.BaseViewModel
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.BPMTextField
import com.team.bpm.presentation.compose.Header
import com.team.bpm.presentation.compose.ImagePlaceHolder
import com.team.bpm.presentation.compose.RoundedCornerButton
import com.team.bpm.presentation.compose.theme.*
import com.team.bpm.presentation.util.convertUriToBitmap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class QuestionPostingActivity : BaseComponentActivity() {
    override val viewModel: BaseViewModel
        get() = TODO("Not yet implemented")

    override fun initUi() {
        initComposeUi {
            QuestionPostingActivityContent()
        }
    }

    override fun setupCollect() {

    }
}

@Composable
private fun QuestionPostingActivityContent(
    viewModel: QuestionPostingViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel)
    val context = LocalContext.current as BaseComponentActivity

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
                event.invoke(QuestionPostingContract.Event.OnImagesAdded(images.mapIndexed { index, image ->
                    Pair(uris[index], image.asImageBitmap())
                }))
            }.onFailure {

            }
        })

    LaunchedEffect(Unit) {
        // TODO : Call Api
    }

    LaunchedEffect(effect) {
        effect.collectLatest { _effect ->
            when (_effect) {
                is QuestionPostingContract.Effect.GoBack -> {
                    context.finish()
                }
                is QuestionPostingContract.Effect.AddImages -> {
                    addImageLauncher.launch(PickVisualMediaRequest())
                }
                is QuestionPostingContract.Effect.RemoveImage -> {

                }
            }
        }
    }


    with(state) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Header(
                    title = "운동인에게 질문하기",
                    onClickBackButton = { event.invoke(QuestionPostingContract.Event.OnClickBackButton) }
                )

                LazyRow(
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    if (imageList.size < 5) {
                        item {
                            ImagePlaceHolder(
                                image = null,
                                onClick = { event.invoke(QuestionPostingContract.Event.OnClickImagePlaceHolder) }
                            )
                        }
                    }

                    itemsIndexed(imageList, key = { _, pair ->
                        pair.first
                    }) { index, pair ->
                        ImagePlaceHolder(
                            image = pair.second,
                            onClick = {},
                            onClickRemove = { event.invoke(QuestionPostingContract.Event.OnClickRemoveImage(index)) }
                        )
                    }
                }

                val titleTextState = remember { mutableStateOf("") }
                val bodyTextState = remember { mutableStateOf("") }

                BPMTextField(
                    modifier = Modifier
                        .padding(top = 22.dp)
                        .padding(horizontal = 16.dp),
                    textState = titleTextState,
                    minHeight = 42.dp,
                    limit = 50,
                    hint = "제목",
                    label = "무엇이 궁금하신가요?",
                    singleLine = false
                )

                BPMTextField(
                    modifier = Modifier
                        .padding(top = 22.dp)
                        .padding(horizontal = 16.dp),
                    textState = bodyTextState,
                    minHeight = 180.dp,
                    limit = 300,
                    hint = "내용을 입력해주세요",
                    label = "질문 내용을 상세히 적어주세요",
                    singleLine = false
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
                textColor = Color.Black,
                buttonColor = MainGreenColor,
                onClick = { }
            )
        }
    }
}