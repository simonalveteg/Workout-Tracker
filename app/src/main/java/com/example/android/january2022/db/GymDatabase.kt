package com.example.android.january2022.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExercise
import com.example.android.january2022.utils.Converters

@Database(
    entities = [
        Session::class,
        Exercise::class,
        SessionExercise::class,
        GymSet::class,
    ],
    autoMigrations = [],
    version = 2,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class GymDatabase : RoomDatabase() {

    /**
     * Connects the database to the DAO.
     */
    abstract val dao: GymDAO
}
