package com.github.trohovsky.mostlytracker.entity

import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Entry(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    val projectId: Int,
    val entryDate: LocalDate,
    val timeSpent: Float,
    val description: String? = null,
)
