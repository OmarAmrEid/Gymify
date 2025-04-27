package com.example.gymify.presentaion.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymify.ui.theme.PrimaryRed
import com.example.gymify.ui.theme.PrimaryText
import com.example.gymify.ui.theme.SecondaryText

@Composable
fun ProfileToggleItem(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    textColor: Color = PrimaryText,
    switchColors: SwitchColors = SwitchDefaults.colors(
        checkedThumbColor = PrimaryRed,
        checkedTrackColor = PrimaryRed.copy(alpha = 0.5f),
        uncheckedThumbColor = SecondaryText,
        uncheckedTrackColor = SecondaryText.copy(alpha = 0.3f)
    )
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 16.sp, color = textColor)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = switchColors
        )
    }
}


@Composable
fun ProfileItem(label: String,textColor: Color = PrimaryRed, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = textColor, fontSize = 16.sp)
    }
}