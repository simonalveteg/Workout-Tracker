package com.alveteg.simon.workouts.ui.home

import android.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alveteg.simon.workouts.ui.home.components.HomeContainer
import com.alveteg.simon.workouts.ui.home.components.SessionCard
import com.alveteg.simon.workouts.utils.UiEvent

@Composable
fun HomeScreen(
  onNavigate: (UiEvent.Navigate) -> Unit,
  viewModel: HomeViewModel = hiltViewModel()
) {
  val sessions by viewModel.sessions.collectAsState(initial = emptyList())

  LaunchedEffect(true) {
    viewModel.uiEvent.collect { event ->
      when (event) {
        is UiEvent.Navigate -> onNavigate(event)
        else -> Unit
      }
    }
  }

  Surface(
    color = MaterialTheme.colorScheme.background
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding(),
      verticalArrangement = Arrangement.SpaceBetween,
    ) {
      Column(
        modifier = Modifier
          .padding(top = 16.dp, start = 24.dp, end = 16.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Top
        ) {
          Text(
            text = "Good Morning",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
              .weight(1f)
              .padding(top = 8.dp)
          )
          IconButton(
            onClick = { viewModel.onEvent(HomeEvent.OpenSettings) }
          ) {
            Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
          }
        }
        Text(
          text = "You're doing great!",
          style = MaterialTheme.typography.labelLarge,
          color = MaterialTheme.colorScheme.secondary,
        )
      }
      Column(
        modifier = Modifier.padding(12.dp)
      ) {
        HomeContainer(
          onClick = { viewModel.onEvent(HomeEvent.NewSession) },
          color = MaterialTheme.colorScheme.primary
        ) {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(8.dp)
          ) {
            Text(
              text = "NEW WORKOUT",
              style = MaterialTheme.typography.titleLarge,
              modifier = Modifier.align(Alignment.Center)
            )
          }
        }
        sessions.take(4).forEach { session ->
          SessionCard(sessionWrapper = session) {
            viewModel.onEvent(HomeEvent.SessionClicked(session))
          }
        }
        Box(
          modifier = Modifier.fillMaxWidth()
        ) {
          Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
              .padding(horizontal = 16.dp)
              .align(Alignment.CenterEnd),
            shape = MaterialTheme.shapes.extraLarge,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.surfaceContainer,
              contentColor = MaterialTheme.colorScheme.onSurface
            )
          ) {
            Text(
              text = "SHOW MORE",
              style = MaterialTheme.typography.labelMedium,
              modifier = Modifier.padding(horizontal = 8.dp)
            )
          }
        }
        Spacer(modifier = Modifier.navigationBarsPadding())
      }
    }
  }
}