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

    @Delete
    suspend fun removeSession(item: Session)

    @Update
    suspend fun updateSet(item: GymSet)

    @Update
    suspend fun updateSession(session: Session)

    @Query("SELECT * FROM sets ORDER BY setId DESC LIMIT 1")
    fun getLastSet(): GymSet

    @Query("SELECT * FROM sets WHERE parentSessionExerciseId = :key ORDER BY setId DESC")
    fun getSetsForSessionExercise(key: Long): LiveData<List<GymSet>>

    @Query("SELECT * FROM sets JOIN sessionExercises ON sessionExerciseId=parentSessionExerciseId WHERE parentSessionId = :key ORDER BY setId ASC")
    fun getSetsForSession(key: Long): LiveData<List<GymSet>>

    @Query("SELECT * FROM exercises ORDER BY id DESC LIMIT 1")
    fun getLastExercise(): Exercise

    @Query("SELECT * FROM exercises WHERE id = :key")
    fun getExercise(key: Long): Exercise

    @Query("SELECT * FROM sessionExercises ORDER BY sessionExerciseId DESC LIMIT 1")
    fun getLastSessionExercise(): SessionExercise

    @Query("DELETE FROM sessions")
    suspend fun clearSessions()

    @Query("SELECT * FROM sessions WHERE sessionId = :key")
    fun getSession(key: Long): Session

    @Query("SELECT DISTINCT targets FROM exercises ORDER BY targets ASC")
    fun getAllMuscleGroups(): LiveData<List<String>>

    @Query("SELECT * FROM sessions ORDER BY start DESC")
    fun getAllSessions(): LiveData<List<Session>>

    @Query("SELECT * FROM exercises ORDER BY id DESC")
    fun getAllExercises(): LiveData<List<Exercise>>

    @Query("SELECT * FROM sets")
    fun getAllSets(): LiveData<List<GymSet>>

    @Query(
        "SELECT targets FROM exercises AS e " +
                "JOIN sessionExercises AS se ON e.id = se.parentExerciseId " +
                "JOIN sessions AS s ON s.sessionId = se.parentSessionId " +
                "WHERE s.sessionId = :key"
    )
    fun getSessionMuscleGroups(key: Long): List<String>

    /**
     * Returns a list of ALL SessionExerciseWithExercise objects
     */
    @Transaction
    @Query("SELECT * FROM sessionExercises JOIN exercises ON sessionExercises.parentExerciseId = exercises.id")
    fun getSessionExercisesWithExercise(): LiveData<List<SessionExerciseWithExercise>>

    /**
     * Returns a list of SessionExerciseWithExercise for the given Session
     */
    @Transaction
    @Query("SELECT * FROM sessionExercises JOIN exercises ON sessionExercises.parentExerciseId = exercises.id WHERE parentSessionId = :key")
    fun getSessionExercisesWithExerciseForSession(key: Long): LiveData<List<SessionExerciseWithExercise>>

    @Query(
        "SELECT * FROM exercises " +
                "WHERE equipment LIKE :equipment " +
                "AND title LIKE :query " +
                "ORDER BY title ASC"
    )
    fun getExercisesByQuery(equipment: String, query: String): Flow<List<Exercise>>

}

