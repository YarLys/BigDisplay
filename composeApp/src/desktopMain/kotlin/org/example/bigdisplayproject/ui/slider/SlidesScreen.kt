package org.example.bigdisplayproject.ui.slider

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.example.bigdisplayproject.data.remote.dto.slider.SlideData
import org.example.bigdisplayproject.data.remote.dto.slider.SlideLink

@Composable
fun SlidesScreen(
    slides: List<SlideData>,
    onNewsLinkClick: (Long) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { slides.size })
    val coroutineScope = rememberCoroutineScope()

    HorizontalPager(
        state = pagerState,
        key = { slides[it] },
        modifier = Modifier.fillMaxSize()
    ) { page ->
        SlideItem(
            slideData = slides[page],
            showLeftArrow = page > 0,
            showRightArrow = page < slides.size - 1,
            onLeftButtonClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            },
            onRightButtonClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            },
            onNewsLinkClick = onNewsLinkClick
        )
    }
}