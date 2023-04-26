package com.team.bpm.presentation.ui.main.community.community_posting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.bpm.presentation.base.BaseComponentActivity
import com.team.bpm.presentation.base.BaseViewModel
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.BPMTextField
import com.team.bpm.presentation.compose.Header
import com.team.bpm.presentation.compose.ImagePlaceHolder
import com.team.bpm.presentation.compose.RoundedCornerButton
import com.team.bpm.presentation.compose.theme.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityPostingActivity : BaseComponentActivity() {
    override val viewModel: BaseViewModel
        get() = TODO("Not yet implemented")

    override fun initUi() {
        initComposeUi {
            CommunityPostingActivityContent()
        }
    }

    override fun setupCollect() {

    }
}

@Composable
private fun CommunityPostingActivityContent(
    viewModel: CommunityPostingViewModel = hiltViewModel()
) {
    val (state, effect, event) = use(viewModel)

    LaunchedEffect(Unit) {
        // TODO : Call Api
    }

    with(state) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = SpaceBetween
        ) {
            Column {
                Header(
                    title = "커뮤니티 글 작성하기",
                    onClickBackButton = {}
                )

                LazyRow(
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = spacedBy(14.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    item {
                        ImagePlaceHolder(image = null, onClick = { })
                    }

                    items(imageList) {

                    }
                }

                val bodyTextState = remember { mutableStateOf("") }

                BPMTextField(
                    modifier = Modifier
                        .padding(top = 22.dp)
                        .padding(horizontal = 16.dp),
                    textState = bodyTextState,
                    minHeight = 180.dp,
                    limit = 300,
                    hint = "내용을 입력해주세요",
                    label = "오늘의 내 몸에 대한 이야기를 작성해주세요",
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
                    horizontalArrangement = SpaceBetween,
                    verticalAlignment = CenterVertically
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
                            modifier = Modifier.align(Center),
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
                    textColor = Color.Black,
                    buttonColor = MainGreenColor,
                    onClick = { }
                )
            }
        }
    }
}