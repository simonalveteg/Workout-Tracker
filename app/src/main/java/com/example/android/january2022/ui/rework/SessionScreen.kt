package com.example.android.january2022.ui.rework

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.january2022.utils.UiEvent
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(
  onNavigate: (UiEvent.Navigate) -> Unit,
) {

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
    LazyColumn(
      modifier = Modifier
        .padding(paddingValues = paddingValues)
        .fillMaxSize()
    ) {
      item {
        Box(
          modifier = Modifier
            .padding(start = 12.dp, top = 120.dp, bottom = 40.dp)
            .fillMaxWidth()
        ) {
          Text(
            text = "Jan 9 2023",
            style = MaterialTheme.typography.headlineLarge
          )
        }
      }
      items(2) {
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(vertical = 8.dp, horizontal = 8.dp),
          tonalElevation = 2.dp,
          shape = MaterialTheme.shapes.medium
        ) {
          Column(Modifier.padding(vertical = 12.dp, horizontal = 12.dp)) {
            Text(
              text = "Lever Front Pulldown",
              style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(4.dp))
            LazyRow {
              items(3) {
                CompactSetCard(reps = 12, weight = 45f)
              }
            }
          }
        }
      }
    }
  }
}