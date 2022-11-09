package ua.rodev.buttontoactionapp.core

interface Mapper<S, R> {

    fun map(source: S): R

    interface Unit<T> : Mapper<T, kotlin.Unit>
}
