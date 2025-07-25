package org.example.bigdisplayproject.ui.news.newsdetails

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.example.bigdisplayproject.domain.entities.news.Attachment
import org.example.bigdisplayproject.domain.entities.news.Link
import org.example.bigdisplayproject.domain.entities.news.News
import org.example.bigdisplayproject.domain.entities.news.Photo
import org.example.bigdisplayproject.ui.news.newslist.checkAttachments
import org.example.bigdisplayproject.ui.util.Constants.CARD_DETAIL_BETWEEN
import org.example.bigdisplayproject.ui.util.Constants.CARD_DETAIL_HEIGHT
import org.example.bigdisplayproject.ui.util.Constants.CARD_DETAIL_PADDING
import org.example.bigdisplayproject.ui.util.Constants.CARD_DETAIL_PADDING_TEXT
import org.example.bigdisplayproject.ui.util.Constants.CARD_DETAIL_WIDTH
import org.example.bigdisplayproject.ui.util.Constants.CLOSE_BUTTON_PADDING
import org.example.bigdisplayproject.ui.util.Constants.CLOSE_BUTTON_SIZE
import org.example.bigdisplayproject.ui.util.QrCode
import org.example.bigdisplayproject.ui.util.pxToDp
import org.example.bigdisplayproject.ui.theme.DarkGray
import org.example.bigdisplayproject.ui.theme.LightWhite
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun NewsDetails(
    news: News,
    onBackButtonClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .width(CARD_DETAIL_WIDTH.pxToDp())
                .height(CARD_DETAIL_HEIGHT.dp)
                .align(Alignment.Center),
            colors = CardDefaults.cardColors(
                containerColor = LightWhite,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(CARD_DETAIL_PADDING.pxToDp()),
                    horizontalArrangement = Arrangement.spacedBy(CARD_DETAIL_BETWEEN.pxToDp())
                ) {

                    // маленькое изображение по типу логотипа ИИТ не отображаем
                    val attachment = checkAttachments(news)
                    if ((news.image != null && !(news.image.width == news.image.height && news.image.height < 400))
                        || attachment != null) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            var attachments: MutableList<Attachment> =
                                news.attachments.filter { it.type == "PHOTO" || it.type == "LINK" }
                                    .toMutableList()
                            if (attachments.isEmpty()) {
                                attachments.add(Photo(image = news.image!!))
                            }

                            var pagerState = rememberPagerState(pageCount = { attachments.size })
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp))
                            ) {
                                println()
                                newsDetailsPager(pagerState, attachments)
                            }
                        }
                    }

                    val scrollState = rememberScrollState()
                    val isScrollable = scrollState.maxValue > 0
                    val scrollbarAlpha by animateFloatAsState(
                        targetValue = if (isScrollable) 1f else 0f,
                        animationSpec = tween(durationMillis = 300),
                        label = "scrollbarAlpha"
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(CARD_DETAIL_PADDING_TEXT.pxToDp())
                            .verticalScroll(scrollState)
                    ) {
                        val dateTime = Instant.ofEpochSecond(news.date)
                            .atZone(ZoneId.of("Europe/Moscow"))
                        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                        val date = dateTime.format(formatter)

                        Row {
                            Icon(
                                imageVector = Icons.Outlined.CalendarMonth,
                                contentDescription = null,
                                modifier = Modifier.size(38.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = date,
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(CARD_DETAIL_PADDING.pxToDp()))

                        if (news.name != null) {
                            Text(
                                text = news.name,
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.Black
                            )
                        }

                        Text(
                            text = news.text,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(CARD_DETAIL_PADDING.pxToDp()))

                        var attachment: Link? = null
                        for (att in news.attachments) {
                            if (att.type == "LINK") {
                                attachment = att as Link
                            }
                        }
                        if (attachment != null) {
                            val src = attachment.link
                            QrCode(
                                src,
                                modifier = Modifier.background(LightWhite)
                                    .align(Alignment.CenterHorizontally)
                            )
                        } else {
                            if (news.copyId != null && news.copyOid != null) {
                                val src = "https://vk.com/wall${news.copyOid}_${news.copyId}"
                                QrCode(
                                    src,
                                    modifier = Modifier.background(LightWhite)
                                        .align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }

                    if (isScrollable) {
                        val scope = rememberCoroutineScope()
                        val scrollbarWidth = 8.dp
                        val scrollbarColor = DarkGray.copy(alpha = 0.5f * scrollbarAlpha)
                        val thumbColor = DarkGray.copy(alpha = scrollbarAlpha)

                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(scrollbarWidth + 4.dp)
                                .align(Alignment.CenterVertically)
                                .padding(top = 70.dp)
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
                }

                val interactionSource = remember { MutableInteractionSource() }
                val isHovered by interactionSource.collectIsHoveredAsState()
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(CLOSE_BUTTON_PADDING.pxToDp())
                        .size(CLOSE_BUTTON_SIZE.pxToDp())
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { onBackButtonClick() }
                        .background(
                            color = LightWhite,
                            shape = CircleShape
                        )
                        .border(
                            width = if (isHovered) 2.dp else 0.dp,
                            color = if (isHovered) DarkGray else LightWhite,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Закрыть",
                        tint = DarkGray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}