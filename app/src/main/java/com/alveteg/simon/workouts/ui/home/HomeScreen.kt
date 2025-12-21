package com.alveteg.simon.workouts.ui.home

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alveteg.simon.workouts.ui.home.components.HomeContainer
import com.alveteg.simon.workouts.ui.home.components.SessionCard
import com.alveteg.simon.workouts.utils.UiEvent

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  onNavigate: (UiEvent.Navigate) -> Unit,
  sharedTransitionScope: SharedTransitionScope,
  animatedVisibilityScope: AnimatedVisibilityScope,
  viewModel: HomeViewModel = hiltViewModel()
) {
  val sessions by viewModel.sessions.collectAsState()
  val tagline by viewModel.tagline.collectAsState()
  val greeting = viewModel.greeting

  val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

  LaunchedEffect(true) {
    viewModel.uiEvent.collect { event ->
      when (event) {
        is UiEvent.Navigate -> onNavigate(event)
        else -> Unit
      }
    }
  }

  with(sharedTransitionScope) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
      val expandedHeight = maxHeight * 0.35f

      Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
          TopAppBar(
            expandedHeight = expandedHeight,
            scrollBehavior = scrollBehavior,
            contentPadding = PaddingValues(0.dp),
            title = {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .windowInsetsPadding(WindowInsets.statusBars) // Prevents overlapping status bar
                  .padding(start = 8.dp, end = 16.dp, top = 24.dp),
                verticalArrangement = Arrangement.Top
              ) {
                Row(
                  horizontalArrangement = Arrangement.SpaceBetween,
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = greeting,
                    style = MaterialTheme.typography.headlineLarge,
                  )
                  IconButton(onClick = { viewModel.onEvent(HomeEvent.OpenSettings) }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                  }
                }
                Text(
                  text = tagline,
                  style = MaterialTheme.typography.labelLarge,
                  color = MaterialTheme.colorScheme.secondary,
                )

                // Using a Spacer with the expanded height effectively pushes
                // the content above it to the top of the internal layout box.
                Spacer(modifier = Modifier.height(expandedHeight))
              }
            }
          )
        }
      ) { innerPadding ->
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
          contentPadding = innerPadding,
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          item {
            HomeContainer(
              onClick = { viewModel.onEvent(HomeEvent.NewSession) },
              color = MaterialTheme.colorScheme.primary,
            ) {
              Box(modifier = Modifier.fillMaxSize()) {
                Text(
                  text = "NEW WORKOUT",
                  style = MaterialTheme.typography.titleLarge,
                  modifier = Modifier.align(Alignment.Center)
                )
              }
            }
          }

          items(
            items = sessions,
            key = { it.session.sessionId }
          ) { session ->
            SessionCard(
              modifier = Modifier.sharedBounds(
                sharedContentState = rememberSharedContentState(key = "session-${session.session.sessionId}"),
                animatedVisibilityScope = animatedVisibilityScope
              ),
              dateModifier = Modifier.sharedElement(
                sharedContentState = rememberSharedContentState(key = "session-date-${session.session.sessionId}"),
                animatedVisibilityScope = animatedVisibilityScope
              ),
              titleModifier = Modifier.sharedBounds(
                sharedContentState = rememberSharedContentState(key = "session-title-${session.session.sessionId}"),
                animatedVisibilityScope = animatedVisibilityScope,
              ),
              sessionWrapper = session
            ) {
              viewModel.onEvent(HomeEvent.SessionClicked(session))
            }
          }
        }
      }
    }
  }
}
