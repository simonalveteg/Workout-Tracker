package com.example.android.january2022.ui.home

import com.example.android.january2022.utils.Event

sealed class DrawerEvent : Event {
    object OnExercisesClicked: DrawerEvent()
    object OnHomeClicked: DrawerEvent()
    object OnSettingsClicked: DrawerEvent()
}
