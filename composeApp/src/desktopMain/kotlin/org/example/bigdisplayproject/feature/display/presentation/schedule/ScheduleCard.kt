package org.example.bigdisplayproject.feature.display.presentation.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.example.bigdisplayproject.feature.display.network.dto.schedule.Classroom
import org.example.bigdisplayproject.feature.display.presentation.util.pxToDp
import org.example.bigdisplayproject.ui.theme.DarkGray

@Composable
fun ScheduleCard(
    classroom: Classroom,
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
            Text("1 пара", color = DarkGray)
            Spacer(modifier = Modifier.height((10).pxToDp()))
            Text(
                text = classroom.className,
                fontWeight = FontWeight.SemiBold,
                color = DarkGray
            )
            Spacer(modifier = Modifier.height((10).pxToDp()))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Лекция", color = DarkGray)
                Text(classroom.classTime, color = DarkGray)
            }
            Spacer(modifier = Modifier.height((19).pxToDp()))

            Divider(modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height((10).pxToDp()))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Icon
                Text(classroom.classCabinet, color = DarkGray)
            }
            Spacer(modifier = Modifier.height((10).pxToDp()))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Icon
                Text("Нежданов Иван Владимирович", color = DarkGray)
            }
        }
    }
}