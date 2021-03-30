package com.github.trohovsky.mostlytracker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MostlyTrackerApplication

fun main(args: Array<String>) {
    runApplication<MostlyTrackerApplication>(*args)
}
