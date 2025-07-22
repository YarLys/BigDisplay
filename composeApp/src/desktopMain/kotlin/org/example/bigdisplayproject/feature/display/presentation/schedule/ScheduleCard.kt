package org.example.bigdisplayproject.feature.display.presentation.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.example.bigdisplayproject.feature.display.domain.schedule.CalendarEvent
import org.example.bigdisplayproject.feature.display.presentation.util.pxToDp
import org.example.bigdisplayproject.ui.theme.DarkGray

@Composable
fun ScheduleCard(
    event: CalendarEvent,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = (23).pxToDp(), start = (30).pxToDp(), bottom = (22).pxToDp(), end = (30).pxToDp())
        ) {
            val classNumber = when (event.start.time.toString()) {
                "09:00" -> "1"
                "10:40" -> "2"
                "12:40" -> "3"
                "14:20" -> "4"
                "16:20" -> "5"
                "18:00" -> "6"
                "19:40" -> "7"
                else -> ""
            }
            Text("$classNumber пара", color = DarkGray)
            Spacer(modifier = Modifier.height((10).pxToDp()))
            Text(
                text = event.summary.substring(3),
                fontWeight = FontWeight.SemiBold,
                color = DarkGray
            )
            Spacer(modifier = Modifier.height((10).pxToDp()))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                var classType = ""
                if (event.summary.startsWith("ПР")) classType = "Практика"
                if (event.summary.startsWith("ЛК")) classType = "Лекция"

                Text(classType, color = DarkGray)
                Text("${event.start.time}-${event.end.time}", color = DarkGray)
            }
            Spacer(modifier = Modifier.height((19).pxToDp()))

            Divider(modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height((10).pxToDp()))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "",
                    tint = DarkGray
                )
                Spacer(modifier = Modifier.width((10).pxToDp()))
                Text(event.location, color = DarkGray)
            }
            Spacer(modifier = Modifier.height((10).pxToDp()))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "",
                    tint = DarkGray
                )
                Spacer(modifier = Modifier.width((10).pxToDp()))
                Text(event.description, color = DarkGray)
            }
        }
    }
    Spacer(modifier = Modifier.height((10).pxToDp()))
}