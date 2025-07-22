package org.example.bigdisplayproject.ui.schedule.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kotlinx.datetime.LocalDate
import org.example.bigdisplayproject.domain.usecases.schedule.CalendarEvent
import org.example.bigdisplayproject.data.remote.dto.schedule.ScheduleData

interface ScheduleStore : Store<ScheduleStore.Intent, ScheduleStore.State, Nothing> {

    // Интенты
    sealed interface Intent : JvmSerializable {
        data class GetSchedule(val name: String): Intent
        data class DownloadCalendar(val url: String): Intent
        data class ParseCalendar(val calendarData: String): Intent
        data class GetEvents(val events: List<CalendarEvent>, val date: LocalDate): Intent
    }

    // Сообщения от executor к reducer
    sealed interface Message {
        data class Error(val message: String): Message
        object Loading: Message
        data class ScheduleLoaded(val scheduleData: ScheduleData): Message
        data class CalendarLoaded(val calendarData: String): Message
        data class CalendarParsed(val events: List<CalendarEvent>): Message
        data class EventsFiltered(val filteredEvents: List<CalendarEvent>): Message
    }

    // Состояние
    data class State(
        val isLoading: Boolean = false,
        val scheduleData: ScheduleData? = null,
        val calendarData: String? = null,
        val events: List<CalendarEvent>? = null,   // все пары из расписания этого семестра для выбранной группы
        val filteredEvents: List<CalendarEvent>? = null,   // пары выбранного дня
        val error: String? = null,
    ) : JvmSerializable

}