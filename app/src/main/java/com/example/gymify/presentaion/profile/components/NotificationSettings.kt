package com.example.gymify.presentaion.profile.components

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import com.google.accompanist.flowlayout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MovableContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.gymify.presentaion.profile.ProfileViewModel
import com.example.gymify.ui.theme.BackgroundDark
import com.example.gymify.ui.theme.PrimaryRed
import com.example.gymify.ui.theme.PrimaryText
import com.example.gymify.ui.theme.SecondaryText
import com.example.gymify.ui.theme.SurfaceDark

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NotificationSettings(
    viewModel: ProfileViewModel,
    context: Context,
    onSaveNotificationTime: (String, String) -> Unit
) {
    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    var selectedDays by remember { mutableStateOf(setOf<String>()) }
    var selectedTime by remember { mutableStateOf("00:00") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp)
    ) {
        Text(
            "Select Day for Notification",
            style = MaterialTheme.typography.bodyMedium,
            color = PrimaryText
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 8.dp
        ) {
            daysOfWeek.forEach { day ->
                val isSelected = selectedDays.contains(day)

                Button(
                    onClick = {
                        selectedDays = if (isSelected) {
                            selectedDays - day
                        } else {
                            selectedDays + day
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) PrimaryRed else SurfaceDark,
                        contentColor = PrimaryText
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = day,
                        color = PrimaryText
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Select Notification Time",
            style = MaterialTheme.typography.bodyMedium,
            color = PrimaryText
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            selectedTime,
            style = MaterialTheme.typography.bodyMedium,
            color = SecondaryText
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        selectedTime = String.format("%02d:%02d", hour, minute)
                    },
                    0, 0, true
                ).show()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryRed,
                contentColor = PrimaryText
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Pick Time")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                selectedDays.forEach { day ->
                    onSaveNotificationTime(day, selectedTime)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryRed,
                contentColor = PrimaryText
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Save Notification Time")
        }
    }
}