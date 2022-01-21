package com.example.android.january2022.ui.home

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.utils.Event
import com.example.android.january2022.utils.UiEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val sessions by viewModel.sessionList.observeAsState(listOf())
    val composableScope = rememberCoroutineScope()
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit // do nothing
            }
        }
    }


    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            Box(
                modifier = Modifier
                    .height(220.dp)
                    .padding(vertical = 16.dp, horizontal = 16.dp)
            ) {
                DrawerMenu(viewModel::onEvent)
            }
        },
        scrimColor = Color(0x99121212)
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
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
                            // TODO: Implement search-button
                        }
                    ) {
                        Icon(Icons.Default.Search, "")
                    }
                    IconButton(
                        onClick = {
                            // TODO: Implement more-button
                        }
                    ) {
                        Icon(Icons.Default.MoreVert, "")
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.onEvent(HomeEvent.OnAddSessionClick) },
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
fun DrawerMenu(onEvent: (Event) -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(Modifier.clickable {
            onEvent(DrawerEvent.OnHomeClicked)
        }) {
            DrawerMenuItem("home")
        }
        Box(Modifier.clickable {
            onEvent(DrawerEvent.OnExercisesClicked)
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

    val sessionContent = viewModel.sessionExerciseList.observeAsState(listOf())
    val sets = viewModel.sets.observeAsState(listOf())

    val coroutineScope = rememberCoroutineScope()

    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = sessions) { session ->
            SessionCard(
                session,
                sessionContent.value,
                sets.value,
                viewModel,
                viewModel::onEvent
            )
        }
        item {
            Spacer(modifier = Modifier.height(124.dp))
        }
    }
}


