package ua.rodev.buttontoactionapp.core

import android.content.Context
import androidx.annotation.StringRes

interface ManageResources {

    fun string(@StringRes id: Int): String

    fun string(@StringRes id: Int, arg: String): String

    class Main(private val context: Context) : ManageResources {

        override fun string(id: Int): String = context.getString(id)

        override fun string(id: Int, arg: String): String = context.getString(id, arg)
    }
}
