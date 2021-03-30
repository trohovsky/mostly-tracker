package com.github.trohovsky.mostlytracker.api

import java.time.LocalDate

data class ProjectDto(
    val id: Int,
    override val name: String,
    override val startDate: LocalDate,
    override val endDate: LocalDate? = null,
) : ProjectBaseDto()
