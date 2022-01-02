package com.example.android.january2022.ui.theme.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.android.january2022.HomeViewModel
import com.example.android.january2022.R
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExerciseWithExercise
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@ExperimentalMaterialApi
@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavController) {
    val sessions by viewModel.sessionList.observeAsState(listOf())
    val composableScope = rememberCoroutineScope()
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            Box(
                modifier = Modifier
                    .height(220.dp)
                    .padding(vertical = 16.dp, horizontal = 16.dp)
            ) {
                DrawerMenu(viewModel, navController)
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    cutoutShape = RoundedCornerShape(50)
                ) {
                    IconButton(
                        onClick = {
                            composableScope.launch { bottomSheetState.show() }
                        }
                    ) {
                        Icon(Icons.Filled.Menu, "Menu")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            viewModel.populateDatabase()
                        }
                    ) {
                        Icon(Icons.Default.Search, "")
                    }
                    IconButton(
                        onClick = {
                            viewModel.clearDatabase()
                        }
                    ) {
                        Icon(Icons.Default.MoreVert, "")
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        viewModel.onNewSession()
                        //navController.navigate("session")
                    },
                    shape = RoundedCornerShape(50),
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Icon(Icons.Filled.Add, "")
                }
            },
            isFloatingActionButtonDocked = true,
            floatingActionButtonPosition = FabPosition.Center
        ) {
            SessionCardList(sessions = sessions, viewModel)
        }
    }
}

@Composable
fun DrawerMenu(viewModel: HomeViewModel, navController: NavController) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(Modifier.clickable {
            navController.navigate("home") {
                popUpTo("home") { inclusive = true }
            }
        }) {
            DrawerMenuItem("home")
        }
        Box(Modifier.clickable {
            navController.navigate("exercises")
        }) {
            DrawerMenuItem("exercises")
        }
    }
}

@Composable
fun DrawerMenuItem(text: String) {
    Text(text = text.uppercase(), style = MaterialTheme.typography.h5)

}


@Composable
fun SessionCardList(
    sessions: List<Session>,
    viewModel: HomeViewModel
) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = sessions) { session ->
            SessionCard(session, viewModel)
        }
    }
}

@Composable
fun SessionCard(
    session: Session,
    viewModel: HomeViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    val sessionContent by viewModel.sessionExerciseList.observeAsState(listOf())

    val startDate = SimpleDateFormat(
        "dd-MM-yy",
        Locale.ENGLISH
    ).format(session.startTimeMilli)
    val startTime = SimpleDateFormat(
        "HH:mm",
        Locale.ENGLISH
    ).format(session.startTimeMilli)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable {
                viewModel.onSessionClicked(session.sessionId)
            }
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    delayMillis = 50,
                    easing = LinearOutSlowInEasing
                )
            )
    ) {
        Column(
            Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
        ) {
            Row {
                Column {
                    Text(text = startDate)
                    Text(text = startTime)
                }
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = {
                        expanded = !expanded
                    }
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription =
                        if (expanded) {
                            stringResource(R.string.show_less)
                        } else {
                            stringResource(
                                R.string.show_more
                            )
                        }
                    )
                }
            }
            if (expanded) {
                Box(Modifier.padding(top = 12.dp, bottom = 8.dp, start = 0.dp, end = 0.dp)) {
                    Divider(color = MaterialTheme.colors.onSurface, thickness = 1.dp)
                }
                sessionContent.forEach { sessionExercise ->
                    if (sessionExercise.sessionExercise.parentSessionId == session.sessionId) {
                        SessionExerciseWithExerciseCondensed(sessionExercise)
                    }
                }
            }
        }
    }
}

@Composable
fun SessionExerciseWithExerciseCondensed(sessionExercise: SessionExerciseWithExercise) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(sessionExercise.exercise.exerciseTitle)
        Spacer(Modifier.weight(1f))
        Text("13x25, 14x45")
    }
}
