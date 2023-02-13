package com.example.android.january2022.ui.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.ui.rework.MainViewModel
import com.example.android.january2022.utils.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  onNavigate: (UiEvent.Navigate) -> Unit,
  viewModel: MainViewModel = hiltViewModel()
) {
  val uiState = viewModel.homeState.collectAsState()
  val sessions by uiState.value.sessions.collectAsState(initial = emptyList())

  LaunchedEffect(true) {
    viewModel.uiEvent.collect { event ->
      when (event) {
        is UiEvent.Navigate -> onNavigate(event)
        else -> Unit
      }
    }
  }

  Scaffold(
    bottomBar = {
      HomeAppBar(onEvent = viewModel::onEvent)
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
      item {
        Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding()))
      }
      items(sessions, key = { it.session.sessionId }) { session ->
        SessionCard(
          sessionWrapper = session,
          onClick = {
            viewModel.onEvent(HomeEvent.SessionClicked(session))
          }
        )
      }
    }
  }
}