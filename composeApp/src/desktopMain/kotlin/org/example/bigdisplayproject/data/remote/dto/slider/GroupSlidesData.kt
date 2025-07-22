package org.example.bigdisplayproject.data.remote.dto.slider

import kotlinx.serialization.Serializable

@Serializable
data class GroupSlidesData(
    val importantSlide: List<SlideData>,
    val defaultSlide: List<SlideData>
)
