package com.example.android.january2022.db

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.android.january2022.R
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.di.ApplicationScope
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.BufferedReader
import javax.inject.Inject
import javax.inject.Provider

class StartingExercises @Inject constructor(
    private val repository: Provider<GymRepository>,
    @ApplicationScope private val applicationScope: CoroutineScope,
    @ApplicationContext private val context: Context,
) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch {
            fillWithStartingExercises(context, repository.get())
        }
    }

    private suspend fun fillWithStartingExercises(
        context: Context,
        repository: GymRepository,
    ) {
        Timber.d("Starting process")
        try {
            val exercises = loadJSONArray(context)
            for (i in 0 until exercises.length()) {
                val item = exercises.getJSONObject(i)
                val title = item.getString("title")
                val type = item.getString("type")
                val force = item.parseToList("force")
                val equipment = item.parseToList("equipment")
                val targets = item.parseToList("targets")
                val synergists = item.parseToList("synergists")
                val stabilizers = item.parseToList("stabilizers")

                val exercise = Exercise(
                    title = title,
                    force = force,
                    type = type,
                    equipment = equipment,
                    targets = targets,
                    synergists = synergists,
                    stabilizers = stabilizers,
                )
                val result = repository.insertExercise(exercise)
                if (result == -1L) Timber.d("Exercise insertion failed for $exercise")
            }
        } catch (e: Exception) {
            Timber.d("fillWithStartingExercises: $e")
            Timber.d("Error caught")
        }
    }

    private fun JSONObject.parseToList(propertyName: String): List<String> {
        val gson = GsonBuilder().create()
        return try {
            Timber.d("Parsing $propertyName")
            val list = gson.fromJson(this.getString(propertyName), Array<String>::class.java).toList()
            Timber.d("List: $list")
            return list
        } catch (e: JSONException) {
            println("Error parsing $propertyName: ${e.message}")
            emptyList()
        }
    }

    private fun loadJSONArray(context: Context): JSONArray {
        Timber.d("loading JSON array")
        val inputStream = context.resources.openRawResource(R.raw.exercises)

        BufferedReader(inputStream.reader()).use {
            return JSONArray(it.readText())
        }
    }
}
