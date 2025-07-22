package org.example.bigdisplayproject.data.remote.dto.slider

import kotlinx.serialization.Serializable

@Serializable
data class SlideAttachment(
    val date: String? = "",
    val time: String? = "",
    val links: List<SlideLink>?,
    val video: SlideVideo?,
    val images: List<SlideImage>?,
    val location: String?
    /*
    val album:
    val audio:
    val documentation:
    */
)