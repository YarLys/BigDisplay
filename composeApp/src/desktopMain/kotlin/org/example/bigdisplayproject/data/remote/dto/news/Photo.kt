package org.example.bigdisplayproject.data.remote.dto.news

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("PHOTO")
data class Photo(   // фотографии
    @SerialName("photo_type") override val type: String = "PHOTO", // тип для определения вложения
    @SerialName("image") val image: StaticImageData // изображение
) : Attachment()