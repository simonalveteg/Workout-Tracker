package com.example.android.january2022.ui.home

import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.ui.session.SessionEvent
import com.example.android.january2022.utils.BottomBarScreen
import com.example.android.january2022.utils.Event


sealed class HomeEvent : Event {
    object OnUndoDeleteClick: HomeEvent()
    data class OnSessionClick(val session: Session): HomeEvent()
    object OnAddSessionClick: HomeEvent()
    data class SetSelectedSession(val session: Session): HomeEvent()
    data class OnDeleteSession(val session: Session): HomeEvent()
    object RestoreRemovedSession: HomeEvent()
}