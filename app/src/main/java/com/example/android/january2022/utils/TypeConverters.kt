package com.example.android.january2022.utils

import androidx.room.TypeConverter
import javax.xml.transform.Source

class Converters {

    @TypeConverter
    fun fromString(source: String): List<String> {
        return source.split("|")
    }

    @TypeConverter
    fun fromList(source: List<String>): String {
        return source.joinToString("|")
    }

}