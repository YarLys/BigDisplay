package org.example.bigdisplayproject.data.remote.dto.slider

import kotlinx.serialization.Serializable

@Serializable
data class SlideData(
    val id: Long,
    val mediaContent: MediaContent,
    val important: Boolean,
    val timeStart: Boolean,
    val timeEnd: Boolean,
    val timer: Long,
    val heading: String,
    val text: String,
    val links: List<SlideLink>,
    val attachments: List<SlideAttachment>,
    val sides: SlideSides,
    //val keyValue: List<>,
    val indexSlide: Long
)
