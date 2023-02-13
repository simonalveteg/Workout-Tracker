package com.example.android.january2022.ui.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
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
  val sessions = uiState.value.sessions.collectAsState(initial = emptyList())

  Scaffold(
    bottomBar = {
      BottomAppBar() {

      }
    }
  ) { paddingValues ->
    LazyColumn() {
      item {
        Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding()))
      }
      items(sessions.value) {
        Text(text = it.start.toString())
      }
    }
  }
}