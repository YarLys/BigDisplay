package org.example.bigdisplayproject.feature.display.network.dto.news

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("image")
data class StaticImageData(   // объект изображения
    val src: String, // ссылка
    val width: Long, // ширина
    val height: Long // высота
)