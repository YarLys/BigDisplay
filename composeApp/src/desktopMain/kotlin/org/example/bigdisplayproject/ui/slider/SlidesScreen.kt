package org.example.bigdisplayproject.ui.slider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.bigdisplayproject.data.remote.dto.slider.SlideData
import org.example.bigdisplayproject.data.remote.dto.slider.SlideLink
import org.example.bigdisplayproject.ui.theme.LightWhite
import org.example.bigdisplayproject.ui.util.pxToDp

@Composable
fun SlidesScreen(
    slides: List<SlideData>,
    onNewsLinkClick: (Long) -> Unit,
    onScheduleLinkClick: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { slides.size })
    val coroutineScope = rememberCoroutineScope()

    // Таймер для автоматического перелистывания
    LaunchedEffect(pagerState.currentPage, slides.size) {
        while (true) {
            val time = slides[pagerState.currentPage].timer
            delay(time * 1000)

            if (slides.isNotEmpty()) {
                val nextPage = (pagerState.currentPage + 1) % slides.size
                coroutineScope.launch {
                    pagerState.animateScrollToPage(nextPage)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        HorizontalPager(
            state = pagerState,
            key = { slides[it] },
            modifier = Modifier.fillMaxSize()
        ) { page ->
            SlideItem(
                slideData = slides[page],
                onLeftButtonClick = {
                    coroutineScope.launch {
                        if (pagerState.currentPage > 0)
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        else
                            pagerState.animateScrollToPage(pagerState.pageCount - 1)
                    }
                },
                onRightButtonClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage((pagerState.currentPage + 1) % pagerState.pageCount)
                    }
                },
                onNewsLinkClick = onNewsLinkClick,
                onScheduleLinkClick = onScheduleLinkClick
            )
        }
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = (115).pxToDp()),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) LightWhite else LightWhite.copy(alpha = 0.25f)
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}