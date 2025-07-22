package org.example.bigdisplayproject.data.remote.dto.slider

import kotlinx.serialization.Serializable

@Serializable
data class SlideLink(
    val link: String,
    val text: String
)
