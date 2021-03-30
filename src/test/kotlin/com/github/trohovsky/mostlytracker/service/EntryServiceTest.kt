package com.github.trohovsky.mostlytracker.service

import com.github.trohovsky.mostlytracker.entity.Entry
import com.github.trohovsky.mostlytracker.entity.Project
import com.github.trohovsky.mostlytracker.repository.EntryRepository
import com.github.trohovsky.mostlytracker.repository.ProjectRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDate
import javax.persistence.EntityNotFoundException

private const val PROJECT_ID = 0
private const val ENTRY_ID = 0
val DATE: LocalDate = LocalDate.of(2021, 1, 1)
private val project = Project(PROJECT_ID, "name", DATE)
private val entry = Entry(ENTRY_ID, PROJECT_ID, DATE, 1f)

internal class EntryServiceTest {

    private val projectRepository: ProjectRepository = mockk()
    private val entryRepository: EntryRepository = mockk()
    private val entryService = EntryService(projectRepository, entryRepository)

    @Test
    fun `getAll returns all entries`() {
        every { projectRepository.findByIdOrNull(PROJECT_ID) } returns project
        every { entryRepository.findByProjectId(PROJECT_ID) } returns listOf(entry)

        val entries = entryService.getAll(PROJECT_ID)

        assertThat(entries).containsExactly(entry)
    }

    @Test
    fun `getAll throws EntityNotFoundException if a project does not exist`() {
        every { projectRepository.findByIdOrNull(PROJECT_ID) } returns null

        assertThrows<EntityNotFoundException> { entryService.getAll(PROJECT_ID) }
    }

    @Test
    fun `create returns a saved entry`() {
        every { projectRepository.findByIdOrNull(PROJECT_ID) } returns project
        every { entryRepository.getTimeSpentPerProjectPerDay(PROJECT_ID, DATE) } returns 0f
        every { entryRepository.save(entry) } returns entry

        val savedEntry = entryService.create(entry)

        assertThat(savedEntry).isEqualTo(entry)
    }

    @Test
    fun `create throws IllegalArgumentException if entry's date is outside of the range`() {
        every { projectRepository.findByIdOrNull(PROJECT_ID) } returns project

        assertThrows<IllegalArgumentException> { entryService.create(entry.copy(entryDate = LocalDate.of(1970, 1, 1))) }
    }

    @Test
    fun `create throws IllegalArgumentException if entry's timeSpent is negative`() {
        every { projectRepository.findByIdOrNull(PROJECT_ID) } returns project

        assertThrows<IllegalArgumentException> { entryService.create(entry.copy(timeSpent = -1f)) }
    }

    @Test
    fun `create throws IllegalArgumentException if entry's timeSpent exceed maximum hours per day`() {
        every { projectRepository.findByIdOrNull(PROJECT_ID) } returns project
        every { entryRepository.getTimeSpentPerProjectPerDay(PROJECT_ID, DATE) } returns MAXIMUM_HOURS_PER_DAY

        assertThrows<IllegalArgumentException> { entryService.create(entry) }
    }

    @Test
    fun `delete calls projectRepository#deleteById`() {
        every { projectRepository.findByIdOrNull(PROJECT_ID) } returns project
        every { entryRepository.deleteById(ENTRY_ID) } returns Unit

        entryService.delete(PROJECT_ID, ENTRY_ID)

        verify { entryRepository.deleteById(ENTRY_ID) }
    }

    @Test
    fun `delete throws EntityNotFoundException if a project does not exist`() {
        every { projectRepository.findByIdOrNull(PROJECT_ID) } returns null

        assertThrows<EntityNotFoundException> { entryService.delete(PROJECT_ID, ENTRY_ID) }
    }
}
