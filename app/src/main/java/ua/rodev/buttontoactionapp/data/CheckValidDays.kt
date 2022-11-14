package ua.rodev.buttontoactionapp.data

import java.time.DayOfWeek
import java.time.LocalDate

interface CheckValidDays {

    fun isValid(days: List<Int>): Boolean

    class Main : CheckValidDays {
        override fun isValid(days: List<Int>): Boolean {
            var valid = false
            val currentDay = LocalDate.now().dayOfWeek
            val weekDays = DayOfWeek.values()

            days.forEach {
                if (it in 0..7) {
                    if (currentDay == weekDays[it]) {
                        valid = true
                        return@forEach
                    }
                }
            }
            return valid
        }
    }
}
