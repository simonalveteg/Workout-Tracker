package com.alveteg.simon.workouts.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
  version = 3,
  exportSchema = true
)
@TypeConverters(Converters::class)
abstract class GymDatabase : RoomDatabase() {

  /**
   * Connects the database to the DAO.
   */
  abstract val dao: GymDAO

  companion object {
    val MIGRATION_TO_RPE = object : Migration(2, 3) {
      override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE sets ADD COLUMN rpe INTEGER")

        db.execSQL("UPDATE sets SET rpe = 4 WHERE setType = 'Warmup'")
        db.execSQL("UPDATE sets SET rpe = 6 WHERE setType = 'Easy'")
        db.execSQL("UPDATE sets SET rpe = 8 WHERE setType = 'Normal'")
        db.execSQL("UPDATE sets SET rpe = 10 WHERE setType = 'Hard'")

        db.execSQL(
          """
            CREATE TABLE sets_new (
                setId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                parentSessionExerciseId INTEGER NOT NULL,
                reps INTEGER,
                weight REAL,
                time INTEGER,
                distance REAL,
                rpe INTEGER
            )
        """
        )
        db.execSQL("CREATE INDEX index_sets_parentSessionExerciseId_new ON sets_new (parentSessionExerciseId)")

        db.execSQL(
          """
            INSERT INTO sets_new (setId, parentSessionExerciseId, reps, weight, time, distance, rpe)
            SELECT setId, parentSessionExerciseId, reps, weight, time, distance, rpe FROM sets
        """
        )

        db.execSQL("DROP TABLE sets")
        db.execSQL("ALTER TABLE sets_new RENAME TO sets")
        db.execSQL("CREATE INDEX index_sets_parentSessionExerciseId ON sets (parentSessionExerciseId)")
      }
    }
  }
}
