package com.example.android.january2022

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.android.january2022.db.GymDatabase
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.ui.theme.January2022Theme
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            January2022Theme {
                val navController = rememberNavController()
                val database = GymDatabase.getInstance(application)
                val repository = GymRepository(database)

                NavHost(navController, "home") {
                    composable("home") {
                        HomeScreen(repository)
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
fun HomeScreen(repository: GymRepository) {
    Scaffold(
        bottomBar = { MyBottomBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

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
        //TestItem(text = "HOME SCREEN!")
        //val sessions = repository.getAllSessions().value
        val sessions = listOf(Session(0), Session(1), Session(2), Session(3))
        SessionList(sessions = sessions)

    }
}

@Composable
fun SessionList(sessions: List<Session>) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = sessions) { session ->
            Session(session)

        }
    }
}

@Composable
fun Session(session: Session) {
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
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .weight(1f)
            ) {
                Text(text = startDate)
                Text(text = startTime)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = session.sessionId.toString())
                Spacer(modifier = Modifier.weight(1f))
                if (expanded) {
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
fun MyBottomBar() {
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
                /* doSomething() */
            }
        ) {
            Icon(Icons.Default.MoreVert, "")
        }
    }

}


@Composable
fun SessionScreen() {
    TestItem(text = "SESSION SCREEN!")
}

@Composable
fun TestItem(text: String) {
    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = text, modifier = Modifier.padding(24.dp))
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SessionPreview() {
    January2022Theme {
        Session(Session(1, 10, 20, "Legs"))
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    January2022Theme {
        //HomeScreen()
    }
}