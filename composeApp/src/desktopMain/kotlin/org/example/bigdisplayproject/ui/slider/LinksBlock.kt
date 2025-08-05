package org.example.bigdisplayproject.ui.slider

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.example.bigdisplayproject.data.remote.dto.slider.SlideData
import org.example.bigdisplayproject.data.remote.dto.slider.SlideLink
import org.example.bigdisplayproject.ui.theme.DarkGray
import org.example.bigdisplayproject.ui.theme.LightWhite
import org.example.bigdisplayproject.ui.theme.White2
import org.example.bigdisplayproject.ui.util.QrCode
import org.example.bigdisplayproject.ui.util.pxToDp
import java.util.Locale

@Composable
fun LinksBlock(
    slideData: SlideData,
    onNewsLinkClick: (Long) -> Unit
) {
    var showQrDialog by remember { mutableStateOf(false) }
    var selectedLink by remember { mutableStateOf<SlideLink?>(null) }

    val onLinkClick: (SlideLink) -> Unit = { link ->
        selectedLink = link
        showQrDialog = true
    }

    // Отображение QR-кода
    if (showQrDialog && selectedLink != null) {
        if (!slideData.keyValue.isNullOrEmpty()) {
            if (slideData.keyValue[0].key == "id") {
                // TODO: Проверить переход на экран новостей и выбор новости с этим id
                onNewsLinkClick(slideData.keyValue[0].value)
            }
        }
        else {
            LinkQrCode(
                link = selectedLink!!,
                onDismissRequest = { showQrDialog = false }
            )
        }
    }

    if (slideData.links.size > 1) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 175.dp)
        ) {
            LazyHorizontalStaggeredGrid(
                rows = StaggeredGridCells.Adaptive(minSize = (50).pxToDp()),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp),
                verticalArrangement = Arrangement.spacedBy((25).pxToDp()),
                horizontalItemSpacing = (25).pxToDp()
            ) {
                items(slideData.links) { link ->
                    LinkButton(link, onLinkClick)
                }
            }
        }
    }
    else if (slideData.links.size == 1) {
        val link = slideData.links[0]
        val linksAlignment = when (slideData.sides.sideLinks) {
            "left" -> Alignment.TopStart
            "right" -> Alignment.TopEnd
            else -> Alignment.Center
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            contentAlignment = linksAlignment
        ) {
            LinkButton(
                link = link,
                onLinkClick = onLinkClick
            )
        }
    }
}

@Composable
fun LinkButton(link: SlideLink, onLinkClick: (SlideLink) -> Unit) {
    Button(
        onClick = {
            onLinkClick(link)
        },
        colors = ButtonColors(
            containerColor = White2,
            contentColor = White2,
            disabledContentColor = White2,
            disabledContainerColor = White2
        ),
        modifier = Modifier
            .height((50).pxToDp())
    ) {
        Text(
            text = link.text.uppercase(Locale.getDefault()),
            fontSize = 16.sp,
            color = Color.Black,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun LinkQrCode(
    link: SlideLink,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(    // TODO: нужно добавить кнопку-крестик для закрытия
            modifier = Modifier
                .fillMaxWidth()
                .height(365.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val interactionSource = remember { MutableInteractionSource() }
                val isHovered by interactionSource.collectIsHoveredAsState()
                Box(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 6.dp, end = 10.dp)
                        .size(30.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { onDismissRequest() }
                        .background(
                            color = LightWhite,
                            shape = CircleShape
                        )
                        .border(
                            width = if (isHovered) 2.dp else 0.dp,
                            color = if (isHovered) DarkGray else LightWhite,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Закрыть",
                        tint = DarkGray,
                        modifier = Modifier.size(22.dp)
                    )
                }

                QrCode(
                    src = link.link,
                    width = 300,
                    height = 300,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 4.dp)
                )
                Text(
                    text = link.text,
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                        .padding(top = 10.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )
            }
        }
    }
}