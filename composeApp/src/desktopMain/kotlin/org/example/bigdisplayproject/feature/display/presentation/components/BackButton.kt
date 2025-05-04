package org.example.bigdisplayproject.feature.display.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun BackButton(
    onBackButtonClick: () -> Unit
) {
    OutlinedButton(
        onClick = onBackButtonClick,
        modifier = Modifier
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp)),
        shape = ButtonDefaults.outlinedShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 8.dp,
            end = 24.dp,
            bottom = 8.dp
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
            )
            Text(
                text = "Назад",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }


    /*var isPressed by remember { mutableStateOf(false) }
    val elevation by animateFloatAsState(
        targetValue = if (isPressed) 2.dp.value else 6.dp.value,
        label = "buttonElevation"
    )

    OutlinedButton(
        onClick = onBackButtonClick,
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation.dp,
            pressedElevation = 2.dp
        ),
        interactionSource = remember { MutableInteractionSource() }.also { source ->
            LaunchedEffect(source) {
                source.interactions.collect {
                    when (it) {
                        is PressInteraction.Press -> isPressed = true
                        is PressInteraction.Release -> isPressed = false
                        is PressInteraction.Cancel -> isPressed = false
                    }
                }
            }
        },
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 8.dp,
            end = 24.dp,
            bottom = 8.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Ваша иконка
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Назад",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }*/
}