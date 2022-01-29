package com.example.android.january2022.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomAppBar
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.utils.Event
import com.example.android.january2022.utils.UiEvent
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val sessions by viewModel.sessionList.observeAsState(listOf())
    val colors = MaterialTheme.colorScheme

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit // do nothing
            }
        }
    }
    Scaffold(
        modifier = Modifier.padding(bottom = 60.dp),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(HomeEvent.OnAddSessionClick) },
                shape = RoundedCornerShape(35),
                containerColor = colors.primary,
            ) {
                Icon(
                    Icons.Filled.Add,
                    "Start new session",
                    tint = colors.onPrimary
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) {
        Column {
            Box(
                Modifier
                    .weight(1f)
                    .statusBarsPadding()) {
                SessionCardList(sessions = sessions, viewModel)
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SessionCardList(
    sessions: List<Session>,
    viewModel: HomeViewModel
) {
    val sessionContent by viewModel.sessionExerciseList.observeAsState(listOf())
    val sets by viewModel.sets.observeAsState(listOf())
    LazyColumn {
        items(sessions) { session ->
            BigSessionCard(
                session = session,
                sessionContent = sessionContent.filter { it.sessionExercise.parentSessionId == session.sessionId },
                sets = sets,
                viewModel = viewModel,
                onEvent = viewModel::onEvent
            )
        }
    }
}



