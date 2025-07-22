package org.example.bigdisplayproject.domain.usecases.schedule

data class ScheduleUseCases(
    val getScheduleUseCase: GetScheduleUseCase,
    val downloadCalendarUseCase: DownloadCalendarUseCase
)
