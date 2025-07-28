package org.example.bigdisplayproject.data.remote.dto.schedule

import kotlinx.serialization.Serializable

@Serializable
data class RawScheduleData(
    val data: List<ScheduleData>,
    val nextPageToken: String?
)