package org.example.bigdisplayproject.data.remote.dto.slider

import kotlinx.serialization.Serializable

@Serializable
data class SlideSides(
    val sideX: String,
    val sideY: String,
    val sideText: String,
    val sideLinks: String,
    val sideHeading: String,
    val sideAttachments: String,
)