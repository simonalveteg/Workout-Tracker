package com.example.android.january2022

import android.content.pm.PackageInstaller
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.ui.theme.January2022Theme
import java.text.SimpleDateFormat
import java.util.*

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
                        SessionScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavController) {
    val sessions: List<Session> by viewModel.sessionList.observeAsState(listOf())
    Scaffold(
        bottomBar = { MyBottomBar(viewModel) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onNewSession()
                    navController.navigate("session")
                },
                shape = RoundedCornerShape(50),
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, "")
            }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center
    ) {
        SessionCardList(sessions = sessions)

    }
}

@Composable
fun SessionCardList(sessions: List<Session>) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = sessions) { session ->
            SessionCard(session)

        }
    }
}

@Composable
fun SessionCard(session: Session) {
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
                    Text(text = session.sessionId.toString())
                    Text(text = stringResource(R.string.lorem_ipsum))
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

@Composable
fun MyBottomBar(viewModel: HomeViewModel) {
    BottomAppBar(cutoutShape = RoundedCornerShape(50)) {
        IconButton(
            onClick = {
                /* do something */
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
}


@ExperimentalMaterialApi
@Composable
fun SessionScreen() {
    BackdropScaffold(
        appBar = { SessionAppBar() },
        backLayerContent = { SessionInfo() },
        frontLayerContent = { SessionContent() }
    ) {

    }
}

@Composable
fun SessionAppBar() {
    //TopAppBar(title = { Text("snopp") })
}


@Composable
fun SessionContent() {
    Column(modifier = Modifier
        .background(MaterialTheme.colors.background)
        .fillMaxSize()
    ) {
        var count: Int = 0
        repeat(
            10
        ) {
            count++
            SessionExercise(value = count)
        }
    }
}

@Composable
fun SessionExercise(value: Int) {
    val paddingTop = if (value == 1) 32.dp else 1.dp
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = paddingTop, bottom = 4.dp, start = 8.dp, end = 8.dp)
    ) {
        Row(modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)) {
            Text(text = "SessionExercise $value")
        }
    }
}


@Composable
fun SessionInfo() {

}


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SessionPreview() {
    January2022Theme {
        SessionCard(Session(1, 10, 20, "Legs"))
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    January2022Theme {
        //HomeScreen()
    }
}