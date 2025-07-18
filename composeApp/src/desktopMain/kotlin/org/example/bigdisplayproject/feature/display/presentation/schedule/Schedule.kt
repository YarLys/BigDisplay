package org.example.bigdisplayproject.feature.display.presentation.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate
import org.example.bigdisplayproject.feature.display.ScheduleStore
import org.example.bigdisplayproject.feature.display.network.dto.schedule.ScheduleData
import org.example.bigdisplayproject.feature.display.presentation.components.BottomPanel
import org.example.bigdisplayproject.feature.display.domain.schedule.CalendarParser
import org.example.bigdisplayproject.feature.display.domain.schedule.getEvents
import org.example.bigdisplayproject.feature.display.presentation.util.pxToDp
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

@Composable
fun Schedule(
    onBackButtonClick: () -> Unit,
    state: ScheduleStore.State,
    getSchedule: (String) -> Unit,
    getCalendarData: (String) -> Unit
) {
    val scheduleData = remember { mutableStateOf(ScheduleData(-1, "", "", 0, "", "", "", "")) }
    val group = remember { mutableStateOf("") }

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
                            bottom = (236).pxToDp()
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
                                .padding(8.dp)
                                .fillMaxWidth()
                                .height((150).pxToDp())
                                .shadow(
                                    elevation = 24.dp,
                                    shape = RoundedCornerShape(24.dp),
                                    spotColor = Color.White,
                                    ambientColor = Color.White.copy(alpha = 0.7f)
                                )
                                .shadow(
                                    elevation = 24.dp,
                                    shape = RoundedCornerShape(24.dp),
                                    spotColor = Color.White.copy(0.9f),
                                    ambientColor = Color.White.copy(alpha = 0.4f)
                                )
                                .shadow(
                                    elevation = 24.dp,
                                    shape = RoundedCornerShape(24.dp),
                                    spotColor = Color.Cyan.copy(alpha = 0.9f),
                                    ambientColor = Color.Cyan.copy(alpha = 0.7f)
                                ),
                            shape = RoundedCornerShape(16.dp),
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
                                            if (group.value.last() == '-') group.value = group.value.substring(0, group.value.length-2)
                                            else group.value = group.value.substring(0, group.value.length-1)
                                        }
                                    }
                                    else -> {
                                        if (group.value.length < 10) group.value += symbol
                                        if (group.value.length == 4) group.value += '-'
                                        if (group.value.length == 7) group.value += '-'
                                    }
                                }
                            },
                            modifier = Modifier.padding((10).pxToDp())
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            top = (84).pxToDp(),
                            bottom = (277).pxToDp(),
                            end = (88).pxToDp()
                        )
                ) {
                    when {
                        state.isLoading -> CircularProgressIndicator()
                        state.error != null -> Text("Ошибка: ${state.error}", color = LightWhite, fontSize = 40.sp)
                        state.scheduleData != null && state.calendarData == null -> {   // получили ответ от Api по названию группы
                            getCalendarData(state.scheduleData.iCalLink)
                        }
                        state.calendarData != null -> {   // скачали iCal файл с расписанием
                            val parser = CalendarParser()
                            parser.parseCalendar(state.calendarData)

                            val events = getEvents(parser.events, LocalDate(2025, 4, 7))
                            File("DateSchedule.txt").printWriter().use { out ->
                                for (event in events) {
                                    out.println("${event.summary} ${event.description} ${event.location}")
                                }
                            }
                        }
                        else -> {
                            Text(
                                text = "Введите название группы",
                                color = LightWhite,
                                fontSize = 40.sp
                            )
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

    Button(
        onClick = { onClick(symbol) },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent,
        ),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = null,
        border = null,
        modifier = modifier
            .size((110).pxToDp())
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {}
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            LinearGradientButton1,
                            LinearGradientButton2
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    shape = RoundedCornerShape(20.dp),
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.05f)
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
        horizontalArrangement = Arrangement.spacedBy((10).pxToDp())
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
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
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