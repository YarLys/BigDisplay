package org.example.bigdisplayproject.domain.usecases.slider

import org.example.bigdisplayproject.data.remote.dto.slider.GroupSlidesData
import org.example.bigdisplayproject.data.repository.SliderRepository
import org.example.bigdisplayproject.domain.util.NetworkError
import org.example.bigdisplayproject.domain.util.Result

class GetSlidesUseCase(
    private val sliderRepository: SliderRepository
) {

    suspend operator fun invoke(): Result<GroupSlidesData, NetworkError> {
        return sliderRepository.getSlides()
    }

}