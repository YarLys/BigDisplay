package org.example.bigdisplayproject.feature.display.network.dto.schedule

import kotlinx.serialization.Serializable

@Serializable
data class RawScheduleData(
    val data: List<ScheduleData>,
    val nextPageToken: String
)