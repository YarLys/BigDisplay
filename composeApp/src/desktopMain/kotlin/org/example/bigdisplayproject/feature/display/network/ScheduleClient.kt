package org.example.bigdisplayproject.feature.display.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import org.example.bigdisplayproject.feature.display.network.dto.news.News
import org.example.bigdisplayproject.feature.display.network.dto.schedule.RawScheduleData
import org.example.bigdisplayproject.feature.display.network.dto.schedule.ScheduleData
import org.example.bigdisplayproject.feature.display.util.NetworkError
import org.example.bigdisplayproject.feature.display.util.Result
import java.io.File

class ScheduleClient (
    private val httpClient: HttpClient
) {
    suspend fun getSchedule(name: String): Result<ScheduleData, NetworkError> {
        val response = try {
            httpClient.get(
                urlString = "https://schedule-of.mirea.ru/schedule/api/search" // спрятать в gradle или куда-то, где будет безопасно
            ) {
                parameter("limit", 1)
                parameter("match", name)
            }
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }

        return when(response.status.value) {
            in 200..299 -> {
                val scheduleData = response.body<RawScheduleData>().data[0]
                Result.Success(scheduleData)
            }
            400 -> Result.Error(NetworkError.WRONG_FORMAT)
            404 -> Result.Error(NetworkError.NOT_FOUND)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }

    suspend fun downloadCalendar(url: String): Result<String, NetworkError> {
        val response = try {
            httpClient.get(url)
        } catch (e: java.nio.channels.UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }

        return when(response.status.value) {
            in 200..299 -> {
                val content = response.bodyAsText()
                File("ScheduleText.txt").printWriter().use { out ->
                    out.println(content)
                }
                return Result.Success(content)
            }
            400 -> Result.Error(NetworkError.WRONG_FORMAT)
            404 -> Result.Error(NetworkError.NOT_FOUND)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }
}
