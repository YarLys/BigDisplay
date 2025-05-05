package org.example.bigdisplayproject.feature.display.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun BottomPanel(
    onBackButtonClick: () -> Unit
) {
    val currentTime = remember { mutableStateOf(getCurrentTime()) }
    val currentLabel = remember { mutableStateOf(getLabelText(currentTime.value)) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTime.value = getCurrentTime()
            currentLabel.value = getLabelText(currentTime.value)
        }
    }

    Surface(
        tonalElevation = 16.dp,
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton { onBackButtonClick() }
            OutlinedButton(
                onClick = {  },
                shape = CircleShape,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("RTUITLab", style = MaterialTheme.typography.labelLarge)
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    currentLabel.value,
                    style = MaterialTheme.typography.labelMedium
                )

                Text(
                    text = currentTime.value,
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    "13-я неделя  1 мая 2025 г.",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

private fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("HH:mm:ss")
    return sdf.format(Date())
}

private fun getLabelText(time: String): String {  // мб можно улучшить, пока что пофиг
    val hh = time.substring(0, 2).toInt()
    val mm = time.substring(3, 5).toInt()
    println(hh)
    println(mm)
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
    else return ""
}