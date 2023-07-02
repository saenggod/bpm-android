package com.team.bpm.presentation.ui.intro.splash

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kakao.sdk.user.UserApiClient
import com.team.bpm.presentation.R
import com.team.bpm.presentation.base.BaseComponentActivityV2
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.BPMSpacer
import com.team.bpm.presentation.compose.getLocalContext
import com.team.bpm.presentation.compose.theme.MainBlackColor
import com.team.bpm.presentation.ui.main.MainActivity
import com.team.bpm.presentation.ui.intro.sign_up.SignUpActivity
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseComponentActivityV2() {

    @Composable
    override fun InitComposeUi() {
        SplashActivityContent()
    }
}

@Composable
private fun SplashActivityContent(
    viewModel: SplashViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel = viewModel)
    val context = getLocalContext()
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    LaunchedEffect(Unit) {
        delay(1000)
        event.invoke(SplashContract.Event.OnStart)
    }

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is SplashContract.Effect.ShowToast -> {
                    context.showToast(effect.text)
                }

                is SplashContract.Effect.Init -> {
                    event.invoke(SplashContract.Event.GetStoredUserInfo)
                }

                is SplashContract.Effect.GetKakaoUserInfo -> {
                    val kakaoLoginInstance = UserApiClient.instance
                    kakaoLoginInstance.loginWithKakaoAccount(context) { loginInfo, error ->
                        if (loginInfo != null) {
                            kakaoLoginInstance.me { user, _->
                                user?.id?.let { kakaoId ->
                                    user.kakaoAccount?.profile?.nickname?.let { kakaoNickname ->
                                        event.invoke(
                                            SplashContract.Event.OnSuccessKakaoLogin(
                                                kakaoId,
                                                kakaoNickname
                                            )
                                        )
                                    }
                                } ?: run {
                                    event.invoke(SplashContract.Event.OnFailureKakaoLogin)
                                }
                            }
                        } else {
                            event.invoke(SplashContract.Event.OnFailureKakaoLogin)
                        }
                    }
                }

                is SplashContract.Effect.GoToMainActivity -> {
                    context.startActivity(MainActivity.newIntent(context))
                }

                is SplashContract.Effect.GoToSignUpActivity -> {
                    context.startActivity(SignUpActivity.newIntent(context, effect.kakaoId, effect.kakaoNickname))
                    context.finish()
                }
            }
        }
    }

    with(state) {
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
                    .align(Alignment.TopEnd)
                    .padding(top = (screenHeightDp * 0.25).dp)
                    .height((screenWidthDp * 0.13).dp)
            ) {
                Image(
                    modifier = Modifier
                        .align(Alignment.Center)
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

            val startButtonAlphaState = animateFloatAsState(
                targetValue = if (isSignUpNeeded) 1f else 0f,
                animationSpec = tween(500)
            )

            Box(modifier = Modifier
                .alpha(startButtonAlphaState.value)
                .padding(bottom = 30.dp)
                .padding(horizontal = 16.dp)
                .clip(shape = RoundedCornerShape(6.dp))
                .fillMaxWidth()
                .height(48.dp)
                .background(Color(0xFFFEE500))
                .align(Alignment.BottomCenter)
                .clickable { event.invoke(SplashContract.Event.OnClickKakaoButton) }
            ) {
                Icon(
                    modifier = Modifier
                        .padding(start = 18.dp)
                        .size(24.dp)
                        .align(Alignment.CenterStart),
                    painter = painterResource(id = R.drawable.ic_kakao),
                    contentDescription = "kakaoIcon"
                )

                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "카카오톡으로 시작하기",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    letterSpacing = 0.sp,
                    color = MainBlackColor
                )
            }
        }
    }
}