package com.example.supserapp.user_interface

import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.supserapp.ui.theme.md_theme_light_secondary
import com.example.supserapp.ui.theme.md_theme_light_onSecondary

@Composable
fun themedButton(text: String, width: Dp = 150.dp, onClick: () -> Unit) {
    Button(
        modifier = Modifier.width(width),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = md_theme_light_secondary
        )
    ) {
        Text(text, color = md_theme_light_onSecondary)
    }
}

@Composable
fun MenuButton(text: String, width: Dp = 250.dp, onClick: () -> Unit) {
    Button(
        modifier = Modifier.width(width),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = md_theme_light_secondary
        )
    ) {
        Text(text, color = md_theme_light_onSecondary)
    }
}
