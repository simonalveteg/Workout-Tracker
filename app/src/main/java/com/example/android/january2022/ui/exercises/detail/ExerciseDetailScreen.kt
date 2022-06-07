package com.example.android.january2022.ui.exercises.detail

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.ui.exercises.ExerciseViewModel
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun ExerciseDetailScreen(
    viewModel: ExerciseDetailViewModel = hiltViewModel()
) {
    val url = "https://duckduckgo.com/?q=! exrx " + viewModel.currentExercise.title
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()
            loadUrl(url)
        }
    }, update = {
        it.loadUrl(url)
    }, modifier = Modifier.statusBarsPadding())
}
