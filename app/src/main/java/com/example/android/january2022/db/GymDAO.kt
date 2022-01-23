package com.example.android.january2022.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.january2022.db.entities.*
import kotlinx.coroutines.flow.Flow


@Dao
interface GymDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSession(session: Session): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExercise(exercise: Exercise)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSessionExercise(item: SessionExercise)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSet(item: GymSet)

    @Delete
    suspend fun removeSessionExercise(item: SessionExercise)

    @Delete
    suspend fun removeSet(item: GymSet)

    @Update
    suspend fun updateSet(item: GymSet)

    @Query("SELECT * FROM sets ORDER BY setId DESC LIMIT 1")
    fun getLastSet(): GymSet

    @Query("SELECT * FROM sets WHERE parentSessionExerciseId = :key ORDER BY setId DESC")
    fun getSetsForSessionExercise(key: Long): LiveData<List<GymSet>>

    @Query("SELECT * FROM sets JOIN sessionExercises ON sessionExerciseId=parentSessionExerciseId WHERE parentSessionId = :key ORDER BY setId ASC")
    fun getSetsForSession(key: Long): LiveData<List<GymSet>>

    @Query("SELECT * FROM exercises ORDER BY exerciseId DESC LIMIT 1")
    fun getLastExercise(): Exercise

    @Query("SELECT * FROM exercises WHERE exerciseId = :key")
    fun getExercise(key: Long): Exercise

    @Query("SELECT * FROM sessionExercises ORDER BY sessionExerciseId DESC LIMIT 1")
    fun getLastSessionExercise(): SessionExercise

    @Query("DELETE FROM sessions")
    suspend fun clearSessions()

    @Query("SELECT * FROM sessions WHERE sessionId = :key")
    fun getSession(key: Long): Session

    @Query("SELECT DISTINCT muscleGroups FROM exercises ORDER BY muscleGroups ASC")
    fun getAllMuscleGroups(): LiveData<List<String>>

    @Query("SELECT * FROM sessions ORDER BY start_time_milli DESC")
    fun getAllSessions(): LiveData<List<Session>>

    @Query("SELECT * FROM exercises ORDER BY exerciseId DESC")
    fun getAllExercises(): LiveData<List<Exercise>>

    @Query("SELECT * FROM sets")
    fun getAllSets(): LiveData<List<GymSet>>

    @Query(
        "SELECT muscleGroups FROM exercises AS e " +
                "JOIN sessionExercises AS se ON e.exerciseId = se.parentExerciseId " +
                "JOIN sessions AS s ON s.sessionId = se.parentSessionId " +
                "WHERE s.sessionId = :key"
    )
    fun getSessionMuscleGroups(key: Long): List<String>

    /**
     * Returns a list of ALL SessionExerciseWithExercise objects
     */
    @Transaction
    @Query("SELECT * FROM sessionExercises JOIN exercises ON sessionExercises.parentExerciseId = exercises.exerciseId")
    fun getSessionExercisesWithExercise(): LiveData<List<SessionExerciseWithExercise>>

    /**
     * Returns a list of SessionExerciseWithExercise for the given Session
     */
    @Transaction
    @Query("SELECT * FROM sessionExercises JOIN exercises ON sessionExercises.parentExerciseId = exercises.exerciseId WHERE parentSessionId = :key")
    fun getSessionExercisesWithExerciseForSession(key: Long): LiveData<List<SessionExerciseWithExercise>>

    @Query(
        "SELECT * FROM exercises WHERE muscleGroups = :string ORDER BY title ASC"
    )
    fun getExercisesByQuery(string: String): Flow<List<Exercise>>

}

