package com.example.pavtool.user_interface

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import com.example.pavtool.ui.theme.md_theme_light_primary
import com.example.pavtool.ui.theme.md_theme_light_secondaryContainer
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.Alignment
import com.example.pavtool.ui.theme.md_theme_light_secondary
import com.example.pavtool.ui.theme.md_theme_light_tertiaryContainer

/**
 * Composable for the Supplementary Services screen.
 *
 * @param makePhoneCall Function to initiate a phone call.
 */
@Composable
fun SupplementaryServicesScreen(makePhoneCall: (String) -> Unit) {
    val cfNumber = remember { mutableStateOf("") }
    var combinedValue by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.Center
        ) {
            EnterCFNumberFunction(cfNumber)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(combinedValue)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CallForwardingOption(
                title = "Call Forwarding Unconditional",
                prefix = "21",
                suffix = "#",
                cfNumber = cfNumber
            ) { combinedValue = it; makePhoneCall(it) }
            Spacer(modifier = Modifier.height(10.dp))
            CallForwardingOption(
                title = "Call Forwarding When Busy",
                prefix = "67",
                suffix = "#",
                cfNumber = cfNumber
            ) { combinedValue = it; makePhoneCall(it) }
            Spacer(modifier = Modifier.height(10.dp))
            CallForwardingOption(
                title = "Call Forwarding When Not Reachable",
                prefix = "61",
                suffix = "#",
                cfNumber = cfNumber
            ) { combinedValue = it; makePhoneCall(it) }
            Spacer(modifier = Modifier.height(10.dp))
            CallForwardingOption(
                title = "Call Forwarding When Not Answered",
                prefix = "62",
                suffix = "#",
                cfNumber = cfNumber
            ) { combinedValue = it; makePhoneCall(it) }
        }
    }
}

/**
 * Composable for displaying a call forwarding option.
 *
 * @param title The title of the call forwarding option.
 * @param prefix The prefix code for the call forwarding.
 * @param suffix The suffix code for the call forwarding.
 * @param cfNumber The call forwarding number.
 * @param onCombinedValueChange Function to handle the combined value change.
 */
@Composable
fun CallForwardingOption(
    title: String,
    prefix: String,
    suffix: String,
    cfNumber: MutableState<String>,
    onCombinedValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .background(md_theme_light_secondaryContainer, shape = RoundedCornerShape(10.dp))
            .fillMaxWidth(0.95f),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = md_theme_light_primary,
            modifier = Modifier.padding(start = 16.dp, top = 5.dp)
        )
        Spacer(modifier = Modifier.height(2.5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ThemedButton("Registration", 150.dp) {
                onCombinedValueChange("**${prefix}*${cfNumber.value}${suffix}")
            }
            ThemedButton("Activation", 150.dp) {
                onCombinedValueChange("*${prefix}*${cfNumber.value}*11${suffix}")
            }
        }
        Spacer(modifier = Modifier.height(2.5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ThemedButton("Interrogate", 150.dp) {
                onCombinedValueChange("*#${prefix}#")
            }
            ThemedButton("Deactivate", 150.dp) {
                onCombinedValueChange("#${prefix}#")
            }
        }
        Spacer(modifier = Modifier.height(2.5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ThemedButton("Erase all", 150.dp) {
                onCombinedValueChange("##${prefix}*#")
            }
        }
        Spacer(modifier = Modifier.height(2.5.dp))
    }
}

/**
 * Composable for entering the call forwarding number.
 *
 * @param cfNumber The call forwarding number.
 */
@Composable
fun EnterCFNumberFunction(cfNumber: MutableState<String>) {
    val options = remember {
        mutableStateListOf(
            "" to "User entry",
            "+48223779542" to "Landline 1",
            "+48223779543" to "Landline 2",
            "+48223779544" to "Landline 3"
        )
    }
    var expanded by remember { mutableStateOf(false) }
    val selectedOption = remember { mutableStateOf(options.first().first) }

    LaunchedEffect(selectedOption.value) {
        cfNumber.value = selectedOption.value
    }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = selectedOption.value,
        onValueChange = { newValue -> selectedOption.value = newValue },
        label = { Text("Enter CF number:") },
        colors = TextFieldDefaults.colors(
            unfocusedTextColor = md_theme_light_primary,
            unfocusedContainerColor = md_theme_light_tertiaryContainer,
            focusedContainerColor = md_theme_light_tertiaryContainer,
            focusedTextColor = md_theme_light_primary,
            cursorColor = md_theme_light_primary,
            focusedIndicatorColor = md_theme_light_secondary,
            unfocusedIndicatorColor = md_theme_light_secondary,
            unfocusedLabelColor = md_theme_light_primary,
            focusedLabelColor = md_theme_light_primary,
            unfocusedTrailingIconColor = md_theme_light_primary,
            focusedTrailingIconColor = md_theme_light_primary
        ),
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
        trailingIcon = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Add, "Add", Modifier.clickable {
                    val customLabel = "Custom"
                    if (selectedOption.value.isNotEmpty() && selectedOption.value !in options.map { it.first }) {
                        options.add(selectedOption.value to customLabel)
                    }
                })
                Icon(
                    Icons.Filled.ArrowDropDown,
                    "Dropdown",
                    Modifier.clickable { expanded = !expanded }
                )
            }
        }
    )

    DropdownMenu(
        modifier = Modifier.fillMaxWidth(0.8F),
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        options.forEach { (number, label) ->
            DropdownMenuItem(
                onClick = {
                    selectedOption.value = number
                    expanded = false
                },
                text = { Text(label) }
            )
        }
    }
}
