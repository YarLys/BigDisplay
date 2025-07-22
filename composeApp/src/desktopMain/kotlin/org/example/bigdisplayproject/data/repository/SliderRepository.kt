package org.example.bigdisplayproject.data.repository

import org.example.bigdisplayproject.data.remote.api.SliderApi
import org.example.bigdisplayproject.data.remote.dto.slider.GroupSlidesData
import org.example.bigdisplayproject.domain.util.NetworkError
import org.example.bigdisplayproject.domain.util.Result

class SliderRepository(
    private val sliderApi: SliderApi
) {

    suspend fun getSlides(): Result<GroupSlidesData, NetworkError> {
        return sliderApi.getSlides()
    }

}