package org.example.bigdisplayproject.ui.schedule

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate
import org.example.bigdisplayproject.ui.schedule.store.ScheduleStore
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import org.example.bigdisplayproject.domain.usecases.schedule.CalendarEvent
import org.example.bigdisplayproject.ui.components.BottomPanel
import org.example.bigdisplayproject.ui.components.Scroller
import org.example.bigdisplayproject.ui.components.myShadow
import org.example.bigdisplayproject.ui.components.verticalScrollbar
import org.example.bigdisplayproject.ui.util.pxToDp
import org.example.bigdisplayproject.ui.theme.DarkGray
import org.example.bigdisplayproject.ui.theme.GradientColor1
import org.example.bigdisplayproject.ui.theme.GradientColor10
import org.example.bigdisplayproject.ui.theme.GradientColor11
import org.example.bigdisplayproject.ui.theme.GradientColor12
import org.example.bigdisplayproject.ui.theme.GradientColor13
import org.example.bigdisplayproject.ui.theme.GradientColor14
import org.example.bigdisplayproject.ui.theme.GradientColor15
import org.example.bigdisplayproject.ui.theme.GradientColor2
import org.example.bigdisplayproject.ui.theme.GradientColor3
import org.example.bigdisplayproject.ui.theme.GradientColor4
import org.example.bigdisplayproject.ui.theme.GradientColor5
import org.example.bigdisplayproject.ui.theme.GradientColor6
import org.example.bigdisplayproject.ui.theme.GradientColor7
import org.example.bigdisplayproject.ui.theme.GradientColor8
import org.example.bigdisplayproject.ui.theme.GradientColor9
import org.example.bigdisplayproject.ui.theme.LightWhite
import org.example.bigdisplayproject.ui.theme.LinearGradientButton1
import org.example.bigdisplayproject.ui.theme.LinearGradientButton2
import java.io.File
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun Schedule(
    onBackButtonClick: () -> Unit,
    state: ScheduleStore.State,
    getSchedule: (String) -> Unit,
    getCalendarData: (String) -> Unit,
    parseCalendar: (String) -> Unit,
    getEvents: (List<CalendarEvent>, LocalDate) -> Unit
) {
    val group = remember { mutableStateOf("") }
    var currentDate by remember { mutableStateOf(ZonedDateTime.now(ZoneId.of("Europe/Moscow"))) }

    LaunchedEffect(group.value) {
        if (group.value.length == 10) {  // получаем расписание, как только введено название группы
            getSchedule(group.value)
        }
    }

    Scaffold(
        bottomBar = { BottomPanel(onButtonClick = { onBackButtonClick() }) }
    ) { paddingValues ->
        GradientBox {
            Row(
                horizontalArrangement = Arrangement.spacedBy((167).pxToDp())
            ) {
                Box(
                    modifier = Modifier
                        //.fillMaxWidth()
                        .weight(1f)
                        .padding(
                            top = (122).pxToDp(),
                            start = (187).pxToDp(),
                            bottom = (227).pxToDp()
                        )
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy((50).pxToDp()),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val buttonSymbols = listOf(
                            "0", "1", "2", "3", "4",
                            "5", "6", "7", "8", "9",
                            "А", "Б", "В", "И", "К",
                            "М", "Н", "О", "С", "<"
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height((150).pxToDp())
                                .myShadow(
                                    color = LightWhite,
                                    borderRadius = 38.dp,
                                    blurRadius = 2.dp,
                                    spread = 3f.dp
                                )
                                .padding(8.dp)
                                .clip(RoundedCornerShape(26.dp)),
                            colors = CardDefaults.cardColors(
                                containerColor = LightWhite,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    modifier = Modifier.wrapContentSize().padding(16.dp),
                                    text = group.value,
                                    fontSize = 50.sp,
                                    color = DarkGray
                                )
                            }
                        }

                        ButtonGrid(
                            symbols = buttonSymbols,
                            onButtonClick = { symbol ->
                                println("Pressed button: $symbol")
                                when (symbol) {
                                    "<" -> {
                                        if (group.value.isNotEmpty()) {
                                            if (group.value.last() == '-') group.value =
                                                group.value.substring(0, group.value.length - 2)
                                            else group.value =
                                                group.value.substring(0, group.value.length - 1)
                                        }
                                    }

                                    else -> {
                                        if (group.value.length < 10) group.value += symbol
                                        if (group.value.length == 4) group.value += '-'
                                        if (group.value.length == 7) group.value += '-'
                                    }
                                }
                            },
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            top = (76).pxToDp()
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        state.isLoading -> CircularProgressIndicator()
                        state.error != null -> {
                            if (state.error == "WRONG_NAME") Text(
                                "Неверное название группы",
                                color = LightWhite,
                                fontSize = 40.sp
                            )
                            else Text(
                                "Ошибка: ${state.error}",
                                color = LightWhite,
                                fontSize = 40.sp
                            )
                        }

                        state.scheduleData != null && state.calendarData == null -> {   // получили ответ от Api по названию группы
                            getCalendarData(state.scheduleData.iCalLink)
                        }

                        state.calendarData != null && state.events == null -> {   // скачали iCal файл с расписанием
                            parseCalendar(state.calendarData)  // Парсим полученные данные о расписании
                        }

                        state.events != null && state.filteredEvents == null -> {
                            File("DateSchedule.txt").printWriter().use { out ->
                                for (event in state.events) {
                                    out.println("${event.summary} ${event.description} ${event.location}")
                                }
                            }

                            // Выбираем события для выбранной даты
                            getEvents(
                                state.events,
                                LocalDate(
                                    currentDate.year,
                                    currentDate.month,
                                    currentDate.dayOfMonth
                                )
                            )
                        }

                        state.filteredEvents != null -> {
                            val scrollState = rememberLazyListState()
                            LazyColumn(
                                modifier = Modifier
                                    .padding(
                                        bottom = (269).pxToDp(),
                                        end = (80).pxToDp()
                                    )
                                .verticalScrollbar(
                                    state = scrollState
                                ),
                                state = scrollState
                            ) {
                                items(state.filteredEvents) { event ->
                                    ScheduleCard(event)
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = (774).pxToDp(),
                                        bottom = (126).pxToDp(),
                                        start = (63).pxToDp()
                                    ),
                                horizontalArrangement = Arrangement.spacedBy((32).pxToDp())
                            ) {
                                val interactionSourceLeft = remember { MutableInteractionSource() }
                                val interactionSourceRight = remember { MutableInteractionSource() }
                                val isLeftHovered by interactionSourceLeft.collectIsHoveredAsState()
                                val isRightHovered by interactionSourceRight.collectIsHoveredAsState()

                                // В этом решении иконка внутри box не центрируется относительно своего "рисунка", а делает это по границам
                                // Пока что решено просто её сдвинуть. Проблема в самой иконке почему-то, либо я чего-то не понимаю.
                                Box(
                                    modifier = Modifier
                                        .myShadow(
                                            color = if (isLeftHovered) LightWhite else Transparent,
                                            borderRadius = 50.dp,
                                            blurRadius = 2.dp,
                                            spread = 1.5f.dp
                                        )
                                        .size((80).pxToDp())
                                        .background(
                                            color = LightWhite,
                                            shape = RoundedCornerShape(50)
                                        )
                                        .clickable(
                                            interactionSource = interactionSourceLeft,
                                            indication = null
                                        ) {
                                            currentDate = currentDate.minusDays(1)

                                            if (state.events != null) {
                                                getEvents(
                                                    state.events,
                                                    LocalDate(
                                                        currentDate.year,
                                                        currentDate.month,
                                                        currentDate.dayOfMonth
                                                    )
                                                )
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                                        contentDescription = "Previous date",
                                        tint = DarkGray,
                                        modifier = Modifier
                                            .offset(x = 3.dp)
                                    )
                                }

                                Card(
                                    modifier = Modifier
                                        .width((400).pxToDp())
                                        .height((105).pxToDp()),
                                    shape = RoundedCornerShape(45.dp),
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                color = LightWhite
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        val date = DateTimeFormatter
                                            .ofPattern("dd.MM.yyyy")
                                            .format(currentDate)
                                        val dayOfWeek = when (currentDate.dayOfWeek) {
                                            DayOfWeek.MONDAY -> "/Пн"
                                            DayOfWeek.TUESDAY -> "/Вт"
                                            DayOfWeek.WEDNESDAY -> "/Ср"
                                            DayOfWeek.THURSDAY -> "/Чт"
                                            DayOfWeek.FRIDAY -> "/Пт"
                                            DayOfWeek.SATURDAY -> "/Сб"
                                            DayOfWeek.SUNDAY -> "/Вс"
                                            else -> ""
                                        }
                                        Text(
                                            text = "$date$dayOfWeek",
                                            color = DarkGray,
                                            fontSize = 40.sp
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .myShadow(
                                            color = if (isRightHovered) LightWhite else Transparent,
                                            borderRadius = 50.dp,
                                            blurRadius = 2.dp,
                                            spread = 1.5f.dp
                                        )
                                        .size((80).pxToDp())
                                        .background(
                                            color = LightWhite,
                                            shape = RoundedCornerShape(50)
                                        )
                                        .clickable(
                                            interactionSource = interactionSourceRight,
                                            indication = null
                                        ) {
                                            currentDate = currentDate.plusDays(1)

                                            if (state.events != null)
                                                getEvents(
                                                    state.events,
                                                    LocalDate(
                                                        currentDate.year,
                                                        currentDate.month,
                                                        currentDate.dayOfMonth
                                                    )
                                                )
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                        contentDescription = "Next date",
                                        tint = DarkGray
                                    )
                                }
                            }
                        }

                        group.value.length == 10 -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = (50).pxToDp(),
                                        start = (30).pxToDp(),
                                        bottom = (22).pxToDp(),
                                        end = (50).pxToDp()
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "События не найдены!",
                                    color = LightWhite,
                                    fontSize = 36.sp
                                )
                            }
                        }

                        else -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = (50).pxToDp(),
                                        start = (30).pxToDp(),
                                        bottom = (22).pxToDp(),
                                        end = (50).pxToDp()
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Введите название группы",
                                    color = LightWhite,
                                    fontSize = 36.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GradientButton(
    symbol: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Button(
        onClick = { onClick(symbol) },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent,
        ),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(25.dp),
        elevation = null,
        border = null,
        modifier = modifier
            .hoverable(interactionSource)
            .myShadow(
                color = if (isHovered) LightWhite else Transparent,
                borderRadius = 25.dp,
                blurRadius = 2.dp,
                spread = 1.5f.dp
            )
            .size((115).pxToDp())
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {}
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = DarkGray,
                    shape = RoundedCornerShape(25.dp)
                )
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            LinearGradientButton1,
                            LinearGradientButton2
                        )
                    ),
                    shape = RoundedCornerShape(25.dp)
                )
                .border(
                    shape = RoundedCornerShape(25.dp),
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.07f)
                )
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = symbol,
                color = LightWhite,
                fontSize = 50.sp,
                modifier = Modifier.wrapContentSize()
            )
        }
    }
}

@Composable
fun ButtonGrid(
    symbols: List<String>,
    onButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy((10).pxToDp()),
        horizontalArrangement = Arrangement.spacedBy((10).pxToDp()),
        contentPadding = PaddingValues(5.dp)
    ) {
        items(symbols) { symbol ->
            GradientButton(
                symbol = symbol,
                onClick = { onButtonClick(symbol) }
            )
        }
    }
}

@Composable
fun GradientBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .drawWithCache {
                val gradient = Brush.radialGradient(
                    colors = listOf(
                        DarkGray,
                        GradientColor1,
                        GradientColor2,
                        GradientColor3,
                        GradientColor4,
                        GradientColor5,
                        GradientColor6,
                        GradientColor7,
                        GradientColor8,
                        GradientColor9,
                        GradientColor10,
                        GradientColor11,
                        GradientColor12,
                        GradientColor13,
                        GradientColor14,
                        GradientColor15
                    ),
                    radius = 1500f,
                    center = Offset(size.width / 2f - 150, size.height / 2f)
                )
                onDrawBehind {
                    rotate(50f, pivot = center) {
                        scale(1f, 2f, pivot = center) {
                            drawRect(
                                brush = gradient,
                                size = Size(size.width * 2, size.height * 2),
                                topLeft = Offset(-size.width / 2, -size.height / 2)
                            )
                        }
                    }
                }
            }
    ) {
        content()
    }
}