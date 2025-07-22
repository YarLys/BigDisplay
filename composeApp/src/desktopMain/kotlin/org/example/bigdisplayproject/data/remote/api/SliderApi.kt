package org.example.bigdisplayproject.data.remote.api

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import org.example.bigdisplayproject.data.remote.SliderClient
import org.example.bigdisplayproject.data.remote.dto.slider.GroupSlidesData
import org.example.bigdisplayproject.data.remote.dto.slider.RawSlideData
import org.example.bigdisplayproject.data.remote.dto.slider.SlideData
import org.example.bigdisplayproject.domain.util.NetworkError
import org.example.bigdisplayproject.domain.util.Result

class SliderApi(
    sliderClient: SliderClient
) {
    private val httpClient = sliderClient.httpClient

    suspend fun getSlides(): Result<GroupSlidesData, NetworkError> {
        val response = try {
            httpClient.get(
                urlString = "http://86.110.212.14:4000/rest/api/slides/screen_http"
            ) {}
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }

        return when(response.status.value) {
            in 200..299 -> {
                val sliderData = response.body<RawSlideData>()
                if (sliderData.message == "Успешно") {
                    val slides = sliderData.data
                    Result.Success(slides)
                }
                else Result.Error(NetworkError.SERVER_ERROR)
            }
            400 -> Result.Error(NetworkError.WRONG_FORMAT)
            404 -> Result.Error(NetworkError.NOT_FOUND)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }
}