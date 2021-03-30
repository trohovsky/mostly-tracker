package com.github.trohovsky.mostlytracker.api

data class ProjectSummaryDto(
    val totalTimeSpent: Float,
    val totalDays: Int,
    val averageTimeSpentPerDay: Float,
)
