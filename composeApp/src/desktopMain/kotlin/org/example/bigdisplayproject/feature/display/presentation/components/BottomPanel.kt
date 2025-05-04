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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomPanel(
    onBackButtonClick: () -> Unit
) {
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
                    "Большой перерыв перед 3-ей парой",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    "12:15:03",
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