package com.example.polyschedule.domain.entity

enum class WeekDay(val abbreviation: String, val position: Int) {
    FALSE_SATURDAY("", 0),
    MONDAY("пн", 1),
    TUESDAY("вт", 2),
    WEDNESDAY("ср", 3),
    THURSDAY("чт", 4),
    FRIDAY("пт", 5),
    SATURDAY("сб", 6),
    FALSE_MONDAY("", 7)
}