package com.example.supserapp.user_interface

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import com.example.supserapp.ui.theme.md_theme_light_primary
import com.example.supserapp.ui.theme.md_theme_light_secondaryContainer
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.Alignment
import com.example.supserapp.ui.theme.md_theme_light_secondary
import com.example.supserapp.ui.theme.md_theme_light_tertiaryContainer

@Composable
fun SupplementaryServicesScreen(makePhoneCall: (String) -> Unit) {
    val cfNumber = remember { mutableStateOf("") }
    var combinedValue by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.Center
        ) {
            enterCFNumberFunction(cfNumber)
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
            CallForwardingOption("Call Forwarding Unconditional", "21", "#", cfNumber) { combinedValue = it; makePhoneCall(it) }
            Spacer(modifier = Modifier.height(10.dp))
            CallForwardingOption("Call Forwarding When Busy", "67", "#", cfNumber) { combinedValue = it; makePhoneCall(it) }
            Spacer(modifier = Modifier.height(10.dp))
            CallForwardingOption("Call Forwarding When Not Reachable", "61", "#", cfNumber) { combinedValue = it; makePhoneCall(it) }
            Spacer(modifier = Modifier.height(10.dp))
            CallForwardingOption("Call Forwarding When Not Answered", "62", "#", cfNumber) { combinedValue = it; makePhoneCall(it) }
        }
    }
}

@Composable
fun CallForwardingOption(title: String, prefix: String, suffix: String, cfNumber: MutableState<String>, onCombinedValueChange: (String) -> Unit) {
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
            themedButton("Registration", 150.dp) { onCombinedValueChange("**${prefix}*${cfNumber.value}${suffix}") }
            themedButton("Activation", 150.dp) { onCombinedValueChange("*${prefix}*${cfNumber.value}*11${suffix}") }
        }
        Spacer(modifier = Modifier.height(2.5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            themedButton("Interrogate", 150.dp) { onCombinedValueChange("*#${prefix}#") }
            themedButton("Deactivate", 150.dp) { onCombinedValueChange("#${prefix}#") }
        }
        Spacer(modifier = Modifier.height(2.5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            themedButton("Erase all", 150.dp) { onCombinedValueChange("##${prefix}*#") }
        }
        Spacer(modifier = Modifier.height(2.5.dp))
    }
}

@Composable
fun enterCFNumberFunction(cfNumber: MutableState<String>) {
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
