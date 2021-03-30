package com.github.trohovsky.mostlytracker.controller

import com.github.trohovsky.mostlytracker.api.CreateEntryDto
import com.github.trohovsky.mostlytracker.api.EntryDto
import com.github.trohovsky.mostlytracker.entity.Entry
import com.github.trohovsky.mostlytracker.service.EntryService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/project/{projectId}/entry")
class EntryController(private val entryService: EntryService) {

    @GetMapping
    fun getAll(@PathVariable projectId: Int): List<EntryDto> {
        return entryService.getAll(projectId).map(Entry::toEntryDto)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@PathVariable projectId: Int, @RequestBody createEntryDto: CreateEntryDto): EntryDto {
        return entryService.create(createEntryDto.toEntry(projectId)).toEntryDto()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable projectId: Int, @PathVariable id: Int) {
        entryService.delete(projectId, id)
    }
}

private fun Entry.toEntryDto(): EntryDto = EntryDto(
    id = checkNotNull(id),
    entryDate = entryDate,
    timeSpent = timeSpent,
    description = description
)

private fun CreateEntryDto.toEntry(projectId: Int): Entry = Entry(
    projectId = projectId,
    entryDate = entryDate,
    timeSpent = timeSpent,
    description = description
)
