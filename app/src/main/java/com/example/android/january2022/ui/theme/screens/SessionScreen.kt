package com.example.android.january2022.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.android.january2022.HomeViewModel
import com.example.android.january2022.db.entities.SessionExerciseWithExercise


@ExperimentalMaterialApi
@Composable
fun SessionScreen(homeViewModel: HomeViewModel, navController: NavController) {
    // TODO: implement backdropState
    val backdropState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)
    BackdropScaffold(
        appBar = { },
        backLayerContent = { },
        frontLayerContent = { SessionContent(homeViewModel, navController) },
        scaffoldState = backdropState,
        backLayerBackgroundColor = MaterialTheme.colors.background
    ) {
    }
}


@Composable
fun SessionContent(homeViewModel: HomeViewModel, navController: NavController) {
    val sessionExercises: List<SessionExerciseWithExercise> by homeViewModel.currentSessionExerciseList.observeAsState(
        listOf()
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        Button(
            onClick = {
                navController.navigate("exercisePicker")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "ADD EXERCISE")
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items = sessionExercises) { sessionExercise ->
                SessionExerciseCard(sessionExercise)
            }
        }
    }
}

@Composable
fun SessionExerciseCard(sessionExercise: SessionExerciseWithExercise) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp)
    ) {
        Row(modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)) {
            Text(text = "${sessionExercise.sessionExercise.sessionExerciseId}")
            Spacer(modifier = Modifier.weight(1f))
            Text(text = sessionExercise.exercise.exerciseTitle)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = sessionExercise.sessionExercise.parentSessionId.toString())
        }
    }
}

