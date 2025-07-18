package org.example.bigdisplayproject.feature.display

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import org.example.bigdisplayproject.feature.display.network.dto.schedule.ScheduleData

interface ScheduleStore : Store<ScheduleStore.Intent, ScheduleStore.State, Nothing> {

    // Интенты
    sealed interface Intent : JvmSerializable {
        data class GetSchedule(val name: String): Intent
        data class DownloadCalendar(val url: String): Intent
    }

    // Сообщения от executor к reducer
    sealed interface Message {
        data class Error(val message: String) : Message
        object Loading : Message
        data class ScheduleLoaded(val scheduleData: ScheduleData): Message
        data class CalendarLoaded(val calendarData: String): Message
    }

    // Состояние
    data class State(
        val isLoading: Boolean = false,
        val scheduleData: ScheduleData? = null,
        val calendarData: String? = null,
        val error: String? = null,
    ) : JvmSerializable

}