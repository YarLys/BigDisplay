package org.example.bigdisplayproject.feature.display.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import io.ktor.http.headers
import kotlinx.coroutines.launch
import org.example.bigdisplayproject.feature.display.network.dto.Link
import org.example.bigdisplayproject.feature.display.network.dto.News
import org.example.bigdisplayproject.feature.display.network.dto.Photo

@Composable
fun NewsDetails(news: News, onBackButtonClick: () -> Unit) {

    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = { BottomPanel { onBackButtonClick() } }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(24.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    val attachment = checkAttachments(news)
                    if (news.image != null || attachment != null) {
                        /*var src = ""
                        if (attachment != null) {
                            src = when (attachment) {
                                is Photo -> attachment.image.src
                                is Link -> attachment.image.src
                                else -> { "" } // because here no other options
                            }
                        }
                        else src = news.image!!.src*/
                        val attachments = news.attachments.filter { it.type == "PHOTO" || it.type == "LINK" }
                        for (att in news.attachments) {
                            println(att.type)
                        }
                        var pagerState = rememberPagerState(pageCount = { attachments.size })
                        Box(
                            modifier = Modifier
                                .height(450.dp)
                                .fillMaxWidth()
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
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(8.dp)),
                                    onError = { error ->
                                        println("Ошибка загрузки. Проверьте URL и заголовки.")
                                    }
                                )
                            }
                        }
                        if (attachments.size > 1) {
                            Box(
                                modifier = Modifier
                                    //.offset(y = -(16).dp)
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
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    }


                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (news.name != null) {
                            Text(
                                text = news.name,
                                style = MaterialTheme.typography.displaySmall,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Text(
                            text = news.text,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.fillMaxWidth()
                        )

                        /*Text(
                            text = news.date.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )*/
                    }
                }
            }
        }
    }
}