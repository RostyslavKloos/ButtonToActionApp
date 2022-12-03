package ua.rodev.buttontoactionapp.data

import ua.rodev.buttontoactionapp.domain.CheckValidDays
import java.time.DayOfWeek
import java.time.LocalDate

class MainCheckValidDays : CheckValidDays {
    override fun isValid(days: List<Int>): Boolean {
        var valid = false
        val currentDay = LocalDate.now().dayOfWeek
        val weekDays = DayOfWeek.values()

        days.forEach {
            if (it in weekDays.first().value..weekDays.last().value) {
                if (currentDay == weekDays[it]) {
                    valid = true
                    return@forEach
                }
            }
        }
        return valid
    }
}
