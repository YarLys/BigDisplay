package org.example.bigdisplayproject.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.example.bigdisplayproject.ui.util.pxToDp
import org.example.bigdisplayproject.ui.util.Constants.BACK_BUTTON_HEIGHT
import org.example.bigdisplayproject.ui.util.Constants.BACK_BUTTON_WIDTH
import org.example.bigdisplayproject.ui.theme.DarkGray
import org.example.bigdisplayproject.ui.theme.LightWhite

@Composable
fun CustomButton(
    onButtonClick: () -> Unit,
    text: String = "Назад",
    icon: ImageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft
) {

    Button(
        onClick = onButtonClick,
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
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = DarkGray
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = DarkGray,
                modifier = Modifier.padding(bottom = 1.7.dp)
            )
        }
    }
}