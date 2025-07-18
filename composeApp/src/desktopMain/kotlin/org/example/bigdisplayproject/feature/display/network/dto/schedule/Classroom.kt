package org.example.bigdisplayproject.feature.display.network.dto.schedule

import kotlinx.serialization.Serializable

@Serializable
data class Classroom(
    val className: String, // название пары
    //val classType: String, // лекция/практика
    val classCabinet: String, // где проходит пара, мб с учётом корпуса
    //val classTeacher: String, // ФИО преподавателя
    val classTime: String, // время проведения пары
    //val classTimeNumber: Int  // какая пара по счёту
)