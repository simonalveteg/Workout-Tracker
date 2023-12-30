package com.example.android.january2022.ui.datetimedialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
internal fun isSmallDevice(): Boolean {
    return LocalConfiguration.current.screenWidthDp <= 360
}

@Composable
internal fun isLargeDevice(): Boolean {
    return LocalConfiguration.current.screenWidthDp <= 600
}
