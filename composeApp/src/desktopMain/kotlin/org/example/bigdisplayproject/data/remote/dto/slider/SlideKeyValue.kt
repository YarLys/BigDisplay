package org.example.bigdisplayproject.data.remote.dto.slider

import kotlinx.serialization.Serializable

@Serializable
data class SlideKeyValue(
    val key: String,
    val type: String,
    val title: String,
    val value: Long
)