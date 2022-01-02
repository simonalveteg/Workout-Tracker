package com.example.android.january2022.db

import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.android.january2022.db.entities.*


@Dao
interface GymDatabaseDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSession(session: Session) : Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExercise(exercise: Exercise)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSessionExercise(item: SessionExercise)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSet(item: GymSet)

    @Query("SELECT * FROM exercises ORDER BY exerciseId DESC LIMIT 1")
    fun getLastExercise() : Exercise

    @Query("DELETE FROM sessions")
    suspend fun clearSessions()

    @Query("SELECT * FROM sessions WHERE sessionId = :key")
    fun getSession(key: Long) : Session

    @Query("SELECT * FROM sessions ORDER BY sessionId DESC")
    fun getAllSessions(): LiveData<List<Session>>

    @Query("SELECT * FROM exercises ORDER BY exerciseId DESC")
    fun getAllExercises(): LiveData<List<Exercise>>

    @Query("SELECT * FROM sets")
    fun getAllSets(): LiveData<List<GymSet>>


    /**
     * Returns a list of ALL SessionExerciseWithExercise objects
     */
    @Transaction
    @Query("SELECT * FROM sessionExercises JOIN exercises ON sessionExercises.parentExerciseId = exercises.exerciseId")
    fun getSessionExercisesWithExercise() : LiveData<List<SessionExerciseWithExercise>>

    /**
     * Returns a list of SessionExerciseWithExercise for the given Session
     */
    @Transaction
    @Query("SELECT * FROM sessionExercises JOIN exercises ON sessionExercises.parentExerciseId = exercises.exerciseId WHERE parentSessionId = :key")
    fun getSessionExercisesWithExerciseForSession(key: Long) : List<SessionExerciseWithExercise>

}

