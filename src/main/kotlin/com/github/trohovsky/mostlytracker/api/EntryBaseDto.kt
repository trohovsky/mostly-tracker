package com.github.trohovsky.mostlytracker.api

import java.time.LocalDate

abstract class EntryBaseDto {
    abstract val entryDate: LocalDate
    abstract val timeSpent: Float
    abstract val description: String?
}
