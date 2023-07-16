package com.team.bpm.presentation.ui.main.bodyshape.detail.posting

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.team.bpm.presentation.base.BaseComponentActivityV2
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.*
import com.team.bpm.presentation.compose.theme.*
import com.team.bpm.presentation.ui.main.bodyshape.detail.BodyShapeDetailActivity
import com.team.bpm.presentation.ui.main.bodyshape.detail.posting.BodyShapeDetailPostingActivity.Companion.RESULT_OK
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class BodyShapeDetailPostingActivity : BaseComponentActivityV2() {
    @Composable
    override fun InitComposeUi() {
        BodyShapeDetailPostingActivityContent()
    }

    companion object {
        const val KEY_BUNDLE = "bundle"
        const val KEY_ALBUM_ID = "album_id"
        const val KEY_BODY_SHAPE_ID = "body_shape_id"
        const val RESULT_OK = 200

        fun newIntent(
            context: Context,
            albumId: Int,
            bodyShapeId: Int? = null
        ): Intent {
            val mBodyShapeId = bodyShapeId ?: -1
            return Intent(context, BodyShapeDetailPostingActivity::class.java).putExtra(
                KEY_BUNDLE, bundleOf(
                    KEY_ALBUM_ID to albumId,
                    KEY_BODY_SHAPE_ID to mBodyShapeId
                )
            )
        }
    }
}

@Composable
private fun BodyShapeDetailPostingActivityContent(
    viewModel: BodyShapeDetailPostingViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel)
    val context = getLocalContext()
    val imageLauncher = initImageLauncher(
        context = context,
        onSuccess = { uris, images ->
            event.invoke(BodyShapeDetailPostingContract.Event.OnImagesAdded(uris.zip(images)))
        },
        onFailure = {

        }
    )
    val contentTextState = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        event.invoke(BodyShapeDetailPostingContract.Event.GetBodyShapeContent)
    }

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is BodyShapeDetailPostingContract.Effect.OnContentLoaded -> {
                    contentTextState.value = effect.content

                    val loadedImageList = mutableListOf<Pair<Uri, ImageBitmap>>()
                    effect.images.forEach { imagePath ->
                        Glide.with(context).asBitmap().load(imagePath).addListener(object : RequestListener<Bitmap> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                                return false
                            }

                            override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                resource?.let {
                                    loadedImageList.add(Pair(imagePath.toUri(), resource.asImageBitmap()))
                                }

                                if (loadedImageList.size == effect.images.size) {
                                    event.invoke(
                                        BodyShapeDetailPostingContract.Event.SetImageListWithLoadedImageList(
                                            loadedImageList
                                        )
                                    )
                                }
                                return true
                            }
                        }).submit()
                    }
                }

                is BodyShapeDetailPostingContract.Effect.ShowToast -> {
                    context.showToast(effect.text)
                }

                is BodyShapeDetailPostingContract.Effect.AddImages -> {
                    imageLauncher.launch(PickVisualMediaRequest())
                }

                is BodyShapeDetailPostingContract.Effect.RedirectToBodyShape -> {
                    if (!effect.newIntentNeeded) {
                        context.setResult(RESULT_OK)
                    } else {
                        context.startActivity(BodyShapeDetailActivity.newIntent(context, effect.albumId, effect.bodyShapeId, effect.dDay))
                    }

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
                                    onClick = { event.invoke(BodyShapeDetailPostingContract.Event.OnClickImagePlaceHolder) }
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
                                        BodyShapeDetailPostingContract.Event.OnClickRemoveImage(
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
                        onClick = {
                            event.invoke(
                                BodyShapeDetailPostingContract.Event.OnClickSubmit(
                                    contentTextState.value
                                )
                            )
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