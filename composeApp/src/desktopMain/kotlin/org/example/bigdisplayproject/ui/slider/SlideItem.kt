package org.example.bigdisplayproject.ui.slider

import org.example.bigdisplayproject.ui.components.videoplayer.VideoPlayer
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import io.ktor.http.headers
import org.example.bigdisplayproject.data.remote.dto.slider.SlideData
import org.example.bigdisplayproject.data.remote.dto.slider.SlideImage
import org.example.bigdisplayproject.data.remote.dto.slider.SlideVideo
import org.example.bigdisplayproject.ui.theme.LightWhite
import org.example.bigdisplayproject.ui.util.pxToDp
import org.example.bigdisplayproject.ui.components.videoplayer.rememberVideoPlayerState
import java.util.Locale

@Composable
fun SlideItem(
    slideData: SlideData,
    onLeftButtonClick: () -> Unit,
    onRightButtonClick: () -> Unit,
    onNewsLinkClick: (Long) -> Unit,
    onScheduleLinkClick: () -> Unit
) {
    val leftInteractionSource = remember { MutableInteractionSource() }
    val rightInteractionSource = remember { MutableInteractionSource() }
    val isLeftHovered by leftInteractionSource.collectIsHoveredAsState()
    val isRightHovered by rightInteractionSource.collectIsHoveredAsState()
    val leftScale by animateFloatAsState(
        targetValue = if (isLeftHovered) 1.2f else 1f,
        animationSpec = tween(300),
        label = "scaleAnimation"
    )
    val rightScale by animateFloatAsState(
        targetValue = if (isRightHovered) 1.2f else 1f,
        animationSpec = tween(300),
        label = "scaleAnimation"
    )
    var spacerFlag by mutableStateOf(true)
    var src = ""
    if (slideData.mediaContent is SlideImage) {
        src = slideData.mediaContent.image.src
    }

    val verticalArrangement = when (slideData.sides.sideY) {
        "top" -> Arrangement.Top
        "bottom" -> Arrangement.Bottom
        else -> Arrangement.Center
    }
    val horizontalAlignment = when (slideData.sides.sideX) {
        "left" -> Alignment.Start
        "right" -> Alignment.End
        else -> Alignment.CenterHorizontally
    }
    val headingAlignment = when (slideData.sides.sideHeading) {
        "left" -> Alignment.Start
        "right" -> Alignment.End
        else -> Alignment.CenterHorizontally
    }
    val textAlignment = when (slideData.sides.sideText) {
        "left" -> Alignment.Start
        "right" -> Alignment.End
        else -> Alignment.CenterHorizontally
    }
    val attachmentsArrangement = when (slideData.sides.sideAttachments) {
        "left" -> Arrangement.Start
        "right" -> Arrangement.End
        else -> Arrangement.Center
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (slideData.mediaContent is SlideImage) {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(src)
                    .apply {
                        headers {
                            append("User-Agent", "Mozilla/5.0")
                            append("Referer", "https://storage.yandexcloud.net/")
                        }
                    }
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
        } else if (slideData.mediaContent is SlideVideo) {

            //val videoSrc = "https://storage.yandexcloud.net/media-screen/560255de7a2498a4ca653a13eb776f18e2ea53d3bffd83fc2bd8ce3310d9bc79.mp4"
            //val videoSrc = "D:\\Projects\\Kotlin\\Android\\KMP\\BigDisplayProject\\sliderVideo\\video1.mp4"
            val videoSrc = slideData.mediaContent.videoContent.src

            val videoPlayerState = rememberVideoPlayerState()
            VideoPlayer(
                mrl = videoSrc,
                state = videoPlayerState,
                modifier = Modifier.fillMaxWidth()
            )
            LaunchedEffect(videoSrc) {
                videoPlayerState.doWithMediaPlayer { mediaPlayer ->
                    mediaPlayer.play()
                }
            }
            /*LaunchedEffect(videoSrc) {
                videoPlayerState.doWithMediaPlayer { mediaPlayer ->
                    mediaPlayer.addOnTimeChangedListener(
                        object : OnTimeChangedListener {
                            override fun onTimeChanged(timeMillis: Long) {
                            }
                        }
                    )
                }
            }*/
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = (40).pxToDp())
                    .clickable(
                        interactionSource = leftInteractionSource,
                        indication = null
                    ) {
                        onLeftButtonClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "",
                    tint = LightWhite,
                    modifier = Modifier
                        .hoverable(leftInteractionSource)
                        .size((100).pxToDp())
                        .graphicsLayer {
                            scaleY = leftScale
                            scaleX = leftScale
                            transformOrigin = TransformOrigin(0.5f, 0.5f)
                            clip = true
                        }
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(
                        top = 50.dp,
                        start = (70).pxToDp(),
                        bottom = (155).pxToDp(),
                        end = (70).pxToDp()
                    ),
                verticalArrangement = verticalArrangement,
                horizontalAlignment = horizontalAlignment
            ) {
                Text(
                    text = slideData.heading.uppercase(Locale.getDefault()),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier
                        .align(headingAlignment),
                    lineHeight = 52.sp
                )
                Spacer(modifier = Modifier.height((50).pxToDp()))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = attachmentsArrangement
                ) {
                    if (slideData.attachments.time != null) {
                        Icon(
                            imageVector = Icons.Filled.AccessTime,
                            contentDescription = "",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width((10).pxToDp()))
                        Text(
                            text = slideData.attachments.time,
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        spacerFlag = true
                    }
                    if (slideData.attachments.date != null) {
                        if (spacerFlag) Spacer(modifier = Modifier.width((10).pxToDp()))
                        Text(
                            text = slideData.attachments.date,
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        spacerFlag = true
                    }
                    if (slideData.attachments.location != null) {
                        if (spacerFlag) Spacer(modifier = Modifier.width((50).pxToDp()))
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width((10).pxToDp()))
                        Text(
                            text = slideData.attachments.location,
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        spacerFlag = true
                    }
                }
                if (spacerFlag) {
                    Spacer(modifier = Modifier.height((50).pxToDp()))
                    spacerFlag = false
                }
                Text(
                    text = slideData.text,
                    fontSize = 24.sp,
                    color = Color.White,
                    modifier = Modifier
                        .align(textAlignment),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 6
                )

                LinksBlock(slideData, onNewsLinkClick, onScheduleLinkClick)

            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = (40).pxToDp())
                    .clickable(
                        interactionSource = rightInteractionSource,
                        indication = null
                    ) {
                        onRightButtonClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "",
                    tint = LightWhite,
                    modifier = Modifier
                        .size((100).pxToDp())
                        .hoverable(rightInteractionSource)
                        .graphicsLayer {
                            scaleY = rightScale
                            scaleX = rightScale
                            transformOrigin = TransformOrigin(0.5f, 0.5f)
                            clip = true
                        }
                )
            }
        }
    }
}