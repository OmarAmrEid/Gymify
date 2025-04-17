package com.example.gymify.presentaion.excersices

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import com.example.gymify.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymify.presentaion.excersices.components.ExerciseCard
import androidx.compose.material3.FilterChip
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gymify.domain.models.ExcersiceItem
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.gymify.ui.theme.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import com.example.gymify.data.local.appDataBase.ExerciseEntity
import com.example.gymify.data.local.planDB.PlanExerciseEntity
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.foundation.clickable
import java.text.SimpleDateFormat
import java.util.*
import com.example.gymify.presentaion.plan.PlanViewModel


@Composable
fun ExercisesScreen(
    modifier: Modifier = Modifier,
    exerciseViewModel: ExcersisecViewModel = hiltViewModel(),
    planViewModel: PlanViewModel = hiltViewModel(),
    navController: NavController,
    onExerciseClick: (ExcersiceItem) -> Unit = {},
) {
    val exercisesItems by exerciseViewModel.exerciseList.observeAsState(emptyList())
    val error by exerciseViewModel.errorMessage.observeAsState()

    val planExercises by planViewModel.planExercises.observeAsState(emptyList())
    val groupedByDay = planExercises.groupBy { it.day }

    var selectedFilter by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredExercises = exercisesItems.filter { exercise ->
        val matchesFilter = selectedFilter == "All" || exercise.target.equals(selectedFilter, ignoreCase = true)
        val matchesSearch = exercise.name.contains(searchQuery, ignoreCase = true)
        matchesFilter && matchesSearch
    }

    LaunchedEffect(Unit) {
        planViewModel.loadPlan()
    }

    val currentDay = getCurrentDay()

    Column(modifier.fillMaxSize().padding(16.dp)) {
        // Today Workout Plan Section
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Today Workout Plan",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Text(
                text = getCurrentDate(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display Today's Workout Plan using LazyRow
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            groupedByDay[currentDay]?.forEach { exercise ->
                item {
                    Card(
                        modifier = Modifier
                            .width(300.dp)
                            .height(180.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF222222)),
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            // Placeholder for exercise image
                            AsyncImage(
                                model = exercise.gifUrl,
                                contentDescription = exercise.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                Text(
                                    text = exercise.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Workout Categories Section
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Workout Categories",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search exercises") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Category Chips
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            val categories = listOf(
                "All", "abductors", "abs", "adductors", "biceps", "calves",
                "cardiovascular system", "delts", "forearms", "glutes", "hamstrings",
                "lats", "levator scapulae", "pectorals", "quads", "serratus anterior",
                "spine", "traps", "triceps", "upper back"
            )

            categories.forEach { category ->
                FilterChip(
                    selected = selectedFilter == category,
                    onClick = { selectedFilter = category },
                    label = { Text(category) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = if (selectedFilter == category) Purple40 else PurpleGrey40,
                        labelColor = TextLight
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Featured Workouts Row
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(count = filteredExercises.size) { index ->
                val exercise = filteredExercises[index]
                Card(
                    modifier = Modifier
                        .width(300.dp)
                        .height(180.dp)
                        .clickable { onExerciseClick(exercise) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF222222)),
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Placeholder for exercise image
                        AsyncImage(
                            model = exercise.gifUrl,
                            contentDescription = exercise.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Text(
                                text = exercise.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

// Function to get current date in "Mon 26 Apr" format
fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("EEE dd MMM", Locale.getDefault())
    return dateFormat.format(Date())
}

// Function to get current day in "Monday" format
fun getCurrentDay(): String {
    val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    return dayFormat.format(Date())
}