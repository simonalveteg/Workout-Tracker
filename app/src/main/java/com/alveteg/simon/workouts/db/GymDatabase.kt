package com.alveteg.simon.workouts.db

import androidx.room.AutoMigration
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
    AutoMigration(from = 4, to = 5),
  ],
  version = 5,
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

    val MIGRATION_3_4 = object : Migration(3, 4) {
      override fun migrate(db: SupportSQLiteDatabase) {
        // STEP 1: Clean up the orphaned rows BEFORE applying new constraints.
        db.execSQL("""
            DELETE FROM sessionExercises 
            WHERE parentSessionId NOT IN (SELECT sessionId FROM sessions)
        """)

        // Create a new temporary table that matches the desired final structure
        db.execSQL("""
            CREATE TABLE sessionExercises_new (
                sessionExerciseId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                parentSessionId INTEGER NOT NULL, 
                parentExerciseId INTEGER NOT NULL, 
                comment TEXT, 
                FOREIGN KEY(parentSessionId) REFERENCES sessions(sessionId) ON UPDATE NO ACTION ON DELETE CASCADE
            )
        """)

        // Copy the (now clean) data from the old table to the new one
        db.execSQL("""
            INSERT INTO sessionExercises_new (sessionExerciseId, parentSessionId, parentExerciseId, comment)
            SELECT sessionExerciseId, parentSessionId, parentExerciseId, comment FROM sessionExercises
        """)

        // Drop the old table
        db.execSQL("DROP TABLE sessionExercises")

        // Rename the new table to the original name
        db.execSQL("ALTER TABLE sessionExercises_new RENAME TO sessionExercises")

        // Re-create the indices on the new table
        db.execSQL("CREATE INDEX index_sessionExercises_parentSessionId ON sessionExercises (parentSessionId)")
        db.execSQL("CREATE INDEX index_sessionExercises_parentExerciseId ON sessionExercises (parentExerciseId)")
      }
    }
  }
}
