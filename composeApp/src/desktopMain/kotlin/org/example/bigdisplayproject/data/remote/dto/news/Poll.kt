package org.example.bigdisplayproject.data.remote.dto.news

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("POLL")
data class Poll(   // голосование (метаинформация, за что голосуют, сколько осталось, закрытый ли опрос и т.д.)
    @SerialName("poll_type") override val type: String = "POLL", // тип для определения вложения
    val id: Long, // id голосования (Пример: 968965781. Пример ссылки: https://vk.com/poll-3865_968965781)
    val owner_id: Long, // id группы в которой опрос (Пример: -3865. Пример ссылки: https://vk.com/poll-3865_968965781)
    val question: String, // вопрос голосования
    val answers: List<String>, // варианты ответов голосования
    val anonymous: Boolean, // анонимное ли голосование (true - анонимное, false - публичное)
    val multiple: Boolean, // можно несколько выбирать вариантов (true - можно выбрать несколько, false - только один вариант ответа можно выбрать)
    val end_date: Long // дата/время конца голосования
) : Attachment()
