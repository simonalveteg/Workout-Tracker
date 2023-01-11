package com.example.android.january2022.ui.rework

import android.util.Half.toFloat
import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.utils.UiEvent
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(
  onNavigate: (UiEvent.Navigate) -> Unit,
  viewModel: MainViewModel = hiltViewModel()
) {

  val uiState = viewModel.uiState.collectAsState()
  val session = uiState.value.currentSession.collectAsState(initial = Session(10))
  LaunchedEffect(true) {
    Log.d("SessionScreen", session.value.toString())
  }

  val scrollState = rememberLazyListState()

  // TODO: Change to be dependent on SessionExerciseId
  val expanded = remember {
    mutableStateOf(true)
  }

  Scaffold(
    bottomBar = {
      BottomAppBar(
        actions = {
          Spacer(modifier = Modifier.width(4.dp))
          IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options")
          }
          Spacer(modifier = Modifier.width(8.dp))
          IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Outlined.Timer, contentDescription = "Options")
          }
        },
        floatingActionButton = {
          FloatingActionButton(
            onClick = { /*TODO*/ },
            containerColor = MaterialTheme.colorScheme.primary
          ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Exercise")
          }
        }
      )
    }
  ) { paddingValues ->
    Box() {
      Box(
        modifier = Modifier
          .padding(
            start = 12.dp,
            top = paddingValues.calculateTopPadding() + 120.dp,
            bottom = 40.dp
          )
          .fillMaxWidth()
          .graphicsLayer {
            val scroll = scrollState.firstVisibleItemScrollOffset.toFloat()
            translationY = -scroll / 2f // Parallax effect
            alpha = 1 - scroll / 250f // Fade out text
            scaleX = 1 - scroll / 3000f
            scaleY = 1 - scroll / 3000f
          }
      ) {
        Text(
          text = session.value.toSessionTitle(),
          style = MaterialTheme.typography.headlineLarge
        )
      }
      LazyColumn(
        modifier = Modifier
          .fillMaxSize(),
        state = scrollState
      ) {
        item {
          Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding() + 180.dp))
        }
        items(2) {
          ExerciseCard(
            expanded = expanded.value
          ) {
            expanded.value = !expanded.value
          }
        }
        item {
          Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding()))
        }
      }
    }
  }
}

fun Session.toSessionTitle(): String {
  return try {
    DateTimeFormatter.ofPattern("MMM d yyyy").format(this.start)
  } catch (e: Exception) {
    "no date"
  }
}