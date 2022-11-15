package ua.rodev.buttontoactionapp.domain

interface HandleError<T> {

    fun handle(e: Exception): T

}
