package org.example.bigdisplayproject.data.remote.dto.slider

import kotlinx.serialization.Serializable

@Serializable
sealed class MediaContent {
    abstract val type: String
}