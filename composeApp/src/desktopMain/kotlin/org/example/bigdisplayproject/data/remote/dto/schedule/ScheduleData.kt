package org.example.bigdisplayproject.data.remote.dto.schedule

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleData(
    val id: Long,
    val targetTitle: String,
    val fullTitle: String,
    val scheduleTarget: Long,
    val iCalLink: String,
    val scheduleImageLink: String,
    val scheduleUpdateImageLink: String,
    val scheduleUIAddToCalendarLink: String
)