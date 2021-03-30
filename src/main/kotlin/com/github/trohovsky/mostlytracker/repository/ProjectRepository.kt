package com.github.trohovsky.mostlytracker.repository

import com.github.trohovsky.mostlytracker.entity.Project
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import javax.persistence.EntityNotFoundException

interface ProjectRepository : JpaRepository<Project, Int>

fun ProjectRepository.findByIdOrThrow(id: Int): Project =
    findByIdOrNull(id) ?: throw EntityNotFoundException("Project with id $id could not be found.")
