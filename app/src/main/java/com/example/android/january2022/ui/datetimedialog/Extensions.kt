package com.example.android.january2022.ui.datetimedialog

import androidx.compose.ui.geometry.Offset
import java.time.LocalTime
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

internal fun Float.getOffset(angle: Double): Offset =
    Offset((this * cos(angle)).toFloat(), (this * sin(angle)).toFloat())

internal val LocalTime.isAM: Boolean
    get() = this.hour in 0..11

internal val LocalTime.simpleHour: Int
    get() {
        val tempHour = this.hour % 12
        return if (tempHour == 0) 12 else tempHour
    }

internal fun LocalTime.toAM(): LocalTime = if (this.isAM) this else this.minusHours(12)
internal fun LocalTime.toPM(): LocalTime = if (!this.isAM) this else this.plusHours(12)

internal fun LocalTime.noSeconds(): LocalTime = LocalTime.of(this.hour, this.minute)
