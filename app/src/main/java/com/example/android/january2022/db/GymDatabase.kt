package com.example.android.january2022.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExercise
import androidx.sqlite.db.SupportSQLiteDatabase

import androidx.room.migration.Migration


@Database(
    entities = [
        Session::class,
        Exercise::class,
        SessionExercise::class,
        GymSet::class
    ],
    autoMigrations = [
        AutoMigration(
            from = 6,
            to = 7
        )
    ],
    version = 7, exportSchema = true
)
abstract class GymDatabase : RoomDatabase() {

    /**
     * Connects the database to the DAO.
     */
    abstract val dao: GymDAO

}
