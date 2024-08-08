package com.example.pavtool.user_interface

import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pavtool.ui.theme.md_theme_light_secondary
import com.example.pavtool.ui.theme.md_theme_light_onSecondary

/**
 * A custom themed button with a specific width.
 *
 * @param text The text to display on the button.
 * @param width The width of the button.
 * @param onClick The action to perform when the button is clicked.
 */
@Composable
fun ThemedButton(text: String, width: Dp = 150.dp, onClick: () -> Unit) {
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

/**
 * A custom menu button with a specific width.
 *
 * @param text The text to display on the button.
 * @param width The width of the button.
 * @param onClick The action to perform when the button is clicked.
 */
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
