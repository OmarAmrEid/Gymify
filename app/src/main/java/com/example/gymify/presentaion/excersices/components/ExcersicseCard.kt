package com.example.gymify.presentaion.excersices.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.example.gymify.domain.models.ExcersiceItem
import dagger.hilt.android.qualifiers.ApplicationContext
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color



@Composable
fun ExerciseCard(exercise: ExcersiceItem , onExerciseClick: (ExcersiceItem) -> Unit = {}) {

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                Toast.makeText(context, "Clicked on ${exercise.name}", Toast.LENGTH_SHORT).show()
                onExerciseClick(exercise)
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = exercise.gifUrl,
                contentDescription = exercise.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = exercise.name,
                modifier = Modifier.padding(8.dp),
                fontWeight = FontWeight.Medium
            )
        }
    }
}