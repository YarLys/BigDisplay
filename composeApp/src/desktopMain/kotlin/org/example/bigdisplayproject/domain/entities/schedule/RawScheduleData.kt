package org.example.bigdisplayproject.domain.entities.schedule

import kotlinx.serialization.Serializable

@Serializable
data class RawScheduleData(
    val data: List<ScheduleData>,
    val nextPageToken: String
)