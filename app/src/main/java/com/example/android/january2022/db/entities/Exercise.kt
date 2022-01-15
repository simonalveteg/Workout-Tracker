package com.example.android.january2022.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.january2022.db.Equipment
import com.example.android.january2022.db.MuscleGroup


@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    var exerciseId: Long = 0L,

    @ColumnInfo(name = "title")
    var exerciseTitle: String = "Exercise",
    @ColumnInfo(defaultValue = MuscleGroup.NULL)
    var muscleGroup: String,
    @ColumnInfo(defaultValue = Equipment.NULL)
    var equipment: String
){
    override fun toString(): String {
        return "[$exerciseId] $exerciseTitle"
    }
}