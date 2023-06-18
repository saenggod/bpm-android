package com.team.bpm.presentation.ui.main.eyebody.posting

import android.content.Context
import android.content.Intent
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.team.bpm.presentation.compose.theme.GrayColor10
import com.team.bpm.presentation.compose.theme.GrayColor4
import com.team.bpm.presentation.compose.theme.GrayColor5
import com.team.bpm.presentation.compose.theme.GrayColor8
import com.team.bpm.presentation.compose.theme.MainBlackColor
import com.team.bpm.presentation.compose.theme.MainGreenColor
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class EyeBodyPostingActivity : BaseComponentActivityV2() {
    @Composable
    override fun InitComposeUi() {
        EyeBodyPostingActivityContent()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, EyeBodyPostingActivity::class.java)
        }
    }
}

@Composable
private fun EyeBodyPostingActivityContent(
    viewModel: EyeBodyPostingViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel)
    val context = getLocalContext()
    val imageLauncher = initImageLauncher(
        context = context,
        onSuccess = { uris, images ->
            event.invoke(EyeBodyPostingContract.Event.OnImagesAdded(uris.zip(images)))
        },
        onFailure = {

        }
    )
    val contentTextState = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        // TODO : Call Api
    }

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is EyeBodyPostingContract.Effect.ShowToast -> {
                    context.showToast(effect.text)
                }

                is EyeBodyPostingContract.Effect.AddImages -> {
                    imageLauncher.launch(PickVisualMediaRequest())
                }

                is EyeBodyPostingContract.Effect.RedirectToEyeBody -> {
//                    context.startActivity() TODO : Redirect to eyeBody detail screen
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
                    ScreenHeader("오늘의 눈바디 남기기")

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
                                    onClick = { event.invoke(EyeBodyPostingContract.Event.OnClickImagePlaceHolder) }
                                )
                            }
                        }

                        itemsIndexed(imageList, key = { _, pair ->
                            pair.first
                        }) { index, pair ->
                            ImagePlaceHolder(
                                image = pair.second,
                                onClick = {},
                                onClickRemove = { event.invoke(
                                    EyeBodyPostingContract.Event.OnClickRemoveImage(
                                        index
                                    )
                                ) }
                            )
                        }
                    }

                    BPMTextField(
                        modifier = Modifier
                            .padding(top = 22.dp)
                            .padding(horizontal = 16.dp),
                        textState = contentTextState,
                        minHeight = 180.dp,
                        limit = 300,
                        label = "오늘의 내 몸에 대한 이야기를 작성해주세요",
                        hint = "내용을 입력해주세요",
                        singleLine = false
                    )
                }

                Column {
                    Divider(
                        thickness = 1.dp,
                        color = GrayColor8
                    )

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth()
                            .height(64.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "공개 커뮤니티에 공유",
                            fontWeight = FontWeight.Normal,
                            fontSize = 15.sp,
                            letterSpacing = -(0.17).sp,
                            color = GrayColor4
                        )

                        Box(
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(60.dp))
                                .width(66.dp)
                                .height(28.dp)
                                .background(color = GrayColor10)
                        ) {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = "오픈예정",
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                letterSpacing = 0.sp,
                                color = GrayColor5
                            )
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
                        text = "저장하기",
                        textColor = MainBlackColor,
                        buttonColor = MainGreenColor,
                        onClick = { event.invoke(
                            EyeBodyPostingContract.Event.OnClickSubmit(
                                contentTextState.value
                            )
                        ) }
                    )
                }
            }

            if (isLoading) {
                LoadingScreen()
            }
        }
    }
}