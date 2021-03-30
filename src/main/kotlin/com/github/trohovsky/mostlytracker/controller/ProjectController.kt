package com.github.trohovsky.mostlytracker.controller

import com.github.trohovsky.mostlytracker.api.CreateOrUpdateProjectDto
import com.github.trohovsky.mostlytracker.api.ProjectDto
import com.github.trohovsky.mostlytracker.api.ProjectSummaryDto
import com.github.trohovsky.mostlytracker.entity.Project
import com.github.trohovsky.mostlytracker.entity.ProjectSummary
import com.github.trohovsky.mostlytracker.service.ProjectService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/project")
class ProjectController(private val projectService: ProjectService) {

    @GetMapping
    fun getAll(): List<ProjectDto> = projectService.getAll().map(Project::toProjectDto)

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): ProjectDto = projectService.getById(id).toProjectDto()

    @PostMapping
    @ResponseStatus(CREATED)
    fun create(@RequestBody createProjectDto: CreateOrUpdateProjectDto): ProjectDto =
        projectService.create(createProjectDto.toProject()).toProjectDto()

    @PutMapping("/{id}")
    fun update(@PathVariable id: Int, @RequestBody updateProjectDto: CreateOrUpdateProjectDto): ProjectDto =
        projectService.update(updateProjectDto.toProject(id)).toProjectDto()

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    fun delete(@PathVariable id: Int): Unit = projectService.delete(id)

    @GetMapping("/{id}/summary")
    fun getSummary(@PathVariable id: Int): ProjectSummaryDto = projectService.getSummary(id).toProjectSummaryDto()
}

private fun Project.toProjectDto(): ProjectDto = ProjectDto(
    id = checkNotNull(id),
    name = name,
    startDate = startDate,
    endDate = endDate
)

private fun CreateOrUpdateProjectDto.toProject(id: Int? = null): Project = Project(
    id = id,
    name = name,
    startDate = startDate,
    endDate = endDate
)

private fun ProjectSummary.toProjectSummaryDto(): ProjectSummaryDto = ProjectSummaryDto(
    totalTimeSpent = totalTimeSpent,
    totalDays = totalDays,
    averageTimeSpentPerDay = averageTimeSpentPerDay
)
