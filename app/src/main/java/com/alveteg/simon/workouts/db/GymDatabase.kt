package com.alveteg.simon.workouts.db

import androidx.room.*
import com.alveteg.simon.workouts.db.entities.Exercise
import com.alveteg.simon.workouts.db.entities.GymSet
import com.alveteg.simon.workouts.db.entities.Session
import com.alveteg.simon.workouts.db.entities.SessionExercise
import com.alveteg.simon.workouts.utils.Converters


@Database(
    entities = [
        Session::class,
        Exercise::class,
        SessionExercise::class,
        GymSet::class
    ],
    autoMigrations = [
    ],
    version = 2, exportSchema = true
)
@TypeConverters(Converters::class)
abstract class GymDatabase : RoomDatabase() {

    /**
     * Connects the database to the DAO.
     */
    abstract val dao: GymDAO

}
