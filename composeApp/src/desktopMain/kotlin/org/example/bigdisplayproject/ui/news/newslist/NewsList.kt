package org.example.bigdisplayproject.ui.news.newslist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.dp
import org.example.bigdisplayproject.domain.entities.news.News
import org.example.bigdisplayproject.ui.components.BottomPanel
import org.example.bigdisplayproject.ui.util.Constants.CARD_WIDTH
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
import org.example.bigdisplayproject.ui.util.Constants.PADDING_BETWEEN_CARDS
import org.example.bigdisplayproject.ui.util.pxToDp

@Composable
fun NewsList(
    newsList: List<News>,
    listState: LazyStaggeredGridState,
    onItemClick: (Long) -> Unit,
    onButtonClick: () -> Unit
) {
    Scaffold(
        bottomBar = { BottomPanel(onButtonClick = onButtonClick) }
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
                columns = StaggeredGridCells.Adaptive(minSize = CARD_WIDTH.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 80.dp, vertical = 35.dp),
                horizontalArrangement = Arrangement.spacedBy(PADDING_BETWEEN_CARDS.pxToDp()),
                verticalItemSpacing = PADDING_BETWEEN_CARDS.pxToDp()
            ) {
                items(items = newsList) { news ->
                    NewsCard(news = news, onItemClick)
                }
            }
        }
    }
}