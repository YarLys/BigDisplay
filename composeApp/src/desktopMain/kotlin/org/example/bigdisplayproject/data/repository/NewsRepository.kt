package org.example.bigdisplayproject.data.repository

import org.example.bigdisplayproject.data.remote.api.NewsApi
import org.example.bigdisplayproject.data.remote.dto.news.News
import org.example.bigdisplayproject.domain.util.NetworkError
import org.example.bigdisplayproject.domain.util.Result

class NewsRepository(
    private val newsApi: NewsApi
) {

    suspend fun getNews(): Result<List<News>, NetworkError> {
        return newsApi.getNews()
    }

    suspend fun getNewsById(id: Long): Result<News, NetworkError> {
        return newsApi.getNewsById(id)
    }

}