package com.example.android.january2022.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    var exerciseId: Long = 0L,

    @ColumnInfo(name = "title")
    var exerciseTitle: String = "Exercise"
){
    override fun toString(): String {
        return "[$exerciseId] $exerciseTitle"
    }
}