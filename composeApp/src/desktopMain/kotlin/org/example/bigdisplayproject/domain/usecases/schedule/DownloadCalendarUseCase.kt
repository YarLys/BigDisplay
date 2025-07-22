package org.example.bigdisplayproject.domain.usecases.schedule

import org.example.bigdisplayproject.data.repository.ScheduleRepository
import org.example.bigdisplayproject.domain.util.NetworkError
import org.example.bigdisplayproject.domain.util.Result

class DownloadCalendarUseCase(
    private val scheduleRepository: ScheduleRepository
) {

    suspend operator fun invoke(url: String): Result<String, NetworkError> {
        return scheduleRepository.downloadCalendar(url)
    }

}