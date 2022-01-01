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

    @Query("DELETE FROM sessions")
    suspend fun clearSessions()

    @Query("SELECT * FROM sessions ORDER BY sessionId DESC")
    fun getAllSessions(): LiveData<List<Session>>

    @Query("SELECT * FROM exercises ORDER BY exerciseId DESC")
    fun getAllExercises(): LiveData<List<Exercise>>

    @Transaction
    @Query("SELECT * FROM sessionExercises JOIN exercises ON sessionExercises.parentExerciseId = exercises.exerciseId WHERE parentSessionId = :key")
    fun getSessionExercisesWithExercise(key: Long) : List<SessionExerciseWithExercise>

    @Transaction
    @Query("SELECT * FROM sessions ORDER BY sessionId DESC")
    fun getSessionsWithContents() : LiveData<List<SessionWithContents>>

}

