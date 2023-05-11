package com.team.bpm.presentation.ui.register_studio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.team.bpm.presentation.R
import com.team.bpm.presentation.base.BaseComponentActivityV2
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.*
import com.team.bpm.presentation.compose.theme.*
import com.team.bpm.presentation.ui.register_studio.register_location.RegisterLocationActivity
import com.team.bpm.presentation.util.addFocusCleaner
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

val dummyKeywordChipList = listOf(
    "친절해요",
    "소통이 빨라요",
    "소품이 다양해요",
    "세트장 구성이 다양해요",
    "제공하는 컨셉이 다양해요",
    "자연스럽게 연출해줘요",
    "시설이 깔끔해요",
    "원하는 스타일을 바로 파악해줘요",
    "주차하기 편해요",
    "보정을 꼼꼼하게 해줘요",
    "가격이 합리적이에요",
    "파우더룸이 잘 되어있어요",
    "요청사항을 잘 들어주세요",
    "스튜디오가 넓어요",
    "여유롭게 준비할 수 있어요"
)

@AndroidEntryPoint
class RegisterStudioActivity : BaseComponentActivityV2() {
    @Composable
    override fun InitComposeUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        RegisterStudioActivityContent()
    }
}

@Composable
private fun RegisterStudioActivityContent(
    viewModel: RegisterStudioViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel)
    val context = LocalContext.current as BaseComponentActivityV2
    val nameTextState = remember { mutableStateOf("") }
    val addressTextState = remember { mutableStateOf("") }
    val phoneNumberTextState = remember { mutableStateOf("") }
    val snsAddressTextState = remember { mutableStateOf("") }
    val businessHoursTextState = remember { mutableStateOf("") }
    val priceInfoTextState = remember { mutableStateOf("") }


    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is RegisterStudioContract.Effect.ShowToast -> {
                    context.showToast(effect.text)
                }
                RegisterStudioContract.Effect.GoToSetLocation -> {
                    context.startActivity(RegisterLocationActivity.newIntent(context))
                }
            }
        }
    }

    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    with(state) {
        Box {
            Column(
                modifier = Modifier
                    .windowInsetsPadding(insets = WindowInsets.systemBars.only(sides = WindowInsetsSides.Vertical))
                    .imePadding()
                    .fillMaxWidth()
                    .verticalScroll(state = scrollState)
                    .background(color = Color.White)
                    .addFocusCleaner(focusManager = focusManager)
            ) {
                ScreenHeader(header = "새 업체 등록하기")

                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .height(55.dp),
                    verticalAlignment = CenterVertically
                ) {
                    Text(
                        text = "업체 정보",
                        textAlign = TextAlign.Center,
                        fontWeight = SemiBold,
                        fontSize = 16.sp,
                        letterSpacing = 0.sp
                    )

                    Text(
                        text = "*",
                        fontWeight = SemiBold,
                        fontSize = 16.sp,
                        letterSpacing = 0.sp,
                        color = Color.Red
                    )
                }

                Divider(color = GrayColor13)

                BPMSpacer(height = 25.dp)

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    BPMTextField(
                        textState = nameTextState,
                        label = "업체 이름",
                        limit = null,
                        singleLine = true,
                        hint = "업체 이름을 입력해주세요"
                    )

                    BPMSpacer(height = 25.dp)

                    BPMTextField(
                        textState = addressTextState,
                        label = "위치",
                        hint = "업체 위치를 등록해주세요",
                        limit = null,
                        iconSize = 30.dp,
                        singleLine = true,
                        icon = {
                            Icon(
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .size(30.dp)
                                    .align(CenterEnd),
                                painter = painterResource(id = R.drawable.ic_location),
                                contentDescription = "locationIcon",
                                tint = GrayColor6
                            )
                        },
                        onClick = { event.invoke(RegisterStudioContract.Event.OnClickSetLocation) }
                    )
                }

                BPMSpacer(height = 30.dp)

                Divider(
                    thickness = 8.dp,
                    color = GrayColor11
                )

                BPMSpacer(height = 20.dp)

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "이런 점을 추천해요",
                        fontWeight = SemiBold,
                        fontSize = 16.sp,
                        letterSpacing = 0.sp
                    )

                    BPMSpacer(height = 8.dp)

                    Text(
                        text = "최대 5개까지 선택가능해요",
                        fontWeight = Medium,
                        fontSize = 12.sp,
                        letterSpacing = 0.sp,
                        color = GrayColor4
                    )
                }

                BPMSpacer(height = 14.dp)

                Divider(color = GrayColor13)

                BPMSpacer(height = 20.dp)

                FlowRow(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    mainAxisSpacing = 7.dp,
                    crossAxisSpacing = 12.dp
                ) {
                    dummyKeywordChipList.forEach { dummyKeyword -> // TODO : will be modified
                        KeywordChip(
                            text = dummyKeyword,
                            isChosen = recommendKeywordMap[dummyKeyword] ?: false,
                            onClick = { event.invoke(RegisterStudioContract.Event.OnClickKeywordChip(dummyKeyword)) }
                        )
                    }
                }

                BPMSpacer(height = 30.dp)

                Divider(
                    thickness = 8.dp,
                    color = GrayColor11
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(55.dp)
                ) {
                    Text(
                        modifier = Modifier.align(CenterStart),
                        text = "업체 추가 정보",
                        fontWeight = SemiBold,
                        fontSize = 16.sp,
                        letterSpacing = 0.sp
                    )
                }

                Divider(color = GrayColor8)

                BPMSpacer(height = 26.dp)

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    BPMTextField(
                        textState = phoneNumberTextState,
                        label = "전화번호",
                        hint = "000-0000-0000",
                        limit = null,
                        singleLine = false,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )

                    BPMSpacer(height = 22.dp)

                    BPMTextField(
                        textState = snsAddressTextState,
                        label = "SNS 주소",
                        limit = null,
                        hint = "인스타그램 @BodyProfileManager",
                        singleLine = false
                    )

                    BPMSpacer(height = 22.dp)

                    BPMTextField(
                        textState = businessHoursTextState,
                        label = "영업시간",
                        limit = null,
                        hint = "12:00~19:00",
                        singleLine = false
                    )

                    BPMSpacer(height = 22.dp)

                    BPMTextField(
                        textState = priceInfoTextState,
                        label = "가격정보",
                        limit = null,
                        hint = "프로필 0000원",
                        singleLine = false
                    )
                }

                BPMSpacer(height = 35.dp)

                RoundedCornerButton(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(48.dp),
                    text = "저장하기",
                    textColor = MainBlackColor,
                    buttonColor = MainGreenColor,
                    enabled = remember { mutableStateOf(nameTextState.value.isNotEmpty()) }.value,
                    onClick = { event.invoke(RegisterStudioContract.Event.OnClickSubmit(
                        name = nameTextState.value,
                        address = addressTextState.value,
                        latitude = state.latitude,
                        longitude = state.longitude,
                        phoneNumber = phoneNumberTextState.value,
                        snsAddress = snsAddressTextState.value,
                        businessHours = businessHoursTextState.value,
                        priceInfo = priceInfoTextState.value
                    )) }
                )

                BPMSpacer(height = 12.dp)
            }
        }
    }
}