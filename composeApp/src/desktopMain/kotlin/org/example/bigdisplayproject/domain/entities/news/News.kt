package org.example.bigdisplayproject.domain.entities.news

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class News(
    val id: Long, // id новости, совпадает с id постом из группы ВК
    //@Serializable(with = Utf8StringSerializer::class)
    val text: String, // Текст поста (без хэштэга в начале)
    val date: Long, // Дата создания поста (в ВК)
    val type: String, // Тип новости (по сути хэштэг из начала поста)(существуют следующие типы новостей: Вика_Общее, Вика_PRO_ИТ, Вика_Наука, Вика_HITECH_REVISION, Вика_Сессия, Вика_sumirea, Вика_Соревнования, Вика_Вакансия, Вика_Вакансии, Вика_Дополнительное_образование, Вика_ЦК, Вика_Пересдачи. Если приходит НН тип, то рекомендую его определять как 'Вика_Общее' (такое тоже иногда бывает))
    val copyId: Long?, // id поста на который ссылается новость (Пример: 33772. Пример ссылки: https://vk.com/wall-1236_33772)(null - значит пост из Вики не ссылает на пост из другой группы ВК)
    val copyOid: Long?, // id группы поста на который ссылается новость (Пример: -1236. Пример ссылки: https://vk.com/wall-1236_33772)(null - значит пост из Вики не ссылает на пост из другой группы ВК)
    val image: StaticImageData?, // Изображение группы поста на который ссылается новость (null - значит пост из Вики не ссылает на пост из другой группы ВК)
    val name: String?, // Название группы поста на который ссылается новость (null - значит пост из Вики не ссылает на пост из другой группы ВК)
    @SerialName("attachments")
    val attachments: List<@Polymorphic Attachment> // Вложения
)