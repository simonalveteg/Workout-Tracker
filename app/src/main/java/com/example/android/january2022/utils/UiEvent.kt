package com.example.android.january2022.utils

sealed class UiEvent {
    data class OpenWebsite(val url: String) : UiEvent()
    data class Navigate(val route: String, val popBackStack: Boolean = false): UiEvent()
    data class FileCreated(val fileName: String) : UiEvent()
}
