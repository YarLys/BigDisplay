package org.example.bigdisplayproject.domain.usecases.schedule

import org.example.bigdisplayproject.data.repository.ScheduleRepository
import org.example.bigdisplayproject.data.remote.dto.schedule.ScheduleData
import org.example.bigdisplayproject.domain.util.NetworkError
import org.example.bigdisplayproject.domain.util.Result

class GetScheduleUseCase(
    private val scheduleRepository: ScheduleRepository
) {

    suspend operator fun invoke(name: String): Result<ScheduleData, NetworkError> {
        return scheduleRepository.getSchedule(name)
    }

}