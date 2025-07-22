package org.example.bigdisplayproject.data.remote.dto.slider

import kotlinx.serialization.Serializable

@Serializable
data class RawSlideData(
    val data: GroupSlidesData,
    val message: String
)
