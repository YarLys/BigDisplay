package org.example.bigdisplayproject.domain.usecases.schedule

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

fun getEvents(
    events: List<CalendarEvent>,
    date: LocalDate
): List<CalendarEvent> {   // функция для получения расписания на выбранный день
    // учитываем обычные пары, переносы занятий, дистант, консультации, зачеты, экзамены
    // обязательно проверяем, не попадает ли дата в даты-сключения
    // сначала проверить на совпадение даты. так мы исключим все события, кроме обычных пар
    // затем ищем по дню недели. берем события подходящего дня, учитывая номер, четность недели

    val dateEvents = mutableListOf<CalendarEvent>()
    // Дважды проходимся по событиям, чтобы случайно не добавить лишних
    for (event in events) {
        if (event.start.date == date && !checkExDate(event.exDates, date)) {
            dateEvents.add(event)
        }
    }
    if (dateEvents.isEmpty()) {
        for (event in events) {
            // Совпал день недели события и четность недель у обеих дат
            // А также проверили, что событие входит в обычное расписание (1-16 недели)
            if (event.start.dayOfWeek == date.dayOfWeek && getWeekNumber(event.start.date) <= 16
                && getWeekNumber(date) <= 16 && !checkExDate(event.exDates, date)
            ) {
                val dateParity = getParity(date)
                if (dateParity == event.parityOfWeek) {
                    dateEvents.add(event)
                }
            }
        }
    }

    return dateEvents
}

fun checkExDate(exDates: List<LocalDateTime>, date: LocalDate): Boolean {
    for (exDate in exDates) {
        if (exDate.date == date) {
            return true
        }
    }
    return false
}