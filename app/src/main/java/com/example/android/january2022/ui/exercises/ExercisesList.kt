package com.example.android.january2022.ui.exercises

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.android.january2022.ui.home.HomeViewModel
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.utils.Event


@Composable
fun ExercisesList(viewModel: HomeViewModel, onEvent: (Event) -> Unit, inPicker: Boolean) {
    val exercises: List<Exercise> by viewModel.exerciseList.observeAsState(listOf())
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(items = exercises) { exercise ->
            Box(Modifier.clickable {
                if (inPicker) {
                    onEvent(ExerciseEvent.ExerciseSelected(exercise))
                }
            }) {
                ExerciseCard(exercise)
            }
        }
    }
}