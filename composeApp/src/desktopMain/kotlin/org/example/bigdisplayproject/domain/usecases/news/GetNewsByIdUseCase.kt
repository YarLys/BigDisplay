package org.example.bigdisplayproject.domain.usecases.news

import org.example.bigdisplayproject.data.repository.NewsRepository
import org.example.bigdisplayproject.data.remote.dto.news.News
import org.example.bigdisplayproject.domain.util.NetworkError
import org.example.bigdisplayproject.domain.util.Result

class GetNewsByIdUseCase(
    private val newsRepository: NewsRepository
) {

    suspend operator fun invoke(id: Long): Result<News, NetworkError> {
        return newsRepository.getNewsById(id)
    }

}