package com.team.bpm.presentation.ui.main.studio.register.register_location

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.bpm.presentation.R.drawable
import com.team.bpm.presentation.base.BaseComponentActivityV2
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.*
import com.team.bpm.presentation.compose.theme.GrayColor3
import com.team.bpm.presentation.compose.theme.GrayColor6
import com.team.bpm.presentation.compose.theme.GrayColor7
import com.team.bpm.presentation.compose.theme.GrayColor8
import com.team.bpm.presentation.compose.theme.MainBlackColor
import com.team.bpm.presentation.compose.theme.MainGreenColor
import com.team.bpm.presentation.util.clickableWithoutRipple
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import net.daum.mf.map.api.MapView.MapViewEventListener

@AndroidEntryPoint
class RegisterLocationActivity : BaseComponentActivityV2() {

    @Composable
    override fun InitComposeUi() {
        RegisterLocationActivityContent()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, RegisterLocationActivity::class.java)
        }
    }
}

@Composable
private fun RegisterLocationActivityContent(
    viewModel: RegisterLocationViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel)
    val context = getLocalContext()
    val focusManager = LocalFocusManager.current
    val geocoder = Geocoder(context)

    val tempLatitude = remember { mutableStateOf(0.0) }
    val tempLongitude = remember { mutableStateOf(0.0) }
    val mapViewEventListener = remember {
        mutableStateOf(object : MapViewEventListener {
            override fun onMapViewInitialized(p0: MapView?) {}
            override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {}
            override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {}
            override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {}
            override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {}
            override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {}
            override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {}
            override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {}
            override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
                p1?.mapPointGeoCoord?.let {
                    with(it) {
                        if (String.format("%.6f", latitude) != (37.5667).toString() ||
                            String.format("%.6f", longitude) != (126.9783).toString()
                        ) {
                            tempLatitude.value = latitude
                            tempLongitude.value = longitude
                        }
                    }
                }
            }
        })
    }

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is RegisterLocationContract.Effect.ShowToast -> {
                    context.showToast(effect.text)
                }
            }
        }
    }

    with(state) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(bottom = 74.dp)
                    .fillMaxSize()
            ) {
                ScreenHeader(header = "위치 등록하기")

                BPMSpacer(height = 12.dp)

                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth()
                        .height(34.dp)
                ) {
                    Box {
                        val searchTextState = remember { mutableStateOf("") }

                        TextFieldColorProvider {
                            BasicTextField(
                                modifier = Modifier
                                    .padding(end = 42.dp)
                                    .fillMaxWidth()
                                    .align(Center),
                                value = searchTextState.value,
                                onValueChange = { searchTextState.value = it },
                                textStyle = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 13.sp,
                                    letterSpacing = 0.sp,
                                    color = MainBlackColor
                                ),
                                cursorBrush = SolidColor(GrayColor3),
                                singleLine = true,
                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                            )
                        }

                        Icon(
                            modifier = Modifier
                                .size(42.dp)
                                .clickableWithoutRipple {
                                    focusManager.clearFocus()

                                }
                                .align(CenterEnd),
                            painter = painterResource(id = drawable.ic_search),
                            contentDescription = "searchLocationIcon"
                        )
                    }

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(BottomCenter),
                        color = GrayColor7,
                        thickness = 1.dp
                    )
                }

                BPMSpacer(height = 12.dp)

                Box(modifier = Modifier.fillMaxSize()) {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { context ->
                            MapView(context).apply {
                                setMapCenterPoint(
                                    MapPoint.mapPointWithGeoCoord(
                                        37.5667,
                                        126.9783
                                    ),
                                    false
                                )

                                setMapViewEventListener(mapViewEventListener.value)
                            }
                        }
                    )

                    Image(
                        modifier = Modifier
                            .size(54.dp)
                            .align(Center),
                        painter = painterResource(id = drawable.ic_marker),
                        contentDescription = "mapMarkerIcon"
                    )

                    Box(
                        modifier = Modifier
                            .padding(bottom = 30.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(50.dp)
                            )
                            .clip(shape = RoundedCornerShape(50.dp))
                            .border(
                                width = 1.dp,
                                color = if (latitude != 0.0 && longitude != 0.0) GrayColor3 else GrayColor8,
                                shape = RoundedCornerShape(50.dp)
                            )
                            .height(36.dp)
                            .background(color = Color.White)
                            .align(BottomCenter)
                            .clickable {
                                if (tempLatitude.value != 0.0 && tempLongitude.value != 0.0) {
                                    event.invoke(
                                        RegisterLocationContract.Event.OnClickChangeLocation(
                                            tempLatitude.value,
                                            tempLongitude.value
                                        )
                                    )
                                }
                            },
                    ) {
                        Row(
                            modifier = Modifier.align(Center),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BPMSpacer(width = 14.dp)

                            Text(
                                modifier = Modifier.padding(vertical = 12.dp),
                                text = "이 위치로 변경하기",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                letterSpacing = 0.sp,
                                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                                color = if (tempLatitude.value == 0.0 && tempLongitude.value == 0.0) GrayColor6 else MainBlackColor
                            )

                            BPMSpacer(width = 14.dp)
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(74.dp)
                    .align(BottomCenter)
            ) {
                RoundedCornerButton(
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp,
                            vertical = 13.dp
                        )
                        .fillMaxWidth()
                        .fillMaxSize(),
                    text = "위치 등록하기",
                    textColor = MainBlackColor,
                    buttonColor = MainGreenColor,
                    onClick = { }
                )
            }
        }
    }
}