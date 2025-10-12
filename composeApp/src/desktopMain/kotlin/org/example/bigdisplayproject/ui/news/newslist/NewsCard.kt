package org.example.bigdisplayproject.ui.news.newslist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import bigdisplayproject.composeapp.generated.resources.Res
import bigdisplayproject.composeapp.generated.resources.zaglushka_svg
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import io.ktor.http.headers
import org.example.bigdisplayproject.data.remote.dto.news.Attachment
import org.example.bigdisplayproject.data.remote.dto.news.Link
import org.example.bigdisplayproject.data.remote.dto.news.News
import org.example.bigdisplayproject.data.remote.dto.news.Photo
import org.example.bigdisplayproject.data.remote.dto.news.Video
import org.example.bigdisplayproject.ui.components.myShadow
import org.example.bigdisplayproject.ui.util.Constants.CARD_WIDTH
import org.example.bigdisplayproject.ui.util.pxToDp
import org.example.bigdisplayproject.ui.theme.LightWhite
import org.jetbrains.compose.resources.painterResource

@Composable
fun NewsCard(news: News, onItemClick: (Long) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .hoverable(interactionSource)
            .myShadow(
                color = if (isHovered) LightWhite else Transparent,
                borderRadius = 15.dp,
                blurRadius = 2.5.dp,
                spread = 2f.dp
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onItemClick(news.id) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightWhite,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top // if (hasImage) Arrangement.Top else Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                val attachment = checkAttachments(news)
                if (news.image != null || (attachment != null && attachment !is Video)) {
                    var src = ""
                    var imageWidth = 0
                    if (attachment != null) {
                        when (attachment) {
                            is Photo -> {
                                src = attachment.image.src
                                imageWidth = attachment.image.width.toInt()
                            }

                            is Link -> {
                                src = attachment.image.src
                                imageWidth = attachment.image.width.toInt()
                            }

                            else -> {
                                src = ""
                            } // because here no other options
                        }
                    } else {
                        src = news.image!!.src
                        imageWidth = news.image.width.toInt()
                    }

                    if (imageWidth.pxToDp() <= (CARD_WIDTH - 30).dp) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalPlatformContext.current)
                                .data(src)
                                .apply {
                                    headers {
                                        append("User-Agent", "Mozilla/5.0")
                                        append("Referer", "https://vk.com/")
                                    }
                                }
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .blur(3.dp)
                                .alpha(0.9f)
                        )
                    }
                    AsyncImage(
                        model = ImageRequest.Builder(LocalPlatformContext.current)
                            .data(src)
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
                            .fillMaxWidth(),
                        onError = { error ->
                            println("Ошибка загрузки. Проверьте URL и заголовки.")
                        }
                    )
                } else {
                    Image(painterResource(Res.drawable.zaglushka_svg), null)
                }
            }
            Spacer(modifier = Modifier.height((20).pxToDp()))
            Text(
                text = news.text,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 4.dp),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

fun checkAttachments(news: News): Attachment? {
    for (attachment in news.attachments) {
        if (attachment.type == "PHOTO" || attachment.type == "LINK" /*|| attachment.type == "VIDEO"*/) {
            return attachment
        }
    }
    return null
}