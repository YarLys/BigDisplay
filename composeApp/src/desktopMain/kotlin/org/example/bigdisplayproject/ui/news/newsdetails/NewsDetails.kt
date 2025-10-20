package org.example.bigdisplayproject.ui.news.newsdetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.bigdisplayproject.data.remote.dto.news.Attachment
import org.example.bigdisplayproject.data.remote.dto.news.Link
import org.example.bigdisplayproject.data.remote.dto.news.Photo
import org.example.bigdisplayproject.ui.news.store.NewsStore
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
import org.example.bigdisplayproject.ui.components.Scroller
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun NewsDetails(
    state: NewsStore.State,
    onBackButtonClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        var isDialogVisible by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            isDialogVisible = true
        }
        DisposableEffect(Unit) {
            onDispose { isDialogVisible = false }
        }

        AnimatedVisibility(
            visible = isDialogVisible,
            enter = fadeIn(animationSpec = tween(700)) +
                    scaleIn(initialScale = 0.9f, animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(700)) +
                    scaleOut(targetScale = 0.9f, animationSpec = tween(500)),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
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
                    when {
                        state.error != null -> {
                            Text("Ошибка: ${state.error}")
                        }

                        state.isLoading -> {
                            CircularProgressIndicator()
                        }

                        state.selectedNews != null -> {

                            val news = state.selectedNews

                            Box(modifier = Modifier.fillMaxSize()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(CARD_DETAIL_PADDING.pxToDp()),
                                    horizontalArrangement = Arrangement.spacedBy(CARD_DETAIL_BETWEEN.pxToDp())
                                ) {
                                    // Добавим в Pager news.image. Маленькое изображение по типу логотипа ИИТ не отображаем
                                    val attachments = mutableListOf<Attachment>()
                                    if (news.image != null && !(news.image.width == news.image.height && news.image.height < 400)) {
                                        attachments.add(Photo(image = news.image))
                                    }
                                    // Добавим все подходящие элементы из news.attachments
                                    attachments.addAll(news.attachments.filter { it.type == "PHOTO" || it.type == "LINK" || it.type == "VIDEO"})

                                    if (attachments.isNotEmpty()) {
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                        ) {
                                            var pagerState =
                                                rememberPagerState(pageCount = { attachments.size })
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(RoundedCornerShape(16.dp))
                                            ) {
                                                newsDetailsPager(pagerState, attachments, news.id)
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
                                        val formatter =
                                            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
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
                                                val src =
                                                    "https://vk.com/wall${news.copyOid}_${news.copyId}"
                                                QrCode(
                                                    src,
                                                    modifier = Modifier.background(LightWhite)
                                                        .align(Alignment.CenterHorizontally)
                                                )
                                            } else {
                                                if (news.text.contains("Ссылка:")) {
                                                    val pattern = "Ссылка: (.+)"
                                                    val match = Regex(pattern).find(news.text)
                                                    if (match != null) {
                                                        val src = match.groupValues[1]
                                                        QrCode(
                                                            src,
                                                            modifier = Modifier.background(
                                                                LightWhite
                                                            )
                                                                .align(Alignment.CenterHorizontally)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (isScrollable) {
                                        Scroller(
                                            scrollState = scrollState,
                                            scrollbarAlpha = scrollbarAlpha,
                                            modifier = Modifier
                                                .align(Alignment.CenterVertically)
                                                .padding(top = 70.dp)
                                        )
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

                        else -> Text("Новость не найдена")
                    }
                }
            }
        }
    }
}