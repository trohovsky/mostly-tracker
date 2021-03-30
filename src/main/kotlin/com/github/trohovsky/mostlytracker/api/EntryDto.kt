package com.github.trohovsky.mostlytracker.api

import java.time.LocalDate

data class EntryDto(
    val id: Int,
    val entryDate: LocalDate,
    val timeSpent: Float,
    val description: String? = null,
)
