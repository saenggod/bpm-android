package com.team.bpm.presentation.ui.main.community.question_posting

import android.content.Context
import android.content.Intent
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.team.bpm.presentation.compose.BPMTextField
import com.team.bpm.presentation.compose.ImagePlaceHolder
import com.team.bpm.presentation.compose.LoadingScreen
import com.team.bpm.presentation.compose.RoundedCornerButton
import com.team.bpm.presentation.compose.ScreenHeader
import com.team.bpm.presentation.compose.getLocalContext
import com.team.bpm.presentation.compose.initImageLauncher
import com.team.bpm.presentation.compose.theme.MainBlackColor
import com.team.bpm.presentation.compose.theme.MainGreenColor
import com.team.bpm.presentation.ui.main.community.question_detail.QuestionDetailActivity
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class QuestionPostingActivity : BaseComponentActivityV2() {
    @Composable
    override fun InitComposeUi() {
        QuestionPostingActivityContent()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, QuestionPostingActivity::class.java)
        }
    }
}

@Composable
private fun QuestionPostingActivityContent(
    viewModel: QuestionPostingViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel)
    val context = getLocalContext()
    val imageLauncher = initImageLauncher(
        context = context,
        onSuccess = { uris, images ->
            event.invoke(QuestionPostingContract.Event.OnImagesAdded(uris.zip(images)))
        },
        onFailure = {

        }
    )
    val titleTextState = remember { mutableStateOf("") }
    val contentTextState = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        // TODO : Call Api
    }

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is QuestionPostingContract.Effect.ShowToast -> {
                    context.showToast(effect.text)
                }

                is QuestionPostingContract.Effect.AddImages -> {
                    imageLauncher.launch(PickVisualMediaRequest())
                }

                is QuestionPostingContract.Effect.RedirectToQuestion -> {
                    context.startActivity(QuestionDetailActivity.newIntent(context, effect.questionId))
                    context.finish()
                }
            }
        }
    }

    with(state) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    ScreenHeader("운동인에게 질문하기")

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
                        textState = contentTextState,
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
                    textColor = MainBlackColor,
                    buttonColor = MainGreenColor,
                    onClick = { event.invoke(QuestionPostingContract.Event.OnClickSubmit(titleTextState.value, contentTextState.value)) }
                )
            }
        }

        if (isLoading) {
            LoadingScreen()
        }
    }
}