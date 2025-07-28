package org.example.bigdisplayproject.data.remote.dto.slider

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("image")
data class SlideImage(
    @SerialName("image_type") override val type: String = "image",
    @SerialName("image") val image: ImageContent,
    @SerialName("hover_media_content") override val hover_media_content: Boolean
): MediaContent()
