package com.example.android.january2022.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExercise


@Database(
    entities = [
        Session::class,
        Exercise::class,
        SessionExercise::class,
        GymSet::class
    ],
    version = 6, exportSchema = false
)
abstract class GymDatabase : RoomDatabase() {

    /**
     * Connects the database to the DAO.
     */
    abstract val dao: GymDAO

}
