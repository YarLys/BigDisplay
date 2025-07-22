package org.example.bigdisplayproject.ui.schedule.store

import com.arkivanov.mvikotlin.core.store.*
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.example.bigdisplayproject.feature.display.domain.schedule.CalendarEvent
import org.example.bigdisplayproject.feature.display.domain.schedule.CalendarParser
import org.example.bigdisplayproject.feature.display.domain.schedule.getEvents
import org.example.bigdisplayproject.data.remote.api.ScheduleApi
import org.example.bigdisplayproject.domain.usecases.schedule.ScheduleUseCases
import org.example.bigdisplayproject.domain.util.onError
import org.example.bigdisplayproject.domain.util.onSuccess

internal class ScheduleStoreFactory(
    private val storeFactory: StoreFactory,
    private val scheduleUseCases: ScheduleUseCases
) {   // когда-нибудь потом вынести storeFactory в DI

    fun create(): ScheduleStore =
        object : ScheduleStore, Store<ScheduleStore.Intent, ScheduleStore.State, Nothing> by storeFactory.create(
            name = "ScheduleStore",
            initialState = ScheduleStore.State(),
            executorFactory = { ExecutorImpl(scheduleUseCases) },
            reducer = ReducerImpl
        ) {}

    private class ExecutorImpl(
        private val scheduleUseCases: ScheduleUseCases
    ) : CoroutineExecutor<ScheduleStore.Intent, Nothing, ScheduleStore.State, ScheduleStore.Message, Nothing>() {

        override fun executeIntent(intent: ScheduleStore.Intent) {
            when (intent) {
                is ScheduleStore.Intent.GetSchedule -> getSchedule(intent.name)
                is ScheduleStore.Intent.DownloadCalendar -> downloadCalendar(intent.url)
                is ScheduleStore.Intent.ParseCalendar -> parseCalendar(intent.calendarData)
                is ScheduleStore.Intent.GetEvents -> filterEvents(intent.events, intent.date)
            }
        }

        private fun getSchedule(name: String) = scope.launch {
            dispatch(ScheduleStore.Message.Loading)
            val scheduleData = scheduleUseCases.getScheduleUseCase(name)
                .onSuccess {
                    println("GET SCHEDULE: SUCCESS")
                    dispatch(ScheduleStore.Message.ScheduleLoaded(it))
                }
                .onError {
                    println("ERROR: ${it.toString()}")
                    dispatch(ScheduleStore.Message.Error(it.toString()))
                }
        }

        private fun downloadCalendar(url: String) = scope.launch {
            dispatch(ScheduleStore.Message.Loading)
            val calendarData = scheduleUseCases.downloadCalendarUseCase(url)
                .onSuccess {
                    println("DOWNLOAD CALENDAR: SUCCESS")
                    dispatch(ScheduleStore.Message.CalendarLoaded(it))
                }
                .onError {
                    println("ERROR: ${it.toString()}")
                    dispatch(ScheduleStore.Message.Error(it.toString()))
                }
        }

        private fun parseCalendar(data: String) {
            dispatch(ScheduleStore.Message.Loading)

            val parser = CalendarParser()
            parser.parseCalendar(data)

            dispatch(ScheduleStore.Message.CalendarParsed(parser.events))
        }

        private fun filterEvents(events: List<CalendarEvent>, date: LocalDate) {
            dispatch(ScheduleStore.Message.Loading)
            val filteredEvents = getEvents(events, date)
            dispatch(ScheduleStore.Message.EventsFiltered(filteredEvents))
        }

    }

    private object ReducerImpl : Reducer<ScheduleStore.State, ScheduleStore.Message> {
        override fun ScheduleStore.State.reduce(msg: ScheduleStore.Message): ScheduleStore.State =
            when (msg) {
                is ScheduleStore.Message.Error -> copy(isLoading = false, error = msg.message)
                is ScheduleStore.Message.Loading -> copy(isLoading = true)
                is ScheduleStore.Message.ScheduleLoaded -> copy(
                    isLoading = false,
                    scheduleData = msg.scheduleData,
                    calendarData = null, // Если обновилась scheduleData, значит обновилось название группы. Тогда calendarData является устаревшей
                    events = null,  // И события тоже устарели
                    filteredEvents = null,
                    error = null
                )
                is ScheduleStore.Message.CalendarLoaded -> copy(
                    isLoading = false,
                    calendarData = msg.calendarData,
                    error = null
                )
                is ScheduleStore.Message.CalendarParsed -> copy(
                    isLoading = false,
                    events = msg.events,
                    error = null
                )
                is ScheduleStore.Message.EventsFiltered -> copy(
                    isLoading = false,
                    filteredEvents = msg.filteredEvents,
                    error = null
                )
            }
    }
}
