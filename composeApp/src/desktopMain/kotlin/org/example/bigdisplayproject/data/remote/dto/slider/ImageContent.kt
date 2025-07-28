package org.example.bigdisplayproject.data.remote.dto.slider

import kotlinx.serialization.Serializable

@Serializable
data class ImageContent(
    val src: String,
    val width: Long,
    val height: Long
)
