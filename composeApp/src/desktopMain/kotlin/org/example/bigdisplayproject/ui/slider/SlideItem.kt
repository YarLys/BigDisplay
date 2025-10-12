package org.example.bigdisplayproject.ui.slider

//import VideoPlayer
import VideoPlayer
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
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
import kotlinx.coroutines.delay
import org.example.bigdisplayproject.data.remote.dto.slider.SlideData
import org.example.bigdisplayproject.data.remote.dto.slider.SlideImage
import org.example.bigdisplayproject.data.remote.dto.slider.SlideVideo
import org.example.bigdisplayproject.ui.components.BottomPanel
import org.example.bigdisplayproject.ui.components.myShadow
import org.example.bigdisplayproject.ui.theme.LightWhite
import org.example.bigdisplayproject.ui.util.pxToDp
import rememberVideoPlayerState
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent
import java.awt.Component
import java.util.Locale
import javax.swing.BoxLayout
import javax.swing.JPanel

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
            /*VideoPlayer(modifier = Modifier.fillMaxWidth().height((950).pxToDp()),
                url = "https://storage.yandexcloud.net/media-screen/560255de7a2498a4ca653a13eb776f18e2ea53d3bffd83fc2bd8ce3310d9bc79.mp4", // Automatically Detect the URL, Wether to Play YouTube Video or .mp4 e.g
                autoPlay = true,
                showControls = true,
            )*/

            /*val state = rememberVideoPlayerState()
            VideoPlayer(
                url = "https://storage.yandexcloud.net/media-screen/560255de7a2498a4ca653a13eb776f18e2ea53d3bffd83fc2bd8ce3310d9bc79.mp4",
                state = state,
                onFinish = state::stopPlayback,
                modifier = Modifier
                    .fillMaxWidth()
                    .height((950).pxToDp())
            )*/

            /*val url = "https://storage.yandexcloud.net/media-screen/560255de7a2498a4ca653a13eb776f18e2ea53d3bffd83fc2bd8ce3310d9bc79.mp4"
            val mediaPlayerComponent = initializeMediaPlayerComponent()
            val mediaPlayer = mediaPlayerComponent.mediaPlayer()
            val mediaContainer = remember {
                JPanel().apply {
                    layout = BoxLayout(this, BoxLayout.Y_AXIS)
                    add(mediaPlayerComponent)
                }
            }

            LaunchedEffect(url) {
                delay(200)
                mediaPlayer.media().play(url)
            }
            DisposableEffect(Unit) { onDispose(mediaPlayer::release) }

            ComposePanel().apply {
                setContent {
                    // 3. Внутри Compose используем Box для слоев
                    Box(modifier = Modifier.fillMaxSize()) {
                        // 4. Слой с видео (нижний)
                        SwingPanel(
                            modifier = Modifier.matchParentSize(),
                            factory = { mediaContainer }
                        )
                        // 5. Слой с элементами управления (верхний)
                        Row(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            horizontalArrangement = Arrangement.SpaceAround
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
                    }
                }
            }*/

            /*SwingPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((950).pxToDp()),
                factory = {
                    ComposePanel().apply {
                        setContent {
                            Box {
                                SwingPanel(
                                    modifier = Modifier
                                        .fillMaxSize(), 
                                    factory = {
                                        mediaContainer
                                    }
                                )
                                Row(
                                    horizontalArrangement = Arrangement.SpaceAround
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
                            }
                        }
                    }
                },
                background = Color.Transparent
            )*/
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








// TODO: FOR TEST, MB DELETE
private fun initializeMediaPlayerComponent(): Component {
    NativeDiscovery().discover()
    return if (isMacOS()) {
        CallbackMediaPlayerComponent()
    } else {
        EmbeddedMediaPlayerComponent()
    }
}

private fun Component.mediaPlayer() = when (this) {
    is CallbackMediaPlayerComponent -> mediaPlayer()
    is EmbeddedMediaPlayerComponent -> mediaPlayer()
    else -> error("mediaPlayer() can only be called on vlcj player components")
}

private fun isMacOS(): Boolean {
    val os = System
        .getProperty("os.name", "generic")
        .lowercase(Locale.ENGLISH)
    return "mac" in os || "darwin" in os
}