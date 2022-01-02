package com.example.android.january2022

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.android.january2022.ui.theme.January2022Theme
import com.example.android.january2022.ui.theme.screens.ExercisePickerScreen
import com.example.android.january2022.ui.theme.screens.ExercisesScreen
import com.example.android.january2022.ui.theme.screens.HomeScreen
import com.example.android.january2022.ui.theme.screens.SessionScreen

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
                    navController.navigate("session"){
                        popUpTo("home")
                    }
                }
                // navigate to exercisePicker
                homeViewModel.navigateToExercisePicker.observe(this) {
                    if (it == 1) {
                        navController.navigate("exercisePicker"){
                            popUpTo("session")
                        }
                    }
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