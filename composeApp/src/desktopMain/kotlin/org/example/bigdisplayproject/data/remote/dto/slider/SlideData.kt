package org.example.bigdisplayproject.data.remote.dto.slider

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SlideData(
    val id: Long,
    @SerialName("mediaContent")
    val mediaContent: @Polymorphic MediaContent,
    val important: Boolean,
    val timeStart: Boolean,
    val timeEnd: Boolean,
    val timer: Long,
    val heading: String,
    val text: String,
    val links: List<SlideLink>,
    val attachments: SlideAttachment,
    val sides: SlideSides,
    val keyValue: List<String>?, // TODO: в идеале узнать, что тут может храниться. Пока что приходят ответы с пустым списком.
    val indexSlide: Long
)
