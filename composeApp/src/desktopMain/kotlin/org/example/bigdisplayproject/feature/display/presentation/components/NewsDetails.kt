package org.example.bigdisplayproject.feature.display.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import io.ktor.http.headers
import kotlinx.coroutines.launch
import org.example.bigdisplayproject.feature.display.network.dto.Attachment
import org.example.bigdisplayproject.feature.display.network.dto.Link
import org.example.bigdisplayproject.feature.display.network.dto.News
import org.example.bigdisplayproject.feature.display.network.dto.Photo
import org.example.bigdisplayproject.feature.display.network.dto.StaticImageData
import org.example.bigdisplayproject.feature.display.presentation.util.Constants.BACK_BUTTON_HEIGHT
import org.example.bigdisplayproject.feature.display.presentation.util.Constants.BACK_BUTTON_WIDTH
import org.example.bigdisplayproject.feature.display.presentation.util.Constants.CARD_DETAIL_BETWEEN
import org.example.bigdisplayproject.feature.display.presentation.util.Constants.CARD_DETAIL_PADDING
import org.example.bigdisplayproject.feature.display.presentation.util.Constants.CARD_DETAIL_PADDING_TEXT
import org.example.bigdisplayproject.feature.display.presentation.util.Constants.CARD_DETAIL_WIDTH
import org.example.bigdisplayproject.feature.display.presentation.util.Constants.CLOSE_BUTTON_PADDING
import org.example.bigdisplayproject.feature.display.presentation.util.Constants.CLOSE_BUTTON_SIZE
import org.example.bigdisplayproject.feature.display.presentation.util.QrCode
import org.example.bigdisplayproject.feature.display.presentation.util.generateQRCode
import org.example.bigdisplayproject.feature.display.presentation.util.pxToDp
import org.example.bigdisplayproject.ui.theme.DarkGray
import org.example.bigdisplayproject.ui.theme.LightWhite
import java.time.Instant
import java.time.LocalDateTime
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
                .height(700.dp)
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

                    val attachment = checkAttachments(news)
                    if (news.image != null || attachment != null) {
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
                                HorizontalPager(
                                    state = pagerState,
                                    key = { attachments[it] }
                                ) { index ->
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalPlatformContext.current)
                                            .data(
                                                when (val attachment = attachments[index]) {
                                                    is Photo -> attachment.image.src
                                                    is Link -> attachment.image.src
                                                    else -> ""
                                                }
                                            )
                                            .apply {
                                                headers {
                                                    append("User-Agent", "Mozilla/5.0")
                                                    append("Referer", "https://vk.com/")
                                                }
                                            }
                                            .build(),
                                        contentDescription = null,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(16.dp)),
                                        onError = { error ->
                                            println("Ошибка загрузки. Проверьте URL и заголовки.")
                                        }
                                    )
                                }
                            }
                            /*if (attachments.size > 1) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.3f)
                                        .clip(RoundedCornerShape(100))
                                        .background(MaterialTheme.colorScheme.background)
                                        .padding(8.dp)
                                        .align(Alignment.CenterHorizontally)
                                ) {
                                    IconButton(
                                        onClick = {
                                            scope.launch {
                                                pagerState.animateScrollToPage(
                                                    pagerState.currentPage - 1
                                                )
                                            }
                                        },
                                        modifier = Modifier.align(Alignment.CenterStart)
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                            contentDescription = "Go back"
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            scope.launch {
                                                pagerState.animateScrollToPage(
                                                    pagerState.currentPage + 1
                                                )
                                            }
                                        },
                                        modifier = Modifier.align(Alignment.CenterEnd)
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                            contentDescription = "Go forward"
                                        )
                                    }
                                }
                            }*/
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(CARD_DETAIL_PADDING_TEXT.pxToDp())
                            .verticalScroll(rememberScrollState())
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
                }

                val interactionSource = remember { MutableInteractionSource() }
                val isHovered by interactionSource.collectIsHoveredAsState()
                IconButton(
                    onClick = onBackButtonClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(CLOSE_BUTTON_PADDING.pxToDp())
                        .size(CLOSE_BUTTON_SIZE.pxToDp())
                        .border(
                            width = if (isHovered) 2.dp else 0.dp,
                            color = if (isHovered) DarkGray else LightWhite,
                            shape = CircleShape
                        )
                        /*.shadow(
                            elevation = if (isHovered) 8.dp else 0.dp,
                            shape = CircleShape,
                            spotColor = Color.Black
                        )*/
                        .hoverable(interactionSource)
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