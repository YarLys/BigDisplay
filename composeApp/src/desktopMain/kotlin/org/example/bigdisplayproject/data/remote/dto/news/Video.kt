package org.example.bigdisplayproject.data.remote.dto.news

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("VIDEO")
data class Video ( // видео/истории
    @SerialName("video_type") override val type: String = "VIDEO", // тип для определения вложения
    val title: String, // название видео
    val ownedId: Long, // id видео из группы
    val objectId: Long // id группы в котором видео
) : Attachment()