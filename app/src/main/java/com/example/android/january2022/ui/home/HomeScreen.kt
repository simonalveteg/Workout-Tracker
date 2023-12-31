package com.example.android.january2022.ui.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.ui.home.components.HomeFab
import com.example.android.january2022.ui.home.components.HomeTopAppBar
import com.example.android.january2022.ui.home.components.SessionCard
import com.example.android.january2022.utils.UiEvent

@Composable
fun HomeScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
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

    Scaffold(
        topBar = {
            HomeTopAppBar(onEvent = viewModel::onEvent)
        },
        floatingActionButton = {
            HomeFab(onEvent = viewModel::onEvent)
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
        ) {
            item {
                Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding()))
            }
            items(items = sessions, key = { it.session.sessionId }) { session ->
                SessionCard(sessionWrapper = session) {
                    viewModel.onEvent(HomeEvent.SessionClicked(session))
                }
            }
        }
    }
}
