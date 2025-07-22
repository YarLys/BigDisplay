package org.example.bigdisplayproject.data.remote.dto.news

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("LINK")
data class Link(   // ссылки обычные
    @SerialName("link_type") override val type: String = "LINK", // тип для определения вложения
    val titleLink: String, // текст ссылки
    val link: String, // url ссылки
    val image: StaticImageData, // изображение ссылки
    val caption: String?, // сокращённая ссылка (для текстового отображения)
    val description: String // дополнительное текстовое описание ссылки (может возвращаться пустая строка)
) : Attachment()
