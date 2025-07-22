package org.example.bigdisplayproject.data.remote.dto.slider

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SlideVideo(
    @SerialName("video_type") override val type: String = "video",
    val src: String,
    val width: Long,
    val height: Long,
    val hover_media_content: Boolean
): MediaContent()
