package org.example.bigdisplayproject.feature.display.network.dto.news

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ALBUM")
data class Album( // ссылки на фотоальбом
    @SerialName("album_type") override val type: String = "ALBUM", // тип для определения вложения
    val title: String, // название альбома
    val size: Long, // количество изображений в альбоме
    val ownerId: Long, // id альбома из группы
    val objectId: Long, // id группы в котором альбом
    val image: StaticImageData // обложка альбома
) : Attachment()
