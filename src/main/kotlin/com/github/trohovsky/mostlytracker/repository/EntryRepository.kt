package com.github.trohovsky.mostlytracker.repository

import com.github.trohovsky.mostlytracker.entity.Entry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface EntryRepository : JpaRepository<Entry, Int> {

    fun findByProjectId(projectId: Int): List<Entry>

    @Query(
        """select coalesce(sum(e.timeSpent), 0)
            from Entry e
            where e.projectId = :projectId and e.entryDate = :entryDate"""
    )
    fun getTimeSpentPerProjectPerDay(projectId: Int, entryDate: LocalDate): Float

    @Query(
        """select e from Entry e
            where e.projectId = :projectId and (e.entryDate < :startDate or e.entryDate > :endDate)"""
    )
    fun findByProjectIdAndEntryDateOutsideOfDateRange(projectId: Int, startDate: LocalDate, endDate: LocalDate?):
            List<Entry>

    @Query(
        """select coalesce(sum(e.timeSpent), 0)
            from Entry e
            where e.projectId = :projectId"""
    )
    fun getTotalTimeSpent(projectId: Int): Float

    @Query(
        """select coalesce(count(distinct e.entryDate), 0)
            from Entry e
            where e.projectId = :projectId"""
    )
    fun getTotalDays(projectId: Int): Int
}
