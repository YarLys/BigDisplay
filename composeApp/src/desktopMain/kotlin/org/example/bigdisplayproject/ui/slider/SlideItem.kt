package org.example.bigdisplayproject.ui.slider

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
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import org.example.bigdisplayproject.data.remote.dto.slider.SlideLink
import org.example.bigdisplayproject.ui.theme.LightWhite
import org.example.bigdisplayproject.ui.theme.White2
import org.example.bigdisplayproject.ui.util.pxToDp
import java.util.Locale

@Composable
fun SlideItem(
    slideData: SlideData,
    onLeftButtonClick: () -> Unit,
    showLeftArrow: Boolean,
    showRightArrow: Boolean,
    onRightButtonClick: () -> Unit,
    onLinkClick: (SlideLink) -> Unit
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
    val linksAlignment = when (slideData.sides.sideLinks) {
        "left" -> Alignment.Start
        "right" -> Alignment.End
        else -> Alignment.CenterHorizontally
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
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
            if (showLeftArrow) {
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
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(
                        top = 50.dp,
                        start = (70).pxToDp(),
                        bottom = (105).pxToDp(),
                        end = (70).pxToDp()
                    )
                    .weight(1f),
                verticalArrangement = verticalArrangement,
                horizontalAlignment = horizontalAlignment
            ) {
                Text(
                    text = slideData.heading,
                    fontSize = 48.sp,
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
                        .align(textAlignment)
                )

                Spacer(modifier = Modifier.height((50).pxToDp()))
                /*if (slideData.links.isNotEmpty()) {
                    // TODO
                    LazyHorizontalStaggeredGrid(
                        rows = StaggeredGridCells.Adaptive(minSize = (70).pxToDp()),
                        modifier = Modifier.fillMaxWidth().align(horizontalAlignment),
                        verticalArrangement = Arrangement.spacedBy((30).pxToDp()),
                        horizontalItemSpacing = (30).pxToDp()
                    ) {
                        items(slideData.links) { link ->
                            Button(
                                onClick = {

                                },
                                colors = ButtonColors(
                                    containerColor = White2,
                                    contentColor = White2,
                                    disabledContentColor = White2,
                                    disabledContainerColor = White2
                                ),
                                modifier = Modifier
                                    .height((70).pxToDp())
                            ) {
                                Text(
                                    text = link.text.uppercase(Locale.getDefault()),
                                    fontSize = 20.sp,
                                    color = Color.Black,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }*/
            }
            if (showRightArrow) {
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
}