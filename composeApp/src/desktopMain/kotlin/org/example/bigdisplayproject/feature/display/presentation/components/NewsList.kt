package org.example.bigdisplayproject.feature.display.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.example.bigdisplayproject.feature.display.network.dto.News

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsList(
    news: List<News>,
    onItemClick: (Long) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    /*val refreshState = rememberPullToRefreshState()
    PullToRefreshBox(
        state = refreshState,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
    ) {*/
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(news) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(item.id) }
                ) {
                    Text(
                        item.text,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    //}
}