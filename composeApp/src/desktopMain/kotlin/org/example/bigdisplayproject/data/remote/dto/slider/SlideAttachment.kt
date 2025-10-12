package org.example.bigdisplayproject.data.remote.dto.slider

import kotlinx.serialization.Serializable

@Serializable
data class SlideAttachment(
    val date: String? = "",
    val time: String? = "",
    val album: String?,  // TODO: Я предположил, что album, audio, documentation типа String. В идеале узнать точно
    val audio: String?,
    val links: Long?,
    val video: SlideVideo?,
    val images: Int?,
    val location: String?,
    val documentation: Long?
)