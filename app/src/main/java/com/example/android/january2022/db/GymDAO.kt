package com.example.android.january2022.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.Session
import kotlinx.coroutines.flow.Flow


@Dao
interface GymDAO {

  @Query("SELECT * FROM sessions")
  fun getAllSessions(): Flow<List<Session>>

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertExercise(exercise: Exercise): Long
}

