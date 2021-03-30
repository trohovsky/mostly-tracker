package com.github.trohovsky.mostlytracker.service

import com.github.trohovsky.mostlytracker.entity.Project
import com.github.trohovsky.mostlytracker.entity.ProjectSummary
import com.github.trohovsky.mostlytracker.repository.EntryRepository
import com.github.trohovsky.mostlytracker.repository.ProjectRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDate
import javax.persistence.EntityNotFoundException

private const val ID = 0
private val project = Project(ID, "name", LocalDate.of(2021, 1, 1))

private const val TOTAL_TIME_SPENT = 8f
private const val TOTAL_DAYS = 2

internal class ProjectServiceTest {

    private val projectRepository: ProjectRepository = mockk()
    private val entryRepository: EntryRepository = mockk()
    private val projectService = ProjectService(projectRepository, entryRepository)

    @Test
    fun `getAll returns all projects`() {
        every { projectRepository.findAll() } returns listOf(project)

        val projects = projectService.getAll()

        assertThat(projects).containsExactly(project)
    }

    @Test
    fun `getById returns a project`() {
        every { projectRepository.findByIdOrNull(ID) } returns project

        val retrievedProject = projectService.getById(ID)

        assertThat(retrievedProject).isEqualTo(project)
    }

    @Test
    fun `getById throws EntityNotFoundException if a project does not exist`() {
        every { projectRepository.findByIdOrNull(ID) } returns null

        assertThrows<EntityNotFoundException> { projectService.getById(ID) }
    }

    @Test
    fun `create returns a saved project`() {
        every { projectRepository.save(project) } returns project

        val savedProject = projectService.create(project)

        assertThat(savedProject).isEqualTo(project)
    }

    @Test
    fun `update returns a saved project`() {
        every { projectRepository.findByIdOrNull(ID) } returns project
        every {
            entryRepository.findByProjectIdAndEntryDateOutsideOfDateRange(
                checkNotNull(project.id),
                project.startDate,
                project.endDate
            )
        } returns emptyList()
        every { projectRepository.save(project) } returns project

        val updatedProject = projectService.update(project)

        assertThat(updatedProject).isEqualTo(project)
    }

    @Test
    fun `update throws EntityNotFoundExceptionif a project does not exist`() {
        every { projectRepository.findByIdOrNull(ID) } returns null

        assertThrows<EntityNotFoundException> { projectService.update(project) }
    }

    @Test
    fun `update throws IllegalArgumentException if entries outside of the date range exist `() {
        every { projectRepository.findByIdOrNull(ID) } returns project
        every {
            entryRepository.findByProjectIdAndEntryDateOutsideOfDateRange(
                checkNotNull(project.id),
                project.startDate,
                project.endDate
            )
        } returns listOf(any())
        every { projectRepository.save(project) } returns project

        assertThrows<IllegalArgumentException> { projectService.update(project) }
    }

    @Test
    fun `delete calls projectRepository#deleteById`() {
        every { projectRepository.deleteById(ID) } returns Unit

        projectService.delete(ID)

        verify { projectRepository.deleteById(ID) }
    }

    @Test
    fun `getSummary returns a project summary`() {
        every { projectRepository.findByIdOrNull(ID) } returns project
        every { entryRepository.getTotalTimeSpent(ID) } returns TOTAL_TIME_SPENT
        every { entryRepository.getTotalDays(ID) } returns TOTAL_DAYS

        val projectSummary = projectService.getSummary(ID)

        assertThat(projectSummary).isEqualTo(
            ProjectSummary(
                TOTAL_TIME_SPENT,
                TOTAL_DAYS,
                TOTAL_TIME_SPENT / TOTAL_DAYS
            )
        )
    }

    @Test
    fun `getSummary returns a project summary with zero values`() {
        every { projectRepository.findByIdOrNull(ID) } returns project
        every { entryRepository.getTotalTimeSpent(ID) } returns 0f
        every { entryRepository.getTotalDays(ID) } returns 0

        val projectSummary = projectService.getSummary(ID)

        assertThat(projectSummary).isEqualTo(ProjectSummary(0f, 0, 0f))
    }

    @Test
    fun `getSummary throws EntityNotFoundException if a project does not exist`() {
        every { projectRepository.findByIdOrNull(ID) } returns null

        assertThrows<EntityNotFoundException> { projectService.getSummary(ID) }
    }
}
