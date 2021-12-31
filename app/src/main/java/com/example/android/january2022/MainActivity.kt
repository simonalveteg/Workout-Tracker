package com.example.android.january2022

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.db.entities.SessionExerciseWithExercise
import com.example.android.january2022.ui.theme.January2022Theme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KFunction1

class MainActivity : ComponentActivity() {

    private val homeViewModel by viewModels<HomeViewModel>()

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            January2022Theme {
                val navController = rememberNavController()

                NavHost(navController, "home") {
                    composable("home") {
                        HomeScreen(homeViewModel, navController)
                    }
                    composable("session") {
                        SessionScreen(homeViewModel, navController)
                    }
                    composable("exercises") {
                        ExercisesScreen(homeViewModel, navController)
                    }
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavController) {
    val sessions: List<Session> by viewModel.sessionList.observeAsState(listOf())
    val composableScope = rememberCoroutineScope()
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            Column(
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
                BottomAppBar(cutoutShape = RoundedCornerShape(50)) {
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
                            /* doSomething() */
                        }
                    ) {
                        Icon(Icons.Default.Search, "")
                    }
                    IconButton(
                        onClick = {
                            viewModel.clearSessions()
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
                        navController.navigate("session")
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
            SessionCardList(sessions = sessions, viewModel::onSessionClicked, navController)
        }
    }
}

@Composable
fun DrawerMenu(viewModel: HomeViewModel, navController: NavController) {
    Column(Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ) {
        Box(Modifier.clickable { navController.navigate("home") }) {
            DrawerMenuItem("home")
        }
        Box(Modifier.clickable { navController.navigate("exercises") }) {
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
    onSessionClicked: KFunction1<Session, Unit>,
    navController: NavController
) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = sessions) { session ->
            SessionCard(session, onSessionClicked, navController)
        }
    }
}

@Composable
fun SessionCard(
    session: Session,
    onSessionClicked: KFunction1<Session, Unit>,
    navController: NavController
) {
    var expanded by remember { mutableStateOf(false) }

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
                onSessionClicked(session)
                navController.navigate("session")
            }
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    delayMillis = 50,
                    easing = LinearOutSlowInEasing
                )
            )
    ) {
        Row(modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp)) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .weight(1f)
            ) {
                Text(text = startDate)
                Text(text = startTime)
                if (expanded) {
                    Column(Modifier.padding(top = 12.dp)) {
                        Text(text = session.sessionId.toString())
                        Text(text = stringResource(R.string.lorem_ipsum))
                    }
                }
            }

            IconButton(onClick = {
                expanded = !expanded
            }) {
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
    }
}

@ExperimentalMaterialApi
@Composable
fun SessionScreen(homeViewModel: HomeViewModel, navController: NavController) {
    // TODO: implement backdropState
    val backdropState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)
    BackdropScaffold(
        appBar = { },
        backLayerContent = { },
        frontLayerContent = { SessionContent(homeViewModel) },
        scaffoldState = backdropState,
        backLayerBackgroundColor = MaterialTheme.colors.background
    ) {
    }
}


@Composable
fun SessionContent(homeViewModel: HomeViewModel) {
    val sessionExercises: List<SessionExerciseWithExercise> by homeViewModel.sessionExerciseList.observeAsState(
        listOf()
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        Button(
            onClick = { homeViewModel.addSessionExerciseToSession() },
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
            Text(text = "sEID: ${sessionExercise.sessionExercise.sessionExerciseId}, sID: ${sessionExercise.sessionExercise.parentSessionId}")
        }
    }
}


@Composable
fun SessionInfo() {

}

@Composable
fun ExercisesScreen(viewModel: HomeViewModel, navController: NavController) {
    // remember inputValue
    var inputValue by remember { mutableStateOf("") }

    val exercises: List<Exercise> by viewModel.exerciseList.observeAsState(listOf())

    Surface(Modifier.fillMaxSize().padding(vertical = 16.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .weight(1f)) {
                items(items = exercises) { exercise ->
                    ExerciseCard(exercise)
                }
            }
            Row(
                Modifier
                    .padding(vertical = 16.dp)
                    .height(TextFieldDefaults.MinHeight)) {
                TextField(
                    value = inputValue,
                    onValueChange = { newText ->
                        inputValue = newText
                    },
                    placeholder = { Text(text = "Enter exercise-name") }
                )
                Button(
                    onClick = {
                        viewModel.onNewExercise(inputValue)
                        inputValue = ""
                    },
                    Modifier
                        .padding(start = 8.dp)
                        .fillMaxHeight()
                ) {
                    Text(text = "Submit")
                }
            }
        }
    }
}

@Composable
fun ExerciseCard(exercise: Exercise) {
    Card(Modifier.padding(vertical = 2.dp, horizontal = 16.dp)) {
        Row(Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 16.dp)){
            Text(text = "${exercise.exerciseId} ${exercise.exerciseTitle}" )
        }
    }
}


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    January2022Theme {
        //HomeScreen()
    }
}