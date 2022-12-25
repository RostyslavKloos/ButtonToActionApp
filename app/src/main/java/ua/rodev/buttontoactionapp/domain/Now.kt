package ua.rodev.buttontoactionapp.domain

interface Now {
    fun time(): Long

    class Main : Now {
        override fun time(): Long = System.currentTimeMillis()
    }
}
