package com.example.android.january2022.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * A workout-session contains multiple SessionExercises. Has a start and end-time.
 */
@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey(autoGenerate = true)
    val sessionId: Long = 0L,
    val start: LocalDateTime = LocalDateTime.now(),
    val end: LocalDateTime? = null,
)
