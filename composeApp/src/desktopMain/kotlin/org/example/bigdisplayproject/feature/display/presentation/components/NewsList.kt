package org.example.bigdisplayproject.feature.display.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import io.ktor.http.headers
import org.example.bigdisplayproject.feature.display.network.dto.Attachment
import org.example.bigdisplayproject.feature.display.network.dto.Link
import org.example.bigdisplayproject.feature.display.network.dto.News
import org.example.bigdisplayproject.feature.display.network.dto.Photo
import org.example.bigdisplayproject.feature.display.network.dto.Video
import java.net.URI
import javax.swing.JEditorPane

@Composable
fun NewsList(
    newsList: List<News>,
    onItemClick: (Long) -> Unit,
    scrollPosition: Int,
    onScrollPositionChanged: (Int) -> Unit
) {
    val listState = rememberLazyGridState(
        initialFirstVisibleItemIndex = scrollPosition
    )
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { onScrollPositionChanged(it) }
    }
    Scaffold(
        bottomBar = { BottomPanel({ /* todo */  }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyVerticalGrid(
                state = listState,
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(newsList) { news ->
                    NewsCard(news = news, onItemClick)
                }
            }
        }
    }
}

@Composable
fun NewsCard(news: News, onItemClick: (Long) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(300.dp)
            .clickable { onItemClick(news.id) },
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                val attachment = checkAttachments(news)
                /*if (news.id == (1710).toLong() && attachment is Video) {
                VideoPlayer1("https://vk.com/video${attachment.ownedId}_${attachment.objectId}")
            }*/
                if (news.image != null || attachment != null) {
                    var src = ""
                    if (attachment != null) {
                        src = when (attachment) {
                            is Photo -> attachment.image.src
                            is Link -> attachment.image.src
                            //is Video -> "https://vk.com/video${attachment.ownedId}_${attachment.objectId}"
                            else -> {
                                ""
                            } // because here no other options
                        }
                    } else src = news.image!!.src
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
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp)),
                        onError = { error ->
                            println("Ошибка загрузки. Проверьте URL и заголовки.")
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            Text(
                text = news.text,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(0.4f)
            )
            //Text(text = news.text.split(".?!\n").firstOrNull() ?: news.text, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
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