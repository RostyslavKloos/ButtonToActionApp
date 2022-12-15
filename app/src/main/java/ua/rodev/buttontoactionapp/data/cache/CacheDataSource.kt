package ua.rodev.buttontoactionapp.data.cache

import android.content.Context
import com.google.gson.Gson
import ua.rodev.buttontoactionapp.data.FetchActions
import ua.rodev.buttontoactionapp.data.cloud.ActionCloud
import ua.rodev.buttontoactionapp.data.cloud.CloudActionsList
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

interface CacheDataSource : FetchActions {

    fun saveActions(actions: List<ActionCloud>)

    class Main(
        context: Context,
        private val gson: Gson,
    ) : CacheDataSource {

        private val path = context.getExternalFilesDir(null)
        private val directory = File(path!!.absolutePath)

        override fun saveActions(actions: List<ActionCloud>) {
            val actionsJson = gson.toJson(actions)
            directory.mkdirs()
            val file = File(directory, CHILD)
            if (!file.exists()) file.createNewFile()
            BufferedWriter(FileWriter(file)).use {
                it.write(actionsJson)
            }
        }

        override suspend fun fetchActions(): List<ActionCloud> {
            val file = File(directory, CHILD)
            return if (file.exists()) {
                val json = file.inputStream().bufferedReader().use(BufferedReader::readText)
                gson.fromJson(json, CloudActionsList::class.java)
            } else emptyList()
        }

        companion object {
            private const val CHILD = "actions.json"
        }
    }
}
