package org.example.bigdisplayproject.feature.display.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("DOC")
data class Doc(  // документы
    @SerialName("doc_type") override val type: String = "DOC", // тип для определения вложения
    val sizeFile: Long, // размер файла (в байтах)
    val titleFile: String, // название файла
    val file: String // ссылка на файл
) : Attachment()
