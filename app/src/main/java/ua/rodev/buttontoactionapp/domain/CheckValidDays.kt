package ua.rodev.buttontoactionapp.domain

interface CheckValidDays {
    fun isValid(days: List<Int>): Boolean
}
