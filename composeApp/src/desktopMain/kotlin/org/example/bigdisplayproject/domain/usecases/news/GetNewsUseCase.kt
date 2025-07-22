package org.example.bigdisplayproject.domain.usecases.news

import org.example.bigdisplayproject.data.repository.NewsRepository
import org.example.bigdisplayproject.domain.entities.news.News
import org.example.bigdisplayproject.domain.util.NetworkError
import org.example.bigdisplayproject.domain.util.Result

class GetNewsUseCase(
    private val newsRepository: NewsRepository
) {

    suspend operator fun invoke(): Result<List<News>, NetworkError> {
        return newsRepository.getNews()
    }

}