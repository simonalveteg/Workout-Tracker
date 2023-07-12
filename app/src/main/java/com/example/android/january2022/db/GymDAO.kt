package com.example.android.january2022.db

import androidx.room.*
import com.example.android.january2022.db.entities.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


@Dao
interface GymDAO {

  @Query("SELECT * FROM sessions WHERE sessionId = :sessionId")
  fun getSessionById(sessionId: Long): Session

  @Query("SELECT * FROM sets ORDER BY setId ASC")
  fun getAllSets(): Flow<List<GymSet>>

  @Query("SELECT * FROM sessions ORDER BY start DESC")
  fun getAllSessions(): Flow<List<Session>>

  @Query("SELECT * FROM sessions ORDER BY sessionId DESC LIMIT 1")
  fun getLastSession(): Session

  @Query("SELECT * FROM exercises ORDER BY title ASC")
  fun getAllExercises(): Flow<List<Exercise>>

  @Query("SELECT * FROM sessionExercises join exercises ON sessionExercises.parentExerciseId = exercises.id")
  fun getAllSessionExercises(): Flow<List<SessionExerciseWithExercise>>

  @Query("SELECT * FROM sessionExercises JOIN exercises ON sessionExercises.parentExerciseId = exercises.id WHERE parentSessionId = :sessionId")
  fun getExercisesForSession(sessionId: Long): Flow<List<SessionExerciseWithExercise>>

  @Query("SELECT * FROM sets WHERE parentSessionExerciseId = :id ORDER BY setId ASC")
  fun getSetsForExercise(id: Long): Flow<List<GymSet>>

  @Query("SELECT GROUP_CONCAT(targets,'|') FROM exercises as e JOIN sessionExercises as se ON e.id = se.parentExerciseId  WHERE se.parentSessionId = :sessionId")
  fun getMuscleGroupsForSession(sessionId: Long): Flow<String>

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertSession(session: Session): Long

  @Delete
  suspend fun removeSession(session: Session)

  @Update
  suspend fun updateSession(session: Session)
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertExercise(exercise: Exercise): Long

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertSessionExercise(sessionExercise: SessionExercise): Long

  @Delete
  suspend fun removeSessionExercise(sessionExercise: SessionExercise)

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertSet(set: GymSet): Long

  @Update
  suspend fun updateSet(set: GymSet)

  @Delete
  suspend fun deleteSet(set: GymSet)

  @Query("SELECT * FROM sessions")
  fun getSessionList(): List<Session>

  @Query("SELECT * FROM exercises")
  fun getExerciseList(): List<Exercise>

  @Query("SELECT * FROM sessionExercises")
  fun getSessionExerciseList(): List<SessionExercise>

  @Query("SELECT * FROM sets")
  fun getSetList(): List<GymSet>

  @Query("DELETE FROM sessions")
  suspend fun clearSessions()

  @Query("DELETE FROM sessionExercises")
  suspend fun clearSessionExercises()

  @Query("DELETE FROM sets")
  suspend fun clearSets()

  @Query("DELETE FROM exercises")
  suspend fun clearExercises()
}

