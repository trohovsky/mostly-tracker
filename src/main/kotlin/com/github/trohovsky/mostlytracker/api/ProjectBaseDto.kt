package com.github.trohovsky.mostlytracker.api

import java.time.LocalDate

abstract class ProjectBaseDto {
    abstract val name: String
    abstract val startDate: LocalDate
    abstract val endDate: LocalDate?
}
