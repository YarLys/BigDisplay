package org.example.bigdisplayproject.data.remote.dto.slider

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("video")
data class SlideVideo(
    @SerialName("video_type") override val type: String = "video",
    @SerialName("video") val videoContent: VideoContent,
    @SerialName("hover_media_content") override val hover_media_content: Boolean
): MediaContent()
