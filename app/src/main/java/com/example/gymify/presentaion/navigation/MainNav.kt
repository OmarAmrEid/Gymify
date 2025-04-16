package com.example.gymify.presentaion.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.gymify.domain.models.ExcersiceItem
import com.example.gymify.presentaion.excersices.ExcersisecViewModel
import com.example.gymify.presentaion.excersices.ExerciseDetailsScreen
import com.example.gymify.presentaion.excersices.ExercisesScreen
import com.example.gymify.presentaion.home.HomeScreen
import com.example.gymify.presentaion.meals.MealsScreen
import com.example.gymify.presentaion.plan.PlanScreen
import com.example.gymify.presentaion.profile.ProfileScreen
import com.google.gson.Gson


@Composable
fun MainNav() {
    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Exercises,
        BottomNavItem.Meals,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color.Black,
                tonalElevation = 4.dp
            ) {
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(item.label) },
                        selected = false,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) { HomeScreen() }
            composable(BottomNavItem.Exercises.route) { ExercisesScreen(
                navController = navController,
                onExerciseClick = { exercise ->
                    navController.navigate("exercise_detail/${exercise.id}")
                }
            )}
            composable(BottomNavItem.Meals.route) { MealsScreen() }
            composable(BottomNavItem.Profile.route) { ProfileScreen(navController = navController) }

            composable(
                route = "exercise_detail/{exerciseId}",
                arguments = listOf(navArgument("exerciseId") { type = NavType.StringType })
            ) { backStackEntry ->
                val exerciseId = backStackEntry.arguments?.getString("exerciseId") ?: ""
                ExerciseDetailsScreen(exerciseID = exerciseId, onAddToPlanClick = ({}))
            }



            composable(Screen.Plan.route) { PlanScreen() } // Add PlanScreen here

        }
    }
}