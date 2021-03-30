package com.github.trohovsky.mostlytracker.service

import com.github.trohovsky.mostlytracker.entity.Entry
import com.github.trohovsky.mostlytracker.entity.Project
import com.github.trohovsky.mostlytracker.repository.EntryRepository
import com.github.trohovsky.mostlytracker.repository.ProjectRepository
import com.github.trohovsky.mostlytracker.repository.findByIdOrThrow
import org.springframework.stereotype.Service

internal const val MAXIMUM_HOURS_PER_DAY = 10f

@Service
class EntryService(
    private val projectRepository: ProjectRepository,
    private val entryRepository: EntryRepository,
) {

    fun getAll(projectId: Int): List<Entry> {
        projectRepository.findByIdOrThrow(projectId)
        return entryRepository.findByProjectId(projectId)
    }

    fun create(entry: Entry): Entry {
        val project = projectRepository.findByIdOrThrow(entry.projectId)
        validateEntryDate(project, entry)
        validateTimeSpent(entry)
        return entryRepository.save(entry)
    }

    private fun validateEntryDate(project: Project, entry: Entry) {
        if (project.startDate.isAfter(entry.entryDate) || project.endDate?.isBefore(entry.entryDate) == true) {
            val message = "Entry date should be equal or after ${project.startDate}" + if (project.endDate != null) {
                "and equal or before ${project.endDate}."
            } else {
                "."
            }
            throw IllegalArgumentException(message)
        }
    }

    private fun validateTimeSpent(entry: Entry) {
        if (entry.timeSpent <= 0) {
            throw IllegalArgumentException("Entry time should be greater than 0.")
        }
        val timeSpentPerDay = entryRepository.getTimeSpentPerProjectPerDay(entry.projectId, entry.entryDate)
        if (timeSpentPerDay + entry.timeSpent > MAXIMUM_HOURS_PER_DAY) {
            throw IllegalArgumentException("Cannot track more than 10 hours per day.")
        }
    }

    fun delete(projectId: Int, id: Int) {
        projectRepository.findByIdOrThrow(projectId)
        entryRepository.deleteById(id)
    }
}
