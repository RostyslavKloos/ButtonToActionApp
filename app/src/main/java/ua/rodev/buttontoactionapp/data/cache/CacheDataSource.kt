package ua.rodev.buttontoactionapp.data.cache

import android.content.Context
import com.google.gson.Gson
import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.data.FetchActions
import ua.rodev.buttontoactionapp.data.cloud.ActionCloud
import ua.rodev.buttontoactionapp.data.cloud.MockActions
import ua.rodev.buttontoactionapp.domain.ReadRawResource
import java.io.*

// TODO: remove needless code
interface CacheDataSource : FetchActions {

    fun saveActions(actions: List<ActionCloud>)

    class Main(
        private val readRawResource: ReadRawResource,
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

        override suspend fun fetchActions(): List<ActionCloud> {
            val file = File(directory, CHILD)
            val fileReader = FileReader(file)
            val bufferedReader = BufferedReader(fileReader)
            val data = bufferedReader.readLine()
            bufferedReader.close()
            return gson.fromJson(data, MockActions::class.java)
//            val data = readRawResource.read(questionsId)
//            return gson.fromJson(data, MockActions::class.java)
        }

        companion object {
            private const val CHILD = "actions.json"
            private const val questionsId = R.raw.mock
        }
    }
}
