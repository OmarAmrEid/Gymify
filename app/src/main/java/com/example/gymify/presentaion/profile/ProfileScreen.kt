package com.example.gymify.presentaion.profile


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.gymify.presentaion.navigation.Screen
import com.example.gymify.presentaion.profile.components.ProfileItem
import com.example.gymify.presentaion.profile.components.ProfileToggleItem
import com.example.gymify.ui.theme.*

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onEditInfoClick: () -> Unit = {},
    onManagePlansClick: () -> Unit = {},
    onLogOutClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }

    Column(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Header
        Text(
            "Profile",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(20.dp))

        // User Info Card
        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "Omar",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(8.dp))
                Text("Height: 180 cm", color = MaterialTheme.colorScheme.onBackground)
                Text("Weight: 94 kg", color = MaterialTheme.colorScheme.onBackground)
                Text("BMI: 29.0", color = TextGray)

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = { /* Handle edit info click */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple40,
                        contentColor = TextLight
                    )
                ) {
                    Text("Edit Info", color = TextLight)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Settings
        Text("Settings", fontSize = 18.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(12.dp))

        ProfileToggleItem(
            label = "Notifications",
            checked = notificationsEnabled,
            onCheckedChange = { notificationsEnabled = it }
        )

        ProfileToggleItem(
            label = "Dark Mode",
            checked = darkModeEnabled,
            onCheckedChange = { darkModeEnabled = it }
        )

        ProfileItem("Manage Plans") {
            navController.navigate(Screen.Plan.route)
            Toast.makeText(context, "Coming Soon!", Toast.LENGTH_SHORT).show()
        }

        ProfileItem("Log Out") {
            Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
        }
    }
}
