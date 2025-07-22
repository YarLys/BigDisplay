package org.example.bigdisplayproject.data.remote.api

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import org.example.bigdisplayproject.data.remote.NewsClient
import org.example.bigdisplayproject.data.remote.dto.news.News
import org.example.bigdisplayproject.domain.util.NetworkError
import org.example.bigdisplayproject.domain.util.Result

class NewsApi(
    newsClient: NewsClient
) {
    private val httpClient = newsClient.httpClient

    suspend fun getNews(): Result<List<News>, NetworkError> {
        val response = try {
            httpClient.get(
                urlString = "http://194.67.93.15:45455/api/v1/wall" // спрятать в gradle или куда-то, где будет безопасно
            ) {}
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }

        return when(response.status.value) {
            in 200..299 -> {
                val news = response.body<List<News>>()
                Result.Success(news)
            }
            400 -> Result.Error(NetworkError.WRONG_FORMAT)
            404 -> Result.Error(NetworkError.NOT_FOUND)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }

    suspend fun getNewsById(id: Long): Result<News, NetworkError> {
        val response = try {
            httpClient.get(
                urlString = "http://194.67.93.15:45455/api/v1/wall"
            ) {
                parameter("id", id)
            }
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }

        return when(response.status.value) {
            in 200..299 -> {
                val news = response.body<News>()
                Result.Success(news)
            }
            400 -> Result.Error(NetworkError.WRONG_FORMAT)
            404 -> Result.Error(NetworkError.NOT_FOUND)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }
}