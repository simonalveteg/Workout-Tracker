package com.example.android.january2022.ui.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.ui.home.HomeViewModel
import com.example.android.january2022.utils.BottomBarScreen
import com.example.android.january2022.utils.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    Scaffold() {
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text("This is some statistics")
        }
    }
}