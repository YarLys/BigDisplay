package org.example.bigdisplayproject.data.repository

import org.example.bigdisplayproject.data.remote.api.ScheduleApi
import org.example.bigdisplayproject.domain.entities.schedule.ScheduleData
import org.example.bigdisplayproject.domain.util.NetworkError
import org.example.bigdisplayproject.domain.util.Result

class ScheduleRepository(
    private val scheduleApi: ScheduleApi
) {

    suspend fun getSchedule(name: String): Result<ScheduleData, NetworkError> {
        return scheduleApi.getSchedule(name)
    }

    suspend fun downloadCalendar(url: String): Result<String, NetworkError> {
        return scheduleApi.downloadCalendar(url)
    }

}