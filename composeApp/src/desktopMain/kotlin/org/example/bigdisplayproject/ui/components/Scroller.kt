package org.example.bigdisplayproject.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
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

@Composable
fun Modifier.verticalScrollbar(
    state: LazyListState,
    apply: Boolean = false,
    alpha: Float = 1f,
    width: Dp = 8.dp
): Modifier {
    if (!apply) return this

    val scope = rememberCoroutineScope()
    val scrollbarColor = DarkGray.copy(alpha = 0.5f * alpha)
    val thumbColor = DarkGray.copy(alpha = alpha)

    var isDragging by remember { mutableStateOf(false) }

    return this.then(
        Modifier
            .drawWithContent {
                drawContent()

                val layoutInfo = state.layoutInfo
                val viewportSize = layoutInfo.viewportSize.height

                val totalItemsHeight = layoutInfo.visibleItemsInfo.sumOf { it.size }
                val totalItemsCount = layoutInfo.totalItemsCount
                val visibleItemsCount = layoutInfo.visibleItemsInfo.size

                val avgItemHeight = if (visibleItemsCount > 0) {
                    totalItemsHeight.toFloat() / visibleItemsCount
                } else 0f
                val totalContentHeight = avgItemHeight * totalItemsCount

                val scrollbarHeight = size.height
                // Рассчитываем характеристики для бегунка
                val thumbHeight = if (totalContentHeight > 0) {
                    ((viewportSize / totalContentHeight) * scrollbarHeight)
                        .coerceAtLeast(32.dp.toPx())
                        .coerceAtMost(scrollbarHeight)
                } else 0f

                val scrollOffset =
                    state.firstVisibleItemIndex * avgItemHeight + state.firstVisibleItemScrollOffset
                val thumbOffset = if (totalContentHeight > viewportSize) {
                    (scrollOffset / (totalContentHeight - viewportSize) * (scrollbarHeight - thumbHeight))
                        .coerceAtLeast(0f)
                } else 0f


                // Рисуем фон скроллбара
                drawRoundRect(
                    color = scrollbarColor,
                    topLeft = Offset(size.width - width.toPx(), 0f),
                    size = Size(width.toPx(), scrollbarHeight),
                    cornerRadius = CornerRadius(width.toPx() / 2)
                )

                // Рисуем бегунок
                drawRoundRect(
                    color = thumbColor,
                    topLeft = Offset(size.width - width.toPx(), thumbOffset),
                    size = Size(width.toPx(), thumbHeight),
                    cornerRadius = CornerRadius(width.toPx() / 2)
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { isDragging = true },
                    onDragEnd = { isDragging = false },
                    onDrag = { change, dragAmount ->
                        val layoutInfo = state.layoutInfo
                        val viewportSize = layoutInfo.viewportSize.height

                        // Рассчитываем общую высоту контента (как в drawWithContent)
                        val totalItemsHeight = layoutInfo.visibleItemsInfo.sumOf { it.size }
                        val totalItemsCount = layoutInfo.totalItemsCount
                        val visibleItemsCount = layoutInfo.visibleItemsInfo.size
                        val avgItemHeight = if (visibleItemsCount > 0) {
                            totalItemsHeight.toFloat() / visibleItemsCount
                        } else 0f
                        val totalContentHeight = avgItemHeight * totalItemsCount

                        // Максимально возможное смещение прокрутки
                        val maxScrollOffset = (totalContentHeight - viewportSize).coerceAtLeast(0f)
                        // Текущая позиция прокрутки
                        val currentScrollOffset = state.firstVisibleItemIndex * avgItemHeight + state.firstVisibleItemScrollOffset
                        // Высота области скроллбара для перемещения бегунка
                        val scrollbarTrackHeight = size.height -
                                ((viewportSize / totalContentHeight) * size.height).coerceAtLeast(32.dp.toPx())

                        if (scrollbarTrackHeight > 0) {
                            // Преобразуем перемещение мыши в смещение прокрутки
                            val scrollDelta = (dragAmount.y / scrollbarTrackHeight) * maxScrollOffset
                            val newScrollOffset = (currentScrollOffset + scrollDelta).coerceIn(0f, maxScrollOffset)

                            // Вычисляем индекс элемента и смещение для scrollToItem
                            val newIndex = (newScrollOffset / avgItemHeight).toInt().coerceAtMost(totalItemsCount - 1)
                            val newItemOffset = (newScrollOffset % avgItemHeight).toInt()

                            scope.launch {
                                state.scrollToItem(
                                    newIndex,
                                    scrollOffset = newItemOffset
                                )
                            }
                        }
                    }
                )
            }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val maxHeight = size.height
                    val newValue = (offset.y / maxHeight) * (state.layoutInfo.visibleItemsInfo.sumOf { it.size })
                    scope.launch {
                        state.scrollToItem(0, scrollOffset = newValue.toInt())
                    }
                }
            }
    )
}