package org.example.bigdisplayproject.ui.slider

import Speed
import VideoPlayer
import VideoPlayerImpl
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RingVolume
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import io.ktor.http.headers
import org.example.bigdisplayproject.data.remote.dto.slider.SlideData
import org.example.bigdisplayproject.data.remote.dto.slider.SlideImage
import org.example.bigdisplayproject.data.remote.dto.slider.SlideVideo
import org.example.bigdisplayproject.ui.components.BottomPanel
import org.example.bigdisplayproject.ui.theme.LightWhite
import org.example.bigdisplayproject.ui.util.pxToDp
import rememberVideoPlayerState
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
    var spacerFlag = false
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
        }
        else if (slideData.mediaContent is SlideVideo) {
            /*VideoPlayerImpl(
                url = slideData.mediaContent.videoContent.src,
                modifier = Modifier
                    .fillMaxSize()
            )*/

/*
            val state = rememberVideoPlayerState()
            *//*
             * Could not use a [Box] to overlay the controls on top of the video.
             * See https://github.com/JetBrains/compose-multiplatform/tree/master/tutorials/Swing_Integration
             * Related issues:
             * https://github.com/JetBrains/compose-multiplatform/issues/1521
             * https://github.com/JetBrains/compose-multiplatform/issues/2926
             *//*
            Column {
                VideoPlayer(
                    url = "https://storage.yandexcloud.net/media-screen/560255de7a2498a4ca653a13eb776f18e2ea53d3bffd83fc2bd8ce3310d9bc79.mp4",
                    state = state,
                    onFinish = state::stopPlayback,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((950).pxToDp())
                        .background(Color.Transparent)
                )
                *//*androidx.compose.material3.Slider(
                    value = state.progress.value.fraction,
                    onValueChange = { state.seek = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Timestamp: ${state.progress.value.timeMillis} ms", modifier = Modifier.width(180.dp))
                    IconButton(onClick = state::toggleResume) {
                        Icon(
                            imageVector = if (state.isResumed) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = "Play/Pause",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    IconButton(onClick = state::toggleFullscreen) {
                        Icon(
                            imageVector = Icons.Filled.FullscreenExit,
                            contentDescription = "Toggle fullscreen",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Speed(
                        initialValue = state.speed,
                        modifier = Modifier.width(104.dp)
                    ) {
                        state.speed = it ?: state.speed
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.RingVolume,
                            contentDescription = "Volume",
                            modifier = Modifier.size(32.dp)
                        )
                        // TODO: Make the slider change volume in logarithmic manner
                        //  See https://www.dr-lex.be/info-stuff/volumecontrols.html
                        //  and https://ux.stackexchange.com/q/79672/117386
                        //  and https://dcordero.me/posts/logarithmic_volume_control.html
                        androidx.compose.material3.Slider(
                            value = state.volume,
                            onValueChange = { state.volume = it },
                            modifier = Modifier.width(100.dp)
                        )
                    }
                }*//*
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
                        .size((100).pxToDp())
                        .hoverable(leftInteractionSource)
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
                    .fillMaxHeight()
                    .padding(
                        top = 50.dp,
                        start = (70).pxToDp(),
                        bottom = (155).pxToDp(),
                        end = (70).pxToDp()
                    )
                    .weight(1f),
                verticalArrangement = verticalArrangement,
                horizontalAlignment = horizontalAlignment
            ) {
                Text(
                    text = slideData.heading.uppercase(Locale.getDefault()),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier
                        .align(headingAlignment)
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