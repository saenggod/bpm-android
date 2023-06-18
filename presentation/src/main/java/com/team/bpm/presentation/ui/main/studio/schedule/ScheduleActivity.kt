package com.team.bpm.presentation.ui.main.studio.schedule

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.bpm.presentation.R
import com.team.bpm.presentation.base.BaseComponentActivityV2
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.*
import com.team.bpm.presentation.compose.theme.*
import com.team.bpm.presentation.ui.main.studio.schedule.ScheduleActivity.Companion.KEY_STUDIO_NAME
import com.team.bpm.presentation.ui.main.studio.schedule.select.SelectStudioActivity
import com.team.bpm.presentation.util.addFocusCleaner
import com.team.bpm.presentation.util.clickableWithoutRipple
import com.team.bpm.presentation.util.repeatCallDefaultOnResume
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

@AndroidEntryPoint
class ScheduleActivity : BaseComponentActivityV2() {
    @Composable
    override fun InitComposeUi() {
        ScheduleActivityContent()
    }

    companion object {
        const val KEY_SCHEDULE_ID = "schedule_id"
        const val KEY_STUDIO_NAME = "studio_name"

        fun newIntent(
            context: Context,
            scheduleId: Int?
        ): Intent {
            return Intent(context, ScheduleActivity::class.java).putExtra(
                KEY_SCHEDULE_ID, scheduleId
            )
        }
    }
}

@OptIn(ExperimentalSnapperApi::class)
@Composable
private fun ScheduleActivityContent(
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel)
    val context = getLocalContext()
    val hoursLazyListState = rememberLazyListState()
    val minutesLazyListState = rememberLazyListState()
    val timeZonesLazyListState = rememberLazyListState()
    val selectStudioLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == SelectStudioActivity.RESULT_OK) {
            result.data?.getStringExtra(KEY_STUDIO_NAME)?.let { studioName ->
                context.repeatCallDefaultOnResume {
                    event.invoke(ScheduleContract.Event.SetStudio(studioName))
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        event.invoke(ScheduleContract.Event.GetSchedule)
    }

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is ScheduleContract.Effect.GoToSelectStudio -> {
                    selectStudioLauncher.launch(SelectStudioActivity.newIntent(context))
                }
            }
        }
    }

    with(state) {
        val scrollState = rememberScrollState()
        val scheduleNameState = remember { mutableStateOf("") }
        val memoState = remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .windowInsetsPadding(insets = WindowInsets.systemBars.only(sides = WindowInsetsSides.Vertical))
                .fillMaxSize()
                .imePadding()
                .verticalScroll(state = scrollState)
                .background(color = Color.White)
                .addFocusCleaner(LocalFocusManager.current)
                .nestedScroll(connection = object : NestedScrollConnection {
                    override suspend fun onPostFling(
                        consumed: Velocity,
                        available: Velocity
                    ): Velocity {
                        return Velocity(0f, available.y)
                    }

                    override fun onPostScroll(
                        consumed: Offset,
                        available: Offset,
                        source: NestedScrollSource
                    ): Offset {
                        return Offset(0f, available.y)
                    }
                }),
            verticalArrangement = SpaceBetween
        ) {
            Column {
                ScreenHeader(
                    header = if (isEditing) "일정 등록하기" else "체형관리 일정",
                    actionBlock = {
                        if (!isEditing) {
                            Text(
                                modifier = Modifier.clickableWithoutRipple { event.invoke(
                                    ScheduleContract.Event.OnClickEdit
                                ) },
                                text = "수정",
                                fontWeight = Medium,
                                fontSize = 14.sp,
                                letterSpacing = 0.sp,
                                color = GrayColor5
                            )
                        }
                    }
                )

                ScheduleItemLayout(
                    isEditing = isEditing,
                    isEssential = true,
                    label = "무엇을 위한 바디프로필인가요?",
                    value = scheduleNameState.value.ifEmpty { fetchedScheduleName ?: "일정 이름" },
                    extraContentHeight = 60.dp
                ) {
                    BPMTextField(
                        textState = scheduleNameState,
                        label = null,
                        limit = null,
                        singleLine = true,
                        hint = "일정 이름을 설정해주세요 예) 여름맞이 다이어트"
                    )
                }

                Divider(color = GrayColor8)

                ScheduleItemLayout(
                    isEditing = isEditing,
                    isEssential = true,
                    label = "일정을 완료할 날짜를 입력해주세요.",
                    value = if (selectedDate != null) selectedDate.toString().replace("-", ".") + " (${
                        when (selectedDate.dayOfWeek) {
                            DayOfWeek.MONDAY -> "월"
                            DayOfWeek.TUESDAY -> "화"
                            DayOfWeek.WEDNESDAY -> "수"
                            DayOfWeek.THURSDAY -> "목"
                            DayOfWeek.FRIDAY -> "금"
                            DayOfWeek.SATURDAY -> "토"
                            DayOfWeek.SUNDAY -> "일"
                            null -> ""
                        }
                    })" else "디데이 날짜",
                    extraContentHeight = 352.dp
                ) {
                    val currentDate = LocalDate.now()
                    val calendarState = remember { mutableStateOf(LocalDate.now()) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        horizontalArrangement = SpaceBetween,
                        verticalAlignment = CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 24.dp),
                            text = "${calendarState.value.year}년 " +
                                    "${
                                        when (calendarState.value.month.name) {
                                            "JANUARY" -> "1"
                                            "FEBRUARY" -> "2"
                                            "MARCH" -> "3"
                                            "APRIL" -> "4"
                                            "MAY" -> "5"
                                            "JUNE" -> "6"
                                            "JULY" -> "7"
                                            "AUGUST" -> "8"
                                            "SEPTEMBER" -> "9"
                                            "OCTOBER" -> "10"
                                            "NOVEMBER" -> "11"
                                            else -> "12"
                                        }
                                    }월",
                            fontWeight = SemiBold,
                            fontSize = 16.sp,
                            letterSpacing = 0.sp
                        )

                        Row(modifier = Modifier.padding(end = 36.dp)) {
                            Icon(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clickableWithoutRipple {
                                        calendarState.value = calendarState.value.minusMonths(1)
                                    },
                                painter = painterResource(id = R.drawable.ic_calendar_back),
                                contentDescription = "backIcon"
                            )

                            BPMSpacer(width = 30.dp)

                            Icon(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clickableWithoutRipple {
                                        calendarState.value = calendarState.value.plusMonths(1)
                                    },
                                painter = painterResource(id = R.drawable.ic_calendar_forth),
                                contentDescription = "forthIcon"
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth()
                            .height(40.dp),
                        horizontalArrangement = SpaceEvenly,
                        verticalAlignment = CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.size(40.dp),
                            text = "M",
                            textAlign = TextAlign.Center,
                            fontWeight = Medium,
                            fontSize = 12.sp,
                            letterSpacing = 0.sp
                        )

                        Text(
                            modifier = Modifier.size(40.dp),
                            text = "T",
                            textAlign = TextAlign.Center,
                            fontWeight = Medium,
                            fontSize = 12.sp,
                            letterSpacing = 0.sp
                        )

                        Text(
                            modifier = Modifier.size(40.dp),
                            text = "W",
                            textAlign = TextAlign.Center,
                            fontWeight = Medium,
                            fontSize = 12.sp,
                            letterSpacing = 0.sp
                        )

                        Text(
                            modifier = Modifier.size(40.dp),
                            text = "T",
                            textAlign = TextAlign.Center,
                            fontWeight = Medium,
                            fontSize = 12.sp,
                            letterSpacing = 0.sp
                        )

                        Text(
                            modifier = Modifier.size(40.dp),
                            text = "F",
                            textAlign = TextAlign.Center,
                            fontWeight = Medium,
                            fontSize = 12.sp,
                            letterSpacing = 0.sp
                        )

                        Text(
                            modifier = Modifier.size(40.dp),
                            text = "S",
                            textAlign = TextAlign.Center,
                            fontWeight = Medium,
                            fontSize = 12.sp,
                            letterSpacing = 0.sp
                        )

                        Text(
                            modifier = Modifier.size(40.dp),
                            text = "S",
                            textAlign = TextAlign.Center,
                            fontWeight = Medium,
                            fontSize = 12.sp,
                            letterSpacing = 0.sp
                        )
                    }

                    val dateArray = Array<LocalDate?>(42) { null }
                    val firstDayIndexOfMonth = when (calendarState.value.withDayOfMonth(1).dayOfWeek) {
                        DayOfWeek.MONDAY -> 0
                        DayOfWeek.TUESDAY -> 1
                        DayOfWeek.WEDNESDAY -> 2
                        DayOfWeek.THURSDAY -> 3
                        DayOfWeek.FRIDAY -> 4
                        DayOfWeek.SATURDAY -> 5
                        DayOfWeek.SUNDAY -> 6
                        null -> 0
                    }

                    for (i in 0 until calendarState.value.lengthOfMonth()) {
                        dateArray[firstDayIndexOfMonth + i] = calendarState.value.withDayOfMonth(1).plusDays(i.toLong())
                    }

                    repeat(6) { week ->
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .fillMaxWidth()
                                .height(40.dp),
                            horizontalArrangement = SpaceEvenly,
                            verticalAlignment = CenterVertically
                        ) {
                            repeat(7) { day ->
                                val thisDay = dateArray[(7 * week) + day]
                                val dayBackgroundColorState = animateColorAsState(
                                    targetValue = if (selectedDate != null &&
                                        selectedDate == thisDay
                                    ) MainBlackColor else Color.White
                                )
                                val dayTextColorState = animateColorAsState(
                                    targetValue = if (selectedDate != null &&
                                        selectedDate == thisDay
                                    ) MainGreenColor else GrayColor2
                                )

                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(shape = CircleShape)
                                        .border(
                                            width = 1.dp,
                                            shape = CircleShape,
                                            color = if (currentDate == thisDay) MainGreenColor else Color.Transparent
                                        )
                                        .background(color = dayBackgroundColorState.value)
                                        .clickableWithoutRipple {
                                            if (thisDay != null && thisDay.toEpochDay() >= currentDate.toEpochDay()) {
                                                event.invoke(ScheduleContract.Event.OnClickDate(thisDay))
                                            }
                                        },
                                ) {
                                    Text(
                                        modifier = Modifier.align(Center),
                                        text = if (thisDay != null) "${thisDay.dayOfMonth}" else "",
                                        fontWeight = Medium,
                                        fontSize = 16.sp,
                                        letterSpacing = 0.sp,
                                        color = if (thisDay != null &&
                                            thisDay.toEpochDay() < currentDate.toEpochDay()
                                        ) GrayColor6
                                        else dayTextColorState.value
                                    )
                                }
                            }
                        }
                    }
                }

                Divider(color = GrayColor8)

                ScheduleItemLayout(
                    isEditing = isEditing,
                    isEssential = false,
                    label = "메모를 남겨주세요",
                    value = memoState.value.ifEmpty { fetchedMemo ?: "메모" },
                    extraContentHeight = 130.dp
                ) {
                    BPMTextField(
                        minHeight = 110.dp,
                        textState = memoState,
                        label = null,
                        limit = null,
                        singleLine = false,
                        hint = "일정에 대한 메모를 입력해주세요."
                    )
                }

                Divider(color = GrayColor8)

                Divider(
                    modifier = Modifier.height(8.dp),
                    color = GrayColor11
                )

                ScheduleItemLayout(
                    isEditing = isEditing,
                    isEssential = false,
                    label = "예약한 촬영 스튜디오가 있으신가요?",
                    value = selectedStudioName ?: "스튜디오 이름",
                    extraContentHeight = 60.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(10.dp),
                                color = GrayColor7
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(
                                    start = 16.dp,
                                    end = 8.dp
                                )
                                .fillMaxWidth()
                                .align(Center)
                                .clickableWithoutRipple { event.invoke(ScheduleContract.Event.OnClickSearchStudio) },
                            horizontalArrangement = SpaceBetween,
                            verticalAlignment = CenterVertically
                        ) {
                            Text(
                                text = "바디프로필 업체를 검색해보세요",
                                fontWeight = Medium,
                                fontSize = 14.sp,
                                letterSpacing = 0.sp,
                                color = GrayColor7
                            )

                            Icon(
                                modifier = Modifier.size(32.dp),
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = "searchIcon",
                                tint = GrayColor15
                            )
                        }
                    }
                }

                Divider(color = GrayColor8)

                ScheduleItemLayout(
                    isEditing = isEditing,
                    isEssential = false,
                    label = "예약 시간을 입력해주세요.",
                    value = selectedTime ?: "시간",
                    extraContentHeight = 212.dp
                ) {
                    val hours = (0..13).toList()
                    val minutes = (-1..60).toList()
                    val timeZones = listOf("", "오후", "오전", "")
                    val scope = rememberCoroutineScope()

                    LaunchedEffect(Unit) {
                        selectedTime?.let { selectedTime ->
                            hoursLazyListState.scrollToItem(selectedTime.substring(0, 2).toInt().minus(1))
                            minutesLazyListState.scrollToItem(selectedTime.substring(3, 5).toInt())
                            timeZonesLazyListState.scrollToItem(if (selectedTime.substring(7,9) == "오후") 0 else 1)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    ) {
                        Row(
                            modifier = Modifier.align(Center),
                            horizontalArrangement = Arrangement.Center
                        ) {

                            LazyColumn(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(120.dp),
                                state = hoursLazyListState,
                                flingBehavior = rememberSnapperFlingBehavior(hoursLazyListState),
                                horizontalAlignment = CenterHorizontally
                            ) {
                                itemsIndexed(hours) { index, hour ->
                                    Box(modifier = Modifier.size(40.dp)) {
                                        val textColorState = animateColorAsState(targetValue = if (index == remember { derivedStateOf { hoursLazyListState.firstVisibleItemIndex } }.value + 1) MainBlackColor else GrayColor5)

                                        Text(
                                            modifier = Modifier
                                                .align(Center)
                                                .clickableWithoutRipple {
                                                    scope.launch {
                                                        if (index != 0) hoursLazyListState.animateScrollToItem(
                                                            index - 1
                                                        )
                                                    }
                                                },
                                            text = if (hour in 1..12) String.format("%02d", hour) else if (hour == 0) "시" else "",
                                            fontWeight = SemiBold,
                                            fontSize = 14.sp,
                                            letterSpacing = 0.sp,
                                            color = textColorState.value
                                        )
                                    }
                                }
                            }

                            Icon(
                                modifier = Modifier.align(CenterVertically),
                                painter = painterResource(id = R.drawable.ic_time_divider),
                                contentDescription = "timeDividerIcon"
                            )

                            LazyColumn(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(120.dp),
                                state = minutesLazyListState,
                                flingBehavior = rememberSnapperFlingBehavior(minutesLazyListState),
                                horizontalAlignment = CenterHorizontally
                            ) {
                                itemsIndexed(minutes) { index, minute ->
                                    val textColorState = animateColorAsState(targetValue = if (index == remember { derivedStateOf { minutesLazyListState.firstVisibleItemIndex } }.value + 1) MainBlackColor else GrayColor5)

                                    Box(modifier = Modifier.size(40.dp)) {
                                        Text(
                                            modifier = Modifier
                                                .align(Center)
                                                .clickableWithoutRipple {
                                                    scope.launch {
                                                        if (index != 0) minutesLazyListState.animateScrollToItem(
                                                            index - 1
                                                        )
                                                    }
                                                },
                                            text = if (minute in 0..59) String.format("%02d", minute) else if (minute == -1) "분" else "",
                                            fontWeight = SemiBold,
                                            fontSize = 14.sp,
                                            letterSpacing = 0.sp,
                                            color = textColorState.value
                                        )
                                    }
                                }
                            }

                            LazyColumn(
                                modifier = Modifier
                                    .width(50.dp)
                                    .height(120.dp),
                                state = timeZonesLazyListState,
                                flingBehavior = rememberSnapperFlingBehavior(timeZonesLazyListState),
                                horizontalAlignment = CenterHorizontally
                            ) {
                                itemsIndexed(timeZones) { index, times ->
                                    val textColorState = animateColorAsState(targetValue = if (index == remember { derivedStateOf { timeZonesLazyListState.firstVisibleItemIndex } }.value + 1) MainBlackColor else GrayColor5)

                                    Box(modifier = Modifier.size(40.dp)) {
                                        Text(
                                            modifier = Modifier
                                                .align(Center)
                                                .clickableWithoutRipple {
                                                    scope.launch {
                                                        if (index != 0) timeZonesLazyListState.animateScrollToItem(
                                                            index - 1
                                                        )
                                                    }
                                                },
                                            text = times,
                                            fontWeight = SemiBold,
                                            fontSize = 14.sp,
                                            letterSpacing = 0.sp,
                                            color = textColorState.value
                                        )
                                    }
                                }
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            verticalArrangement = SpaceEvenly,
                            horizontalAlignment = CenterHorizontally
                        ) {
                            Divider(
                                modifier = Modifier.width(210.dp),
                                color = GrayColor8
                            )
                            Divider(
                                modifier = Modifier.width(210.dp),
                                color = GrayColor8
                            )
                        }
                    }

                    BPMSpacer(height = 30.dp)

                    Box(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(8.dp))
                            .height(42.dp)
                            .width(210.dp)
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(8.dp),
                                color = GrayColor5
                            )
                            .align(CenterHorizontally)
                            .clickable {
                                val hour = "${if (hours[hoursLazyListState.firstVisibleItemIndex + 1] < 10) "0" else ""}${hours[hoursLazyListState.firstVisibleItemIndex + 1]}"
                                val minute = "${if (minutes[minutesLazyListState.firstVisibleItemIndex + 1] < 10) "0" else ""}${minutes[minutesLazyListState.firstVisibleItemIndex + 1]}"
                                val time = "$hour:$minute (${timeZones[timeZonesLazyListState.firstVisibleItemIndex + 1]})"
                                event.invoke(ScheduleContract.Event.OnClickSetTime(time))
                            }
                    ) {
                        Text(
                            modifier = Modifier.align(Center),
                            text = "확인",
                            fontWeight = SemiBold,
                            fontSize = 14.sp,
                            letterSpacing = 0.sp,
                            color = GrayColor3
                        )
                    }
                }

                Divider(color = GrayColor8)
            }

            Column {
                RoundedCornerButton(
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp,
                            vertical = 14.dp
                        )
                        .fillMaxWidth()
                        .height(48.dp),
                    text = "저장하기",
                    borderColor = if (isEditing) MainGreenColor else GrayColor9,
                    enabled = isEditing,
                    textColor = MainBlackColor,
                    buttonColor = MainGreenColor,
                    onClick = { event.invoke(
                        ScheduleContract.Event.OnClickSubmit(
                            scheduleNameState.value,
                            memoState.value
                        )
                    ) }
                )
            }
        }
    }
}

@Composable
private fun ScheduleItemLayout(
    isEditing: Boolean,
    isEssential: Boolean,
    label: String,
    value: String,
    extraContentHeight: Dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val collapsedHeight = 77.dp
    val expandState = remember { mutableStateOf(false) }
    val columnHeightState = animateDpAsState(targetValue = if (expandState.value) collapsedHeight + extraContentHeight else collapsedHeight)
    val focusManager = LocalFocusManager.current

    if (!isEditing) {
        focusManager.clearFocus()
        expandState.value = false
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 18.dp)
            .fillMaxWidth()
            .height(columnHeightState.value)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(collapsedHeight)
                .clickableWithoutRipple {
                    if (isEditing) {
                        expandState.value = !expandState.value
                        focusManager.clearFocus()
                    }
                }
        ) {
            Column(modifier = Modifier.align(Center)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = SpaceBetween,
                    verticalAlignment = CenterVertically
                ) {
                    Row {
                        Text(
                            text = label,
                            fontWeight = SemiBold,
                            fontSize = 12.sp,
                            letterSpacing = 0.sp,
                            color = GrayColor5
                        )

                        if (isEssential) {
                            Text(
                                text = "*",
                                fontWeight = SemiBold,
                                fontSize = 12.sp,
                                letterSpacing = 0.sp,
                                color = Color.Red
                            )
                        }
                    }

                    Box(modifier = Modifier.size(22.dp)) {
                        if (isEditing) {
                            Icon(
                                modifier = Modifier
                                    .size(22.dp)
                                    .rotate(if (expandState.value) 180f else 0f),
                                painter = painterResource(id = R.drawable.ic_arrow_expand_0),
                                contentDescription = "expandItemIcon",
                                tint = if (expandState.value) GrayColor2 else GrayColor5
                            )
                        }
                    }
                }

                Row(modifier = Modifier.padding(end = 36.dp)) {
                    Text(
                        modifier = Modifier
                            .height(24.dp)
                            .align(CenterVertically),
                        text = value,
                        textAlign = TextAlign.Center,
                        fontWeight = Medium,
                        fontSize = 17.sp,
                        letterSpacing = 0.sp,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (isEssential) {
                        Text(
                            text = "*",
                            fontWeight = Medium,
                            fontSize = 17.sp,
                            letterSpacing = 0.sp,
                            color = Color.Red
                        )
                    }
                }
            }
        }

        BPMSpacer(height = 1.dp)

        if (!expandState.value) {
            Box {
                Column {
                    content()
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickableWithoutRipple { }
                )
            }
        } else {
            content()
        }
    }
}