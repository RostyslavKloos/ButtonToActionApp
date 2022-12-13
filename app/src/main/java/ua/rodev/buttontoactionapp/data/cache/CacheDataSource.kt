package ua.rodev.buttontoactionapp.data.cache

import android.content.Context
import com.google.gson.Gson
import okio.IOException
import ua.rodev.buttontoactionapp.data.FetchActions
import ua.rodev.buttontoactionapp.data.cloud.ActionCloud
import ua.rodev.buttontoactionapp.data.cloud.CloudActionsList
import java.io.*

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
            if (!file.exists()) {
                file.createNewFile()
            }
            BufferedWriter(FileWriter(file)).apply {
                write(actionsJson)
                close()
            }
        }

        @Throws(IOException::class)
        override suspend fun fetchActions(): List<ActionCloud> {
            val file = File(directory, CHILD)
            FileReader(file).use { fileReader ->
                BufferedReader(fileReader).use { bufferedReader ->
                    return gson.fromJson(bufferedReader.readLine(), CloudActionsList::class.java)
                }
            }
        }

        companion object {
            private const val CHILD = "actions.json"
        }
    }
}
