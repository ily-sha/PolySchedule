package com.example.polyschedule.domain.entity

enum class Course(val nameOfCourse: String, val numberOfCourse: Int, var enable: Boolean) {
    FIRST("1 курс", 1, false),
    SECOND("2 курс", 2, false),
    THIRD("3 курс", 3,false),
    FOURTH("4 курс", 4,false),
    FIFTH("5 курс", 5, false);
}