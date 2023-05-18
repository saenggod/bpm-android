package com.team.bpm.presentation.ui.splash

import android.annotation.SuppressLint
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kakao.sdk.user.UserApiClient
import com.team.bpm.presentation.R
import com.team.bpm.presentation.base.BaseComponentActivity
import com.team.bpm.presentation.compose.BPMSpacer
import com.team.bpm.presentation.compose.theme.*
import com.team.bpm.presentation.ui.main.MainActivity
import com.team.bpm.presentation.ui.sign_up.SignUpActivity
import com.team.bpm.presentation.util.repeatCallDefaultOnStarted
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseComponentActivity() {

    override val viewModel: SplashViewModel by viewModels()

    private val startButtonVisibilityState = mutableStateOf(false)

    private val kakaoLoginInstance: UserApiClient by lazy {
        UserApiClient.instance
    }

    override fun initUi() {
        setContent {
            BPMTheme {
                SplashActivityContent(
                    startButtonVisibilityState = startButtonVisibilityState,
                    onClickStartButton = {
                        setupLogin()
                    }
                )
            }
        }
    }

    override fun setupCollect() {
        repeatCallDefaultOnStarted {
            viewModel.state.collectLatest { state ->
                when (state) {
                    SplashState.Init -> {
                        MainScope().launch {
                            delay(1000L)
                            viewModel.getStoredUserInfo()
                        }
                    }
                    is SplashState.ValidationCheck -> {
                        viewModel.sendKakaoIdVerification(state.id, state.kakaoNickName)
                    }
                    is SplashState.SignUp -> {
                        goToSignUp(state.id, state.kakaoNickName)
                    }
                    SplashState.NoUserInfo -> {
                        startButtonVisibilityState.value = true
                    }
                    is SplashState.SaveToken -> {
                        // TODO : 캡슐화에 대한 생각이 추가되어야 할 듯
                        state.token?.let { viewModel.saveUserToken(it) }
                    }
                    SplashState.Finish -> {
                        goToMainActivity()
                    }
                    is SplashState.Error -> {
                        this@SplashActivity.showToast("로그인 중 오류가 발생하였습니다. 다시 시도해주세요")
                    }
                }
            }
        }
    }

    private fun setupLogin() {
        // 카카오톡으로 로그인
        if (kakaoLoginInstance.isKakaoTalkLoginAvailable(this)) {
            kakaoLoginInstance.loginWithKakaoTalk(this) { loginInfo, error ->
                if (error != null) {
                    // 로그인 실패
//                    showDebugToast("login failed! cause : ${error.message}")
                    showToast("로그인에 실패하였습니다. 다시 시도해 주세요.")
                } else if (loginInfo != null) {
                    // 로그인 성공
                    kakaoLoginInstance.me { user, error ->
                        viewModel.setKakaoUserId(user?.id ?: 0L, user?.kakaoAccount?.profile?.nickname ?: "")
//                        user?.id?.let { viewModel.setKakaoUserId(it) }
//                        showDebugToast("login succeed. user token : ${user?.id}")
                    }
                }
            }
        } else {
            showToast("로그인에 실패하였습니다. 다시 시도해 주세요.")
        }
    }

    private fun goToSignUp(
        kakaoUserId: Long?,
        kakaoNickName: String
    ) {
        startActivity(
            SignUpActivity.newIntent(this, kakaoUserId, kakaoNickName)
        )
        finish()
    }

    private fun goToMainActivity() {
        startActivity(MainActivity.newIntent(this))
        finish()
    }
}

@Composable
private fun SplashActivityContent(
    startButtonVisibilityState: MutableState<Boolean>,
    onClickStartButton: () -> Unit
) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    val startButtonAlphaState = animateFloatAsState(
        targetValue = if (startButtonVisibilityState.value) 1f else 0f,
        animationSpec = tween(500)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        Image(
            modifier = Modifier
                .padding(top = ((screenHeightDp * 0.165).dp))
                .width((screenWidthDp * 0.75).dp),
            painter = painterResource(id = R.drawable.splash_b),
            contentDescription = "splashBImage"
        )

        Image(
            modifier = Modifier
                .padding(
                    top = (screenHeightDp * 0.25).dp,
                    start = (screenWidthDp * 0.37).dp
                )
                .size((screenWidthDp * 0.13).dp),
            painter = painterResource(id = R.drawable.splash_light),
            contentDescription = "splashLightImage"
        )

        Box(
            modifier = Modifier
                .align(TopEnd)
                .padding(top = (screenHeightDp * 0.25).dp)
                .height((screenWidthDp * 0.13).dp)
        ) {
            Image(
                modifier = Modifier
                    .align(Center)
                    .width((screenWidthDp * 0.433).dp),
                painter = painterResource(id = R.drawable.splash_line),
                contentDescription = "splashLineImage"
            )
        }

        Column(
            modifier = Modifier
                .padding(
                    top = (screenHeightDp * 0.43).dp,
                    start = (screenWidthDp * 0.09).dp
                )
        ) {
            Image(
                modifier = Modifier
                    .width((screenWidthDp * 0.375).dp),
                painter = painterResource(id = R.drawable.splash_text),
                contentDescription = "splashLineImage"
            )

            BPMSpacer(height = (screenHeightDp * 0.015).dp)

            Image(
                modifier = Modifier
                    .width((screenWidthDp * 0.344).dp),
                painter = painterResource(id = R.drawable.splash_logo),
                contentDescription = "splashLineImage"
            )
        }

        Box(modifier = Modifier
            .alpha(startButtonAlphaState.value)
            .padding(bottom = 30.dp)
            .padding(horizontal = 16.dp)
            .clip(shape = RoundedCornerShape(6.dp))
            .fillMaxWidth()
            .height(48.dp)
            .background(Color(0xFFFEE500))
            .align(BottomCenter)
            .clickable {
                onClickStartButton()
            }
        ) {
            Icon(
                modifier = Modifier
                    .padding(start = 18.dp)
                    .size(24.dp)
                    .align(CenterStart),
                painter = painterResource(id = R.drawable.ic_kakao),
                contentDescription = "kakaoIcon"
            )

            Text(
                modifier = Modifier.align(Center),
                text = "카카오톡으로 시작하기",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                letterSpacing = 0.sp,
                color = MainBlackColor
            )
        }
    }
}