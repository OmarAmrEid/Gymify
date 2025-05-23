package com.example.gymify.presentaion.profile


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.gymify.data.local.DataStoreManager
import com.example.gymify.presentaion.navigation.Screen
import com.example.gymify.presentaion.profile.components.NotificationSettings
import com.example.gymify.presentaion.profile.components.ProfileItem
import com.example.gymify.presentaion.profile.components.ProfileToggleItem
import com.example.gymify.ui.theme.*

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogOutClick: () -> Unit = {}
) {
    val context = LocalContext.current

    val notificationsEnabled by viewModel.notificationsEnabledFlow.collectAsState(initial = false)
    val darkModeEnabled by viewModel.darkModeFlow.collectAsState(initial = false)

    val name by viewModel.name.collectAsState()
    val height by viewModel.height.collectAsState()
    val weight by viewModel.weight.collectAsState()

    var isEditing by remember { mutableStateOf(false) }
    var showNotificationSettings by remember { mutableStateOf(false) }

    val notificationDetails by viewModel.notificationTime.collectAsState("")


    Column(
        modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryText)
        Spacer(Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark)
        ) {
            Column(Modifier.padding(16.dp)) {
                if (isEditing) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { viewModel.setName(it) },
                        label = { Text("Name", color = SecondaryText) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = height,
                        onValueChange = { viewModel.setHeight(it.filter { ch -> ch.isDigit() }) },
                        label = { Text("Height (cm)", color = SecondaryText) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = weight,
                        onValueChange = { viewModel.setWeight(it.filter { ch -> ch.isDigit() }) },
                        label = { Text("Weight (kg)", color = SecondaryText) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))

                    Row {
                        Button(
                            onClick = {
                                isEditing = false
                                Toast.makeText(context, "Info Saved", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                        ) {
                            Text("Save", color = PrimaryText)
                        }
                        Spacer(Modifier.width(16.dp))
                        Button(
                            onClick = {
                                isEditing = false
                            }
                        ) {
                            Text("Cancel", color = PrimaryText)
                        }
                    }
                } else {
                    Text(name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PrimaryText)
                    Spacer(Modifier.height(8.dp))
                    Text("Height: $height cm", color = SecondaryText)
                    Text("Weight: $weight kg", color = SecondaryText)
                    val bmi = calculateBMI(height, weight)
                    Text("BMI: $bmi", color = PrimaryText)
                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = { isEditing = true },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                    ) {
                        Text("Edit Info", color = PrimaryText)
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text("Settings", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = PrimaryText)
        Spacer(Modifier.height(12.dp))

        ProfileToggleItem(
            label = "Notifications",
            checked = notificationsEnabled,
            onCheckedChange = {
                viewModel.setNotificationsEnabled(it)
                if (it) {
                    Toast.makeText(context, "Notifications Enabled", Toast.LENGTH_SHORT).show()
                    showNotificationSettings = true
                } else {
                    showNotificationSettings = false
                    Toast.makeText(context, "Notifications Disabled", Toast.LENGTH_SHORT).show()
                    viewModel.disableNotification(context)
                    viewModel.clearNotificationDetails()
                }
            }
        )

        if (showNotificationSettings) {
            NotificationSettings(
                viewModel = viewModel,
                onSaveNotificationTime = { selectedDay, selectedTime ->
                    viewModel.scheduleNotification(context, selectedDay, selectedTime)
                    showNotificationSettings = false },
                context = context
            )
        }

        if (notificationDetails.isNotEmpty()) {
            Text(
                text = notificationDetails,
                color = SecondaryText,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        ProfileToggleItem(
            label = "Dark Mode",
            checked = darkModeEnabled,
            onCheckedChange = {
                viewModel.setDarkMode(it)
            },
        )

        Spacer(Modifier.height(16.dp))

        ProfileItem("Log Out") {
            Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
            onLogOutClick()
        }
    }


}

@Composable fun calculateBMI(heightCm: String, weightKg: String): String {
    val height = heightCm.toFloatOrNull() ?: return "-"
    val weight = weightKg.toFloatOrNull() ?: return "-"
    if (height == 0f) return "-"
    val bmi = weight / ((height / 100) * (height / 100))
    return String.format("%.1f", bmi)
}