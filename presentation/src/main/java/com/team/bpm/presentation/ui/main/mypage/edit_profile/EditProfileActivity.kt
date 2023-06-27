package com.team.bpm.presentation.ui.main.mypage.edit_profile

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.team.bpm.presentation.util.convertUriToBitmap
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class EditProfileActivity : BaseComponentActivityV2() {

    @Composable
    override fun InitComposeUi() {
        EditProfileActivityContent()
    }

    companion object {
        const val RESULT_OK = 200
    }
}

@Composable
private fun EditProfileActivityContent(viewModel: EditProfileViewModel = hiltViewModel()) {
    val (state, event, effect) = use(viewModel)
    val context = getLocalContext()
    val profileImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            runCatching {
                uri?.let { convertUriToBitmap(context.contentResolver, it) }
            }.onSuccess { image ->
                image?.let { event.invoke(EditProfileContract.Event.OnImageAdded(it.asImageBitmap())) }
            }.onFailure {
                event.invoke(EditProfileContract.Event.OnError("이미지를 불러 올 수 없습니다."))
            }
        })
    val nicknameTextState = remember { mutableStateOf("") }
    val bioTextState = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        event.invoke(EditProfileContract.Event.GetProfile)
    }

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is EditProfileContract.Effect.OnProfileLoaded -> {
                    effect.userProfile.nickname?.let { nickname ->
                        nicknameTextState.value = nickname
                    }

                    effect.userProfile.bio?.let { bio ->
                        bioTextState.value = bio
                    }

                    effect.userProfile.image?.let { imagePath ->
                        Glide.with(context).asBitmap().load(imagePath).addListener(object : RequestListener<Bitmap> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                                return false
                            }

                            override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                resource?.let { bitmap ->
                                    event.invoke(EditProfileContract.Event.OnImageAdded(bitmap.asImageBitmap()))
                                }

                                return true
                            }
                        }).submit()
                    }
                }

                is EditProfileContract.Effect.ShowToast -> {
                    context.showToast(effect.text)
                }

                is EditProfileContract.Effect.AddImage -> {
                    profileImageLauncher.launch(PickVisualMediaRequest())
                }

                is EditProfileContract.Effect.EditSuccess -> {
                    context.setResult(EditProfileActivity.RESULT_OK)
                    context.finish()
                }
            }
        }
    }

    with(state) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                ScreenHeader(header = "프로필 생성하기")

                BPMSpacer(height = 44.dp)

                Column(
                    modifier = Modifier.align(CenterHorizontally),
                    horizontalAlignment = CenterHorizontally
                ) {
                    image?.let {
                        Image(
                            modifier = Modifier
                                .clip(shape = CircleShape)
                                .size(130.dp)
                                .align(CenterHorizontally),
                            bitmap = image,
                            contentDescription = "profileImage",
                            contentScale = Crop
                        )
                    }

                    BPMSpacer(height = 16.dp)

                    Text(
                        modifier = Modifier.clickable { event.invoke(EditProfileContract.Event.OnClickAddImage) },
                        text = "프로필 사진 등록",
                        fontWeight = Medium,
                        fontSize = 15.sp,
                        letterSpacing = 0.sp,
                        textDecoration = TextDecoration.Underline,
                        color = GrayColor12
                    )
                }

                BPMSpacer(height = 50.dp)

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Column {
                        Row(modifier = Modifier.padding(start = 2.dp)) {
                            Text(
                                text = "닉네임",
                                fontWeight = Medium,
                                fontSize = 16.sp,
                                letterSpacing = 0.sp
                            )

                            Text(
                                text = "*",
                                fontWeight = Medium,
                                fontSize = 16.sp,
                                letterSpacing = 0.sp,
                                color = Color.Red
                            )
                        }

                        BPMTextField(
                            modifier = Modifier.padding(top = 10.dp),
                            textState = nicknameTextState,
                            label = null,
                            limit = null,
                            singleLine = true,
                            hint = "어깨_매니저",
                            allocatedErrorCode = "409",
                            occurredErrorCode = errorCode,
                            errorMessage = "중복된 닉네임입니다."
                        )
                    }

                    BPMSpacer(height = 24.dp)

                    Text(
                        text = "한 줄 소개",
                        fontWeight = Medium,
                        fontSize = 16.sp,
                        letterSpacing = 0.sp
                    )

                    BPMTextField(
                        modifier = Modifier.padding(top = 10.dp),
                        textState = bioTextState,
                        label = null,
                        limit = null,
                        singleLine = true,
                        hint = "회원님 반갑습니다. 제 특기는 어깨춤 추기입니다.",
                    )
                }
            }

            RoundedCornerButton(
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp,
                        vertical = 13.dp
                    )
                    .fillMaxWidth()
                    .height(48.dp)
                    .align(BottomCenter),
                text = "저장하기",
                textColor = if (nicknameTextState.value.isNotEmpty()) MainBlackColor else GrayColor7,
                buttonColor = if (nicknameTextState.value.isNotEmpty()) MainGreenColor else GrayColor11,
                onClick = { event.invoke(EditProfileContract.Event.OnClickSubmit(nicknameTextState.value, bioTextState.value)) }
            )

            if (isLoading) {
                LoadingScreen()
            }
        }
    }
}