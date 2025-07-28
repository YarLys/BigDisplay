package org.example.bigdisplayproject.data.remote.api

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import org.example.bigdisplayproject.data.remote.ScheduleClient
import org.example.bigdisplayproject.data.remote.dto.schedule.RawScheduleData
import org.example.bigdisplayproject.data.remote.dto.schedule.ScheduleData
import org.example.bigdisplayproject.domain.util.CustomError
import org.example.bigdisplayproject.domain.util.Error
import org.example.bigdisplayproject.domain.util.NetworkError
import org.example.bigdisplayproject.domain.util.Result
import java.io.File

class ScheduleApi (
    scheduleClient: ScheduleClient
) {
    private val httpClient = scheduleClient.httpClient

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
                val rawData = response.body<RawScheduleData>()
                if (rawData.data.isEmpty()) {
                    Result.Error(NetworkError.WRONG_NAME)
                }
                else {
                    val scheduleData = response.body<RawScheduleData>().data[0]
                    Result.Success(scheduleData)
                }
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
