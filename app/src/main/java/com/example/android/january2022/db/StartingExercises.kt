package com.example.android.january2022.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.android.january2022.R
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.di.ApplicationScope
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import javax.inject.Inject
import javax.inject.Provider

class StartingExercises @Inject constructor(
    private val repository: Provider<GymRepository>,
    @ApplicationScope private val applicationScope: CoroutineScope,
    @ApplicationContext private val context: Context
) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch {
            fillWithStartingExercises(context, repository.get())
        }
    }

    private suspend fun fillWithStartingExercises(
        context: Context,
        repository: GymRepository
    ) {
        Log.d("StartingExercises","Starting process")
        try {
            val notes = loadJSONArray(context)
            for (i in 0 until notes.length()) {
                val item = notes.getJSONObject(i)
                val title = item.getString("title")
                val force = item.getString("force")
                var equipment = item.getString("equip")
                val gson = GsonBuilder().create()
                val targets =
                    gson.fromJson(item.getString("targets"), Array<String>::class.java).toList()
                val synergists =
                    gson.fromJson(item.getString("synergists"), Array<String>::class.java).toList()

                val exercise = Exercise(
                    title = title,
                    force = force,
                    equipment = equipment,
                    targets = targets,
                    synergists = synergists,
                )
                repository.insertExercise(exercise)
                Log.d("StartingExercises","added exercise $exercise")
            }
        } catch (e: JSONException) {
            Log.d("StartingExercises", "fillWithStartingExercises: $e")
        }
    }

    private fun loadJSONArray(context: Context): JSONArray {

        Log.d("StartingExercises","loading JSON array")
        val inputStream = context.resources.openRawResource(R.raw.unique_exercises)

        BufferedReader(inputStream.reader()).use {
            return JSONArray(it.readText())
        }
    }
}