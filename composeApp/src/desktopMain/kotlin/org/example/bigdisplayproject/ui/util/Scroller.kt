package org.example.bigdisplayproject.ui.util

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.example.bigdisplayproject.ui.theme.DarkGray

@Composable
fun Scroller(
    scrollState: ScrollState,
    scrollbarAlpha: Float,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val scrollbarWidth = 8.dp
    val scrollbarColor = DarkGray.copy(alpha = 0.5f * scrollbarAlpha)
    val thumbColor = DarkGray.copy(alpha = scrollbarAlpha)

    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(scrollbarWidth + 4.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        val maxHeight = size.height
                        val sensitivity = 2.5f
                        val delta = (dragAmount.y * sensitivity * scrollState.maxValue / maxHeight)
                        val newOffset = (scrollState.value + delta)
                            .coerceIn(0f, scrollState.maxValue.toFloat())
                        scope.launch {
                            scrollState.scrollTo(newOffset.toInt())
                        }
                    }
                )
            }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val maxHeight = size.height
                    val newValue = (offset.y / maxHeight) * scrollState.maxValue
                    scope.launch {
                        scrollState.scrollTo(newValue.toInt())
                    }
                }
            }
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val scrollbarHeight = size.height
            val scrollbarTop = 0f

            drawRoundRect(
                color = scrollbarColor,
                topLeft = Offset(size.width - scrollbarWidth.toPx(), scrollbarTop),
                size = Size(scrollbarWidth.toPx(), scrollbarHeight),
                cornerRadius = CornerRadius(scrollbarWidth.toPx() / 2)
            )

            val visibleHeight = scrollbarHeight
            val contentHeight = scrollState.maxValue + visibleHeight
            val thumbHeight = (visibleHeight * (visibleHeight / contentHeight))
                .coerceAtLeast(32.dp.toPx())
                .coerceAtMost(visibleHeight)

            val thumbOffset = (scrollState.value * (visibleHeight / contentHeight))
                .coerceAtMost(visibleHeight - thumbHeight)

            drawRoundRect(
                color = thumbColor,
                topLeft = Offset(size.width - scrollbarWidth.toPx(), thumbOffset),
                size = Size(scrollbarWidth.toPx(), thumbHeight),
                cornerRadius = CornerRadius(scrollbarWidth.toPx() / 2)
            )
        }
    }
}