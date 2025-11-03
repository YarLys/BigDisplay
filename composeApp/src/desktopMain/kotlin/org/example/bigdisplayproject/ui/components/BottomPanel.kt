package org.example.bigdisplayproject.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bigdisplayproject.composeapp.generated.resources.Res
import kotlinx.coroutines.delay
import org.example.bigdisplayproject.ui.util.Constants.BOTTOM_PANEL_HEIGHT
import org.example.bigdisplayproject.ui.util.Constants.BOTTOM_PANEL_ICONS_SPACE
import org.example.bigdisplayproject.ui.util.pxToDp
import org.example.bigdisplayproject.ui.theme.DarkGray
import org.jetbrains.compose.resources.painterResource
import bigdisplayproject.composeapp.generated.resources.iit_logo_svg
import bigdisplayproject.composeapp.generated.resources.rtuitlab_logo_svg
import bigdisplayproject.composeapp.generated.resources.separator
import kotlinx.datetime.LocalDate
import org.example.bigdisplayproject.domain.usecases.schedule.getWeekNumber
import org.example.bigdisplayproject.ui.theme.LightWhite
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date

@Composable
fun BottomPanel(
    onButtonClick: () -> Unit,
    text: String = "Назад",
    icon: ImageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft
) {
    val currentTime by produceState(initialValue = getCurrentTime()) {
        while (true) {
            delay(1000)
            value = getCurrentTime()
        }
    }
    val currentLabel by remember { derivedStateOf { getLabelText() } }
    val currentWeek by remember { derivedStateOf { getCurrentWeek(currentTime) } }

    Surface(
        color = DarkGray,
        modifier = Modifier
            .height(BOTTOM_PANEL_HEIGHT.pxToDp())
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomButton(onButtonClick = { onButtonClick() }, text = text, icon = icon)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(BOTTOM_PANEL_ICONS_SPACE.pxToDp())
            ) {
                Image(painterResource(Res.drawable.iit_logo_svg), null)
                Text(
                    text = "Институт Информационных\nТехнологий",
                    style = MaterialTheme.typography.bodyMedium,
                    color = LightWhite,
                    textAlign = TextAlign.Center
                )
                Image(painterResource(Res.drawable.separator), null)
                Image(painterResource(Res.drawable.rtuitlab_logo_svg), null)
                Text(
                    text = "RTUITLab",
                    style = MaterialTheme.typography.bodyMedium,
                    color = LightWhite
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(BOTTOM_PANEL_ICONS_SPACE.pxToDp()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        currentLabel,
                        style = MaterialTheme.typography.bodyMedium,
                        color = LightWhite
                    )
                    Text(
                        text = currentWeek,
                        style = MaterialTheme.typography.bodyMedium,
                        color = LightWhite
                    )
                }

                Text(
                    text = currentTime.substring(0, 9),
                    style = MaterialTheme.typography.headlineMedium,
                    color = LightWhite
                )
            }
        }
    }
}

private fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("HH:mm:ss dd.MM.yyyy")
    return sdf.format(Date())
}

private fun getLabelText(): String {
    val currentDate = ZonedDateTime.now(ZoneId.of("Europe/Moscow"))
    val localDate = LocalDate(
        currentDate.year,
        currentDate.month,
        currentDate.dayOfMonth
    )
    val weekNum = getWeekNumber(localDate)

    if (weekNum < 18) {
        val hh = currentDate.hour
        val mm = currentDate.minute
        if (hh == 9 || (hh == 10 && mm <= 29)) return "Идёт 1-ая пара"
        else if (hh == 10 && mm in 30..39) return "Перерыв перед 2-ой парой"
        else if (hh == 11 || (hh == 10 && mm in 40..60) || (hh == 12 && mm <= 9)) return "Идёт 2-ая пара"
        else if (hh == 12 && mm in 10..39) return "Большой перерыв перед 3-ей парой"
        else if (hh == 13 || (hh == 12 && mm in 40..60) || (hh == 14 && mm <= 9)) return "Идёт 3-ья пара"
        else if (hh == 14 && mm in 10..19) return "Перерыв перед 4-ой парой"
        else if ((hh == 14 && mm in 20..60) || (hh == 15 && mm <= 49)) return "Идёт 4-ая пара"
        else if ((hh == 15 && mm in 50..60) || (hh == 16 && mm <= 19)) return "Большой перерыв перед 5-ой парой"
        else if ((hh == 16 && mm in 20..60) || (hh == 17 && mm <= 49)) return "Идёт 5-ая пара"
        else if (hh == 17 && mm in 50..59) return "Перерыв перед 6-ой парой"
        else if (hh == 18 || (hh == 19 && mm <= 29)) return "Идёт 6-ая пара"
        else if (hh == 19 && mm in 30..39) return "Перерыв перед 7-ой парой"
        else if ((hh == 19 && mm in 40..60) || hh == 20 || (hh == 21 && mm <= 9)) return "Идёт 7-ая пара"
    }
    return ""
}

private fun getCurrentWeek(time: String): String {
    val currentDate = ZonedDateTime.now(ZoneId.of("Europe/Moscow"))
    val localDate = LocalDate(
        currentDate.year,
        currentDate.month,
        currentDate.dayOfMonth
    )
    val weekNum = getWeekNumber(localDate)

    return if (weekNum < 18) "$weekNum-я неделя ${time.substring(9)}"
    else time.substring(9)
}