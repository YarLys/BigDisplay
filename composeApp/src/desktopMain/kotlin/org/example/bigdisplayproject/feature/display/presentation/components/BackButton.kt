package org.example.bigdisplayproject.feature.display.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.bigdisplayproject.feature.display.presentation.util.pxToDp
import org.example.bigdisplayproject.feature.display.presentation.util.Constants.BACK_BUTTON_HEIGHT
import org.example.bigdisplayproject.feature.display.presentation.util.Constants.BACK_BUTTON_WIDTH
import org.example.bigdisplayproject.ui.theme.DarkGray
import org.example.bigdisplayproject.ui.theme.LightWhite

@Composable
fun BackButton(
    onBackButtonClick: () -> Unit
) {

    Button(
        onClick = onBackButtonClick,
        modifier = Modifier
            .height(BACK_BUTTON_HEIGHT.pxToDp())
            .width(BACK_BUTTON_WIDTH.pxToDp())
            .clip(RoundedCornerShape(10.dp)),
        shape = ButtonDefaults.outlinedShape,
        contentPadding = PaddingValues(
            start = 8.dp,
            top = 4.dp,
            end = 12.dp,
            bottom = 4.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = LightWhite
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                tint = DarkGray
            )
            Text(
                text = "Назад",
                style = MaterialTheme.typography.bodyMedium,
                color = DarkGray
            )
        }
    }
}