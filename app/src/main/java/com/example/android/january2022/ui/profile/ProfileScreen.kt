package com.example.android.january2022.ui.profile

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.widget.EditText
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.ui.home.HomeViewModel
import com.example.android.january2022.utils.BottomBarScreen
import com.example.android.january2022.utils.UiEvent
import kotlinx.coroutines.flow.collect
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val launch = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {  }
    )

    val mContext = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                is UiEvent.ShareIntent -> {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_CREATE_DOCUMENT
                        putExtra(
                            Intent.EXTRA_TITLE,
                            "workout_db_${LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)}.txt"
                        )
                        putExtra(
                            Intent.EXTRA_TEXT,
                            event.file
                        )
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, "hej")
                    Log.d("PS", "share intent received")
                    launch.launch(shareIntent)
                }
                else -> Unit // do nothing
            }
        }
    }
    Scaffold() {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("This is your profile")
            FilledTonalButton(modifier = Modifier.padding(8.dp), onClick = {
                viewModel.onEvent(ProfileEvent.NavigateToExercises)
            }) {
                Text("Exercises")
            }
            FilledTonalButton(onClick = {
                viewModel.onEvent(ProfileEvent.ExportDatabase)
            }) {

            }
        }
    }
}