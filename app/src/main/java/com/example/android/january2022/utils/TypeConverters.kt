package com.example.android.january2022.utils

import androidx.room.TypeConverter
import timber.log.Timber
import java.time.LocalDateTime
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

  @TypeConverter
  fun fromDateTime(source: LocalDateTime?): String {
    return source?.toString() ?: ""
  }

  @TypeConverter
  fun toDateTime(source: String?): LocalDateTime? {
    return try {
      LocalDateTime.parse(source)
    } catch (e: Exception) {
      null
    }
  }
}