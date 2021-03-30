package com.github.trohovsky.mostlytracker.service

import com.github.trohovsky.mostlytracker.entity.Project
import com.github.trohovsky.mostlytracker.entity.ProjectSummary
import com.github.trohovsky.mostlytracker.repository.EntryRepository
import com.github.trohovsky.mostlytracker.repository.ProjectRepository
import com.github.trohovsky.mostlytracker.repository.findByIdOrThrow
import org.springframework.stereotype.Service

@Service
class ProjectService(
    private val projectRepository: ProjectRepository,
    private val entryRepository: EntryRepository,
) {

    fun getAll(): List<Project> = projectRepository.findAll()

    fun getById(id: Int): Project = projectRepository.findByIdOrThrow(id)

    fun create(project: Project): Project = projectRepository.save(project)

    fun update(project: Project): Project {
        projectRepository.findByIdOrThrow(checkNotNull(project.id))
        validateEntryDatesInRange(project)
        return projectRepository.save(project)
    }

    private fun validateEntryDatesInRange(project: Project) {
        val entriesOutsideOfDateRange = entryRepository.findByProjectIdAndEntryDateOutsideOfDateRange(
            checkNotNull(project.id),
            project.startDate,
            project.endDate
        )
        if (entriesOutsideOfDateRange.isNotEmpty()) {
            throw IllegalArgumentException(
                "Cannot update the project due to these entries outside of the date range: $entriesOutsideOfDateRange."
            )
        }
    }

    fun delete(id: Int) = projectRepository.deleteById(id)

    fun getSummary(id: Int): ProjectSummary {
        projectRepository.findByIdOrThrow(id)
        val totalTimeSpent = entryRepository.getTotalTimeSpent(id)
        val totalDays = entryRepository.getTotalDays(id)
        val averageTimeSpentPerDay = if (totalDays != 0) totalTimeSpent / totalDays else 0f
        return ProjectSummary(totalTimeSpent, totalDays, averageTimeSpentPerDay)
    }
}
