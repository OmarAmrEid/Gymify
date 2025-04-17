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


@Composable
fun ExercisesScreen(
    modifier: Modifier = Modifier,
    viewModel: ExcersisecViewModel = hiltViewModel(),
    navController: NavController,
    onExerciseClick: (ExcersiceItem) -> Unit = {},
) {
    val exercisesItems by viewModel.exerciseList.observeAsState(emptyList())
    val error by viewModel.errorMessage.observeAsState()

    var selectedFilter by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredExercises = exercisesItems.filter { exercise ->
        val matchesFilter = selectedFilter == "All" || exercise.target.equals(selectedFilter, ignoreCase = true)
        val matchesSearch = exercise.name.contains(searchQuery, ignoreCase = true)
        matchesFilter && matchesSearch
    }

    Column(modifier.fillMaxSize().padding(16.dp)) {
        // Greeting Section
        Text(
            text = "Hello Azeem,",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )
        Text(
            text = "Good morning.",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

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
                text = "Mon 26 Apr",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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
            Text(
                text = "See All",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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

        // Featured Workout Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF222222)),
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Placeholder for featured workout image
                AsyncImage(
                    model = "https://example.com/featured-workout.jpg",
                    contentDescription = "Featured Workout",
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
                        text = "Learn the Basic of Training",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "06 Workouts for Beginner",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Exercises Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredExercises) { exercise ->
                ExerciseCard(exercise = exercise, onExerciseClick = onExerciseClick)
            }
        }
    }
}