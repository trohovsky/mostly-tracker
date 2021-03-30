package com.github.trohovsky.mostlytracker.api

import java.time.LocalDate

data class CreateEntryDto(
    override val entryDate: LocalDate,
    override val timeSpent: Float,
    override val description: String? = null,
) : EntryBaseDto()
