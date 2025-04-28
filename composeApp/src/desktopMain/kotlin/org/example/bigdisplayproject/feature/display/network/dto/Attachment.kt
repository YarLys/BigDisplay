package org.example.bigdisplayproject.feature.display.network.dto

import kotlinx.serialization.Serializable

@Serializable
sealed class Attachment {
    abstract val type: String

    /*class Album(type: String, title: String, size: Long, ownerId: Long, objectId: Long, image: StaticImageData): Attachment()
    class Doc(type: String, sizeFile: Long, titleFile: String, file: String): Attachment()
    class Link(type: String, titleLink: String, link: String, image: StaticImageData, caption: String?, description: String): Attachment()
    class Poll(type: String, id: Long, owner_id: Long, question: String, answers: List<String>, anonymous: Boolean,
               multiple: Boolean, end_date: Long): Attachment()
    class Photo(type: String, image: StaticImageData): Attachment()
    class Video(type: String, title: String, ownedId: Long, objectId: Long): Attachment()*/
}