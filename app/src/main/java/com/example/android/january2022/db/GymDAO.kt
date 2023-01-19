package com.example.android.january2022.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExerciseWithExercise
import kotlinx.coroutines.flow.Flow


@Dao
interface GymDAO {

  @Query("SELECT * FROM sessions")
  fun getAllSessions(): Flow<List<Session>>

  @Query("SELECT * FROM sessionExercises JOIN exercises ON sessionExercises.parentExerciseId = exercises.id WHERE parentSessionId = :sessionId")
  fun getExercisesForSession(sessionId: Long): Flow<List<SessionExerciseWithExercise>>

  @Query("SELECT * FROM sets WHERE parentSessionExerciseId = :id ORDER BY setId ASC")
  fun getSetsForExercise(id: Long): Flow<List<GymSet>>

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertExercise(exercise: Exercise): Long
}

