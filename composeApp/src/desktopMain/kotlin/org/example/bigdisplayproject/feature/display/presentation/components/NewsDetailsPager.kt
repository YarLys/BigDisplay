package org.example.bigdisplayproject.feature.display.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import io.ktor.http.headers
import kotlinx.coroutines.launch
import org.example.bigdisplayproject.feature.display.network.dto.Attachment
import org.example.bigdisplayproject.feature.display.network.dto.Link
import org.example.bigdisplayproject.feature.display.network.dto.Photo
import org.example.bigdisplayproject.feature.display.presentation.util.Constants.CARD_DETAIL_HEIGHT
import org.example.bigdisplayproject.feature.display.presentation.util.Constants.CARD_DETAIL_WIDTH
import org.example.bigdisplayproject.feature.display.presentation.util.Constants.CARD_WIDTH
import org.example.bigdisplayproject.feature.display.presentation.util.dpToPx
import org.example.bigdisplayproject.feature.display.presentation.util.pxToDp
import kotlin.math.abs

@Composable
fun newsDetailsPager(
    pagerState: PagerState,
    attachments: List<Attachment>
) {
    HorizontalPager(
        state = pagerState,
        key = { attachments[it] }
    ) { index ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp)
                .drawWithContent {
                    drawContent()
                }
        ) {
            var src = ""
            var imageHeight = 0
            var imageWidth = 0
            when (val attachment = attachments[index]) {
                is Photo -> {
                    src = attachment.image.src
                    imageHeight = attachment.image.height.toInt()
                    imageWidth = attachment.image.width.toInt()
                }
                is Link -> {
                    src = attachment.image.src
                    imageHeight = attachment.image.height.toInt()
                    imageWidth = attachment.image.width.toInt()
                }
                else -> {}
            }
            //if (imageHeight.pxToDp() <= CARD_DETAIL_HEIGHT.dp - 70.dp) {
            if (shouldApplyBlur(IntSize(width = imageWidth, height = imageHeight),
                    IntSize(width = CARD_DETAIL_WIDTH / 2, height = CARD_DETAIL_HEIGHT.dp.dpToPx().toInt()))) { // небольшой нюанс
                //println(imageHeight)
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
                        .blur(3.5.dp)
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
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(16.dp)),
                onError = { error ->
                    println("Ошибка загрузки. Проверьте URL и заголовки.")
                }
            )

            if (attachments.size > 1) {

                val scope = rememberCoroutineScope()

                if (index > 0) {
                    val leftInteractionSource = remember { MutableInteractionSource() }
                    val isLeftHovered by leftInteractionSource.collectIsHoveredAsState()

                    IconButton(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index - 1)
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(4.dp)
                            .size(64.dp)
                            .hoverable(leftInteractionSource)
                        /*.shadow(
                            elevation = if (isLeftHovered) 8.dp else 0.dp,
                            shape = CircleShape,
                            spotColor = Color.White
                        )*/,
                        interactionSource = leftInteractionSource
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Previous",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                if (index < attachments.size - 1) {
                    val rightInteractionSource = remember { MutableInteractionSource() }
                    val isRightHovered by rightInteractionSource.collectIsHoveredAsState()

                    IconButton(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index + 1)
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(4.dp)
                            .size(64.dp)
                            .hoverable(rightInteractionSource)
                        /*.shadow(
                            elevation = if (isRightHovered) 8.dp else 0.dp,
                            shape = CircleShape,
                            spotColor = Color.White
                        )*/,
                        interactionSource = rightInteractionSource
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Next",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                        .height(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    repeat(attachments.size) { i ->
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = if (i == index) Color.White else Color.White.copy(alpha = 0.5f),
                                    shape = CircleShape
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (i == index) Color.Cyan else Color.Transparent,
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
        }
    }
}

fun shouldApplyBlur(
    imageSize: IntSize,
    containerSize: IntSize,
    threshold: Float = 0.3f // % разница
): Boolean {
    val imageAspect = imageSize.width.toFloat() / imageSize.height
    val containerAspect = containerSize.width.toFloat() / containerSize.height

    val aspectDiff = abs(imageAspect - containerAspect)

    val heightDiff = 1f - (imageSize.height.toFloat() / containerSize.height)

    return aspectDiff > 0.3f || heightDiff > threshold
}