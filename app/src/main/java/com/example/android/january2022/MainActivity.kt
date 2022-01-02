package com.example.android.january2022

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.android.january2022.db.entities.*
import com.example.android.january2022.ui.theme.January2022Theme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KFunction1

class MainActivity : ComponentActivity() {

    private val homeViewModel by viewModels<HomeViewModel>()

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            January2022Theme {
                val navController = rememberNavController()

                NavHost(navController, "home") {
                    composable("home") {
                        HomeScreen(homeViewModel, navController)
                    }
                    composable("session") {
                        SessionScreen(homeViewModel, navController)
                    }
                    composable("exercises") {
                        ExercisesScreen(homeViewModel, navController)
                    }
                    composable("exercisePicker") {
                        ExercisePickerScreen(homeViewModel, navController)
                    }
                }
                // when the session object in viewModel gets update, navigate to SessionScreen
                homeViewModel.sessionId.observe(this) {
                    Log.d("MA", "Session observed changed value to $it")
                    navController.navigate("session")
                }
            }
        }
    }
}


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    January2022Theme {
        //HomeScreen()
    }
}