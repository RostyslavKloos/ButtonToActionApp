package ua.rodev.buttontoactionapp.core

interface Log {

    fun print(message: String)

    class Main(private val tag: String = "RORO") : Log {
        override fun print(message: String) {
            android.util.Log.e(tag, message)
        }
    }
}
