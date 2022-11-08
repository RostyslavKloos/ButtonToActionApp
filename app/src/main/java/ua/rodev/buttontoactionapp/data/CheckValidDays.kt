package ua.rodev.buttontoactionapp.data

import java.time.DayOfWeek
import java.time.LocalDate

interface CheckValidDays {

    fun check(days: List<Int>): Boolean

    class Main : CheckValidDays {
        override fun check(days: List<Int>): Boolean {
            var valid = false
            val currentDay = LocalDate.now().dayOfWeek
            val weekDays = DayOfWeek.values()

            days.forEach {
                if (it in 0..7) {
                    val givenDay = weekDays[it]
                    if (currentDay == givenDay) {
                        valid = true
                        return@forEach
                    }
                }
            }
            return valid
        }
    }
}
