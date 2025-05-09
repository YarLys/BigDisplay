package org.example.bigdisplayproject.feature.display.presentation.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
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
import org.example.bigdisplayproject.ui.theme.DarkGray
import org.example.bigdisplayproject.ui.theme.GradientColor1
import org.example.bigdisplayproject.ui.theme.GradientColor10
import org.example.bigdisplayproject.ui.theme.GradientColor11
import org.example.bigdisplayproject.ui.theme.GradientColor12
import org.example.bigdisplayproject.ui.theme.GradientColor13
import org.example.bigdisplayproject.ui.theme.GradientColor14
import org.example.bigdisplayproject.ui.theme.GradientColor15
import org.example.bigdisplayproject.ui.theme.GradientColor2
import org.example.bigdisplayproject.ui.theme.GradientColor3
import org.example.bigdisplayproject.ui.theme.GradientColor4
import org.example.bigdisplayproject.ui.theme.GradientColor5
import org.example.bigdisplayproject.ui.theme.GradientColor6
import org.example.bigdisplayproject.ui.theme.GradientColor7
import org.example.bigdisplayproject.ui.theme.GradientColor8
import org.example.bigdisplayproject.ui.theme.GradientColor9
import org.example.bigdisplayproject.feature.display.presentation.util.Constants.PADDING_BETWEEN_CARDS
import org.example.bigdisplayproject.feature.display.presentation.util.pxToDp
import java.net.URI
import javax.swing.JEditorPane

@Composable
fun NewsList(
    newsList: List<News>,
    onItemClick: (Long) -> Unit,
    scrollPosition: Int,
    onScrollPositionChanged: (Int) -> Unit
) {
    /*val listState = rememberLazyGridState(
        initialFirstVisibleItemIndex = scrollPosition
    )*/
    val listState = rememberLazyStaggeredGridState(
        initialFirstVisibleItemIndex = scrollPosition
    )
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { onScrollPositionChanged(it) }
    }
    Scaffold(
        bottomBar = { BottomPanel({ /* todo */  }) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawWithCache {
                    val gradient = Brush.radialGradient(
                        colors = listOf(DarkGray, GradientColor1, GradientColor2, GradientColor3, GradientColor4, GradientColor5, GradientColor6,
                            GradientColor7, GradientColor8, GradientColor9, GradientColor10, GradientColor11, GradientColor12, GradientColor13,
                            GradientColor14, GradientColor15),
                        radius = 1500f,
                        center = Offset(size.width / 2f - 150, size.height / 2f)
                    )
                    onDrawBehind {
                        rotate(50f, pivot = center) {
                            scale(1f, 2f, pivot = center) {
                                drawRect(
                                    brush = gradient,
                                    size = Size(size.width * 2, size.height * 2),
                                    topLeft = Offset(-size.width/2, -size.height/2)
                                )
                            }
                        }
                    }
                }
        ) {
            LazyVerticalStaggeredGrid(
                state = listState,
                columns = StaggeredGridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 80.dp, vertical = 35.dp),
                horizontalArrangement = Arrangement.spacedBy(PADDING_BETWEEN_CARDS.pxToDp()),
                verticalItemSpacing = PADDING_BETWEEN_CARDS.pxToDp()
            ) {
                items(newsList) { news ->
                    NewsCard(news = news, onItemClick)
                }
            }
        }
    }
}