package org.example.bigdisplayproject.feature.display.domain.schedule

import kotlinx.datetime.LocalDate

fun getEvents(
    events: List<CalendarEvent>,
    date: LocalDate
): List<CalendarEvent> {   // функция для получения расписания на выбранный день
    // сначала проверить на совпадение даты. так мы исключим разовые события (переносы занятий, дистант, консультации, зачеты, экзамены)
    // затем ищем по дню недели. берем события подходящего дня, учитывая четность недели и периодичность пары

    val dateEvents = mutableListOf<CalendarEvent>()
    // Дважды проходимся по событиям, чтобы случайно не добавить лишних
    for (event in events) {
        if (event.start.date == date) {
            dateEvents.add(event)
        }
    }
    if (dateEvents.isEmpty()) {
        for (event in events) {
            if (event.start.dayOfWeek == date.dayOfWeek) {  // Совпал день недели события и четность недель у обеих дат
                val dateParity = getParity(date)
                if (dateParity == event.parityOfWeek) {
                    dateEvents.add(event)
                }
            }
        }
    }

    return dateEvents
}