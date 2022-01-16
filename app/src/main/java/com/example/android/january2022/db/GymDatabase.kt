package com.example.android.january2022.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExercise
import androidx.sqlite.db.SupportSQLiteDatabase

import androidx.room.migration.Migration
import androidx.annotation.NonNull

import androidx.room.DeleteColumn

import androidx.room.RenameColumn

import androidx.room.RenameTable

import androidx.room.DeleteTable





@Database(
    entities = [
        Session::class,
        Exercise::class,
        SessionExercise::class,
        GymSet::class
    ],
    autoMigrations = [
        AutoMigration(
            from = 8,
            to = 9,
            spec = GymDatabase.MyExampleAutoMigration::class
        )
    ],
    version = 9, exportSchema = true
)
abstract class GymDatabase : RoomDatabase() {

    /**
     * Connects the database to the DAO.
     */
    abstract val dao: GymDAO

    @RenameColumn(tableName = "exercises", fromColumnName = "muscleGroup", toColumnName = "muscleGroups")
    internal class MyExampleAutoMigration : AutoMigrationSpec {
        override fun onPostMigrate(db: SupportSQLiteDatabase) {
            // Invoked once auto migration is done
        }
    }
}
