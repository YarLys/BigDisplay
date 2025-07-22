package org.example.bigdisplayproject.data.remote.dto.news

import kotlinx.serialization.Serializable

@Serializable
sealed class Attachment {
    abstract val type: String
}