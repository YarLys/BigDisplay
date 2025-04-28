package org.example.bigdisplayproject.feature.display.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import io.ktor.http.headers
import org.example.bigdisplayproject.feature.display.network.dto.News

@Composable
fun NewsDetails(news: News, onBackButtonClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)  // change to material3
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = news.name ?: "Новость"
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = news.text // мб добавить скролл и сделать ограниченное кол-во текста?
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = news.date.toString()
                )
                if (news.image != null) {  // мб нужно добавить, чтобы, пока картинка грузится, был индикатор. Также обработку ошибки тоже можно добавить
                    AsyncImage(
                        model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(news.image.src)
                        .apply {
                            headers {
                                append("User-Agent", "Mozilla/5.0")
                            }
                            headers {
                                append("Referer", "https://vk.com/")
                            }
                        }
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        onError = { error ->
                            println("Ошибка загрузки. Проверьте URL и заголовки.")
                        }
                    )

                    /*AsyncImage(
                        model = ImageRequest.Builder(LocalPlatformContext.current)
                            .data(news.image.src)
                            .apply {
                                headers {
                                    append("User-Agent", "Mozilla/5.0")
                                }
                                headers {
                                    append("Referer", "https://vk.com/")
                                }
                            }
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        onState = { state ->
                            when (state) {
                                is AsyncImagePainter.State.Loading {
                                    CircularProgressIndicator()
                                }
                                is AsyncImagePainter.State.Error {
                                    println("Ошибка загрузки. Проверьте URL и заголовки.")
                                }
                                else -> {}
                            }
                        }
                    )*/
                }

                BackButton(
                    onBackButtonClick = onBackButtonClick
                )
            }
        }
    }
}