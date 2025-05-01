package org.example.bigdisplayproject.feature.display.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import org.example.bigdisplayproject.feature.display.network.dto.News
import org.example.bigdisplayproject.feature.display.util.NetworkError
import org.example.bigdisplayproject.feature.display.util.Result
import java.io.File

class NewsClient(
    private val httpClient: HttpClient
) {

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

        // удалить потом. использовалось для проверки, что данные в utf-8
        /*val bytes: ByteArray = response.body()
        val utf8String = bytes.toString(Charsets.UTF_8)
        File("debug_output.txt").writeText(utf8String, Charsets.UTF_8)*/

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