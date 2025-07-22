package org.example.bigdisplayproject.domain.usecases.schedule

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import java.io.FileOutputStream
import java.time.temporal.WeekFields

data class CalendarEvent(
    val summary: String,          // Название предмета
    val description: String,      // ФИО преподавателя
    val location: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val parityOfWeek: Int = 1, // 0 - четная, 1 - нечетная,
    val exDates: List<LocalDateTime>  // Список дат-исключений (в эти дни события не будет)
)

class CalendarParser {
    val events = mutableListOf<CalendarEvent>()

    fun parseCalendar(content: String) {
        // Разбиваем содержимое на события
        val eventBlocks = content.split("BEGIN:VEVENT")
            .drop(1) // Первый элемент - не событие
            .map { it.substringBefore("END:VEVENT") }

        for (block in eventBlocks) {
            if (block.contains("TRANSPARENT")) continue    // Пропускаем "пустые" события

            // Извлекаем свойства события
            var description = extractProperty(block, "DESCRIPTION")
                ?.replace("\\n", " ") ?: ""
            if (description != "" && description.contains("Группы")) description = description.substring(0, description.indexOf("Группы"))
            val location = extractProperty(block, "LOCATION") ?: ""
            val summary = extractProperty(block, "SUMMARY") ?: ""

            val exDates = parseExDates(block)

            // Обрабатываем даты
            val startDate = parseDate(block, "DTSTART")
            val endDate = parseDate(block, "DTEND")

            // Эту функцию нужно редактировать каждый семестр
            val parity = getParity(startDate?.date ?: LocalDate(2025, 9, 1))

            if (startDate != null && endDate != null) {  // Добавляем событие в список
                events.add(CalendarEvent(summary, description, location, startDate, endDate, parity, exDates))
            }

            FileOutputStream("ScheduleText2.txt", true).bufferedWriter().use { writer ->
                writer.write("$summary ${startDate?.time} ${endDate?.time} ${startDate?.dayOfWeek}\n")
            }
        }
    }

    private fun extractProperty(block: String, propertyName: String): String? {
        val pattern = "$propertyName[^:]*:((?:[^\\r\\n]*(?:\\r?\\n[ \\t][^\\r\\n]*)*))"
        return Regex(pattern).find(block)?.groupValues?.get(1)
            ?.replace("\r\n", "\n")
            ?.replace(Regex("""\n[ \t]+"""), "") // Удаляем переносы с пробелами
            ?.trim() // Убираем пробелы в начале/конце
    }

    private fun parseDate(
        block: String,
        propertyName: String
    ): LocalDateTime? {
        try {
            val pattern = "$propertyName[^:]*:(\\d{8}(T\\d{6})?)"
            val match = Regex(pattern).find(block) ?: return null
            val dateStr = match.groupValues[1]

            if (dateStr.length == 15 && dateStr[8] == 'T') {   // yyyyMMdd'T'hhMMss
                val year = dateStr.substring(0, 4).toInt()
                val month = dateStr.substring(4, 6).toInt()
                val day = dateStr.substring(6, 8).toInt()
                val hour = dateStr.substring(9, 11).toInt()
                val minute = dateStr.substring(11, 13).toInt()
                val second = dateStr.substring(13, 15).toInt()

                return LocalDateTime(year, month, day, hour, minute, second)
            } else return null
        } catch (e: Exception) {
            return null
        }
    }

    private fun parseExDates(
        block: String
    ): List<LocalDateTime> {
        try {
            val cleanedBlock = block.replace(Regex("\\r?\\n[ \\t]+"), "")

            val pattern = "EXDATE(?:;[^:]*)?:([^\\n\\r]*)"
            val match = Regex(pattern).find(cleanedBlock) ?: return emptyList()
            val exdates = match.groupValues[1]

            return exdates.split(',')
                .filter { it.isNotBlank() }
                .mapNotNull { dateStr ->
                    try {
                        val year = dateStr.substring(0, 4).toInt()
                        val month = dateStr.substring(4, 6).toInt()
                        val day = dateStr.substring(6, 8).toInt()
                        val hour = dateStr.substring(9, 11).toInt()
                        val minute = dateStr.substring(11, 13).toInt()
                        val second = dateStr.substring(13, 15).toInt()

                        LocalDateTime(year, month, day, hour, minute, second)
                    } catch (e: Exception) {
                        println("LocalDateTime parsing error")
                        null
                    }
                }
        } catch (e: Exception) {
            return emptyList()
        }
    }

}

// вычисление четности недели по дате относительно даты первой недели семестра
fun getParity(date: LocalDate): Int {
    // TODO: Раскомментить для 1 семестра 2025-2026 учебного года
    //val firstDate = java.time.LocalDate.of(2025, 9, 1)
    val firstDate = java.time.LocalDate.of(2025, 2, 10)

    val currentDate = java.time.LocalDate.of(date.year, date.monthNumber, date.dayOfMonth)
    // Вычисляем четность недели: вычитаем из номера текущей недели номер первой недели семестра и находим остаток
    val parity = (currentDate.get(WeekFields.ISO.weekOfYear()) - firstDate.get(WeekFields.ISO.weekOfYear())) % 2
    return parity
}

// вычисление номера недели в семестре
fun getWeekNumber(date: LocalDate): Int {
    // TODO: Раскомментить для 1 семестра 2025-2026 учебного года
    //val firstDate = java.time.LocalDate.of(2025, 9, 1)
    val firstDate = java.time.LocalDate.of(2025, 2, 10)

    val currentDate = java.time.LocalDate.of(date.year, date.monthNumber, date.dayOfMonth)
    return currentDate.get(WeekFields.ISO.weekOfYear()) - firstDate.get(WeekFields.ISO.weekOfYear())
}