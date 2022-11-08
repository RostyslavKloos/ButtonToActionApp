package ua.rodev.buttontoactionapp.domain

import android.content.Context
import androidx.annotation.RawRes

interface ReadRawResource {

    fun read(@RawRes id: Int): String

    class Main(private val context: Context) : ReadRawResource {
        override fun read(id: Int) =
            context.resources.openRawResource(id).bufferedReader().readText()
    }
}
