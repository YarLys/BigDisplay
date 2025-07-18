/*
package org.example.bigdisplayproject.feature.display.domain.schedule

import java.time.*
import java.time.format.DateTimeFormatter

// Модели данных
data class CalendarEvent1(
    val uid: String,
    val summary: String,          // Название предмета
    val description: String,      // ФИО преподавателя
    val categories: String,       // Тип занятия (ЛК, ПР, и т.д.)
    val location: String,
    val start: ZonedDateTime,
    val end: ZonedDateTime,
    val recurrenceRule: RecurrenceRule? = null,
    val exceptionDates: Set<ZonedDateTime> = emptySet(),
    val isAllDay: Boolean = false
)

data class RecurrenceRule(
    val frequency: Frequency,
    val interval: Int,
    val until: ZonedDateTime? = null
)

enum class Frequency { WEEKLY, DAILY, MONTHLY, YEARLY }

// Главный парсер
class ICalendarParser {

    fun parse(icalData: String): List<CalendarEvent> {
        val unfoldedData = unfoldLines(icalData)
        val events = mutableListOf<CalendarEvent>()
        var currentEvent: EventBuilder? = null
        var defaultTimeZone: ZoneId = ZoneId.systemDefault()

        unfoldedData.lineSequence().forEach { line ->
            when {
                line == "BEGIN:VTIMEZONE" -> {
                    defaultTimeZone = parseTimeZone(icalData) ?: defaultTimeZone
                }
                line == "BEGIN:VEVENT" -> {
                    currentEvent = EventBuilder(defaultTimeZone)
                }
                line == "END:VEVENT" -> {
                    currentEvent?.let { builder ->
                        events.add(builder.build())
                    }
                    currentEvent = null
                }
                else -> currentEvent?.parseLine(line)
            }
        }
        return events
    }

    private fun unfoldLines(icalData: String): String {
        return icalData.split("\n")
            .fold(StringBuilder()) { sb, line ->
                if (line.startsWith(" ")) {
                    sb.append(line.substring(1))
                } else {
                    if (sb.isNotEmpty()) sb.append("\n")
                    sb.append(line)
                }
                sb
            }.toString()
    }

    private fun parseTimeZone(icalData: String): ZoneId? {
        val tzIdRegex = "TZID:([\\w/]+)".toRegex()
        return tzIdRegex.find(icalData)?.groupValues?.get(1)
            ?.let { ZoneId.of(it) }
    }

    // Вспомогательный класс для построения событий
    private class EventBuilder(private val defaultZone: ZoneId) {
        private var uid: String = ""
        private var summary: String = ""
        private var description: String = ""
        private var categories: String = ""
        private var location: String = ""
        private var start: ZonedDateTime? = null
        private var end: ZonedDateTime? = null
        private var recurrenceRule: RecurrenceRule? = null
        private val exceptionDates = mutableSetOf<ZonedDateTime>()
        private var isAllDay: Boolean = false
        private var currentZone: ZoneId = defaultZone

        fun parseLine(line: String) {
            val (key, value) = splitKeyValue(line)
            when {
                key == "UID" -> uid = value
                key == "SUMMARY" -> summary = value
                key == "DESCRIPTION" -> description = cleanDescription(value)
                key == "CATEGORIES" -> categories = value
                key == "LOCATION" -> location = value
                key.startsWith("DTSTART") -> {
                    parseDateTime(key, value).also {
                        start = it
                        isAllDay = key.contains("VALUE=DATE")
                    }
                }
                key.startsWith("DTEND") -> {
                    end = parseDateTime(key, value)
                }
                key == "RRULE" -> recurrenceRule = parseRRule(value)
                key.startsWith("EXDATE") -> exceptionDates.addAll(parseExDates(value))
                key == "X-SCHEDULE_VERSION-ID" -> Unit // Игнорируем
            }
        }

        private fun cleanDescription(desc: String): String {
            return desc
                .replace("\\\\n", "\n") // Заменяем экранированные переносы
                .substringAfter("Преподаватель:")
                .trim()
        }

        private fun parseDateTime(key: String, value: String): ZonedDateTime {
            val isDateOnly = key.contains("VALUE=DATE")
            val zone = if (key.contains("TZID=")) {
                parseTimeZoneFromParam(key) ?: currentZone
            } else {
                currentZone
            }

            return if (isDateOnly) {
                LocalDate.parse(value, DateTimeFormatter.BASIC_ISO_DATE)
                    .atStartOfDay(zone)
            } else {
                val ldt = LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"))
                ZonedDateTime.of(ldt, zone)
            }
        }

        private fun parseRRule(rrule: String): RecurrenceRule {
            val params = rrule.split(";").associate {
                val parts = it.split("=")
                parts[0] to parts.getOrElse(1) { "" }
            }

            return RecurrenceRule(
                frequency = when (params["FREQ"]) {
                    "WEEKLY" -> Frequency.WEEKLY
                    "DAILY" -> Frequency.DAILY
                    else -> Frequency.WEEKLY
                },
                interval = params["INTERVAL"]?.toIntOrNull() ?: 1,
                until = params["UNTIL"]?.let {
                    ZonedDateTime.parse(it, DateTimeFormatter.ISO_ZONED_DATE_TIME)
                }
            )
        }

        private fun parseExDates(exdates: String): List<ZonedDateTime> {
            return exdates.split(",").map {
                val ldt = LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"))
                ZonedDateTime.of(ldt, currentZone)
            }
        }

        private fun parseTimeZoneFromParam(line: String): ZoneId? {
            return "TZID=([\\w/]+)".toRegex().find(line)?.groupValues?.get(1)
                ?.let { ZoneId.of(it) }
        }

        fun build(): CalendarEvent {
            require(start != null) { "Missing DTSTART for event" }
            require(end != null) { "Missing DTEND for event" }

            return CalendarEvent(
                uid = uid,
                summary = summary,
                description = description,
                categories = categories,
                location = location,
                start = start!!,
                end = end!!,
                recurrenceRule = recurrenceRule,
                exceptionDates = exceptionDates,
                isAllDay = isAllDay
            )
        }

        private fun splitKeyValue(line: String): Pair<String, String> {
            val parts = line.split(":", limit = 2)
            return parts[0] to parts.getOrElse(1) { "" }
        }
    }
}

// Генератор событий для дня
class EventOccurrenceGenerator {

    fun getEventsForDay(
        events: List<CalendarEvent>,
        date: LocalDate,
        timeZone: ZoneId = ZoneId.systemDefault()
    ): List<CalendarEvent> {

        val startOfDay = date.atStartOfDay(timeZone)
        val endOfDay = date.plusDays(1).atStartOfDay(timeZone)

        return events.flatMap { event ->
            if (event.recurrenceRule != null) {
                generateRecurringEvents(event, startOfDay, endOfDay, timeZone)
            } else {
                if (isEventOnDate(event, date, timeZone)) listOf(event) else emptyList()
            }
        }.sortedBy { it.start }
    }

    private fun isEventOnDate(event: CalendarEvent, date: LocalDate, zone: ZoneId): Boolean {
        val eventStart = event.start.withZoneSameInstant(zone).toLocalDate()
        return eventStart == date && !event.exceptionDates.any {
            it.withZoneSameInstant(zone).toLocalDate() == date
        }
    }

    private fun generateRecurringEvents(
        event: CalendarEvent,
        rangeStart: ZonedDateTime,
        rangeEnd: ZonedDateTime,
        displayZone: ZoneId
    ): List<CalendarEvent> {

        val occurrences = mutableListOf<CalendarEvent>()
        val rule = event.recurrenceRule!!
        val duration = Duration.between(event.start, event.end)

        // Обрабатываем исходное событие
        if (isInRange(event.start, rangeStart, rangeEnd)) {
            occurrences.add(event)
        }

        // Генерируем повторения
        var current = nextOccurrence(event.start, rule)
        var occurrenceCount = 0

        while (shouldContinue(current, rule, rangeEnd) && occurrenceCount < 1000) {
            if (!isException(current, event.exceptionDates) &&
                isInRange(current, rangeStart, rangeEnd)
            ) {
                occurrences.add(
                    event.copy(
                        start = current,
                        end = current.plus(duration)
                    )
                )
            }

            current = nextOccurrence(current, rule)
            occurrenceCount++
        }

        return occurrences
    }

    private fun nextOccurrence(current: ZonedDateTime, rule: RecurrenceRule): ZonedDateTime {
        return when (rule.frequency) {
            Frequency.WEEKLY -> current.plusWeeks(rule.interval.toLong())
            Frequency.DAILY -> current.plusDays(rule.interval.toLong())
            else -> current.plusWeeks(1)
        }
    }

    private fun isInRange(date: ZonedDateTime, start: ZonedDateTime, end: ZonedDateTime): Boolean {
        return date >= start && date < end
    }

    private fun isException(date: ZonedDateTime, exceptions: Set<ZonedDateTime>): Boolean {
        return exceptions.any { it.isEqual(date) }
    }

    private fun shouldContinue(current: ZonedDateTime, rule: RecurrenceRule, rangeEnd: ZonedDateTime): Boolean {
        return (rule.until == null || current <= rule.until) && current <= rangeEnd.plusYears(1)
    }
}*/
