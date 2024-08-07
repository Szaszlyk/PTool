package com.example.supserapp

import android.Manifest
import android.app.AlertDialog
import android.net.Uri
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.core.content.ContextCompat
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.example.supserapp.ui.theme.*
import android.provider.Settings
import android.util.Log
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import androidx.compose.material.ModalDrawer
import androidx.compose.material.DrawerState
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.DrawerValue

class MainActivity : ComponentActivity() {

    enum class AppSection {
        SupplementaryServices,
        AutoCalls
    }

    private var isCallPermissionGranted = false
    private lateinit var requestPhoneCallPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupPermissionLauncher()
        checkFirstRun()

        setContent {
            MyApp()
        }
    }

    override fun onResume() {
        super.onResume()
        checkCallPermission()
    }

    private fun setupPermissionLauncher() {
        requestPhoneCallPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            isCallPermissionGranted = isGranted
            if (!isGranted) {
                showPermissionSettingsDialog()
            }
        }
    }

    private fun checkFirstRun() {
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val firstStart = prefs.getBoolean("firstStart", true)
        if (firstStart) {
            requestPhoneCallPermission()
        }
    }

    private fun requestPhoneCallPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPhoneCallPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
        }
    }

    private fun checkCallPermission() {
        isCallPermissionGranted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun showPermissionSettingsDialog() {
        AlertDialog.Builder(this)
            .setMessage("This feature requires phone call permission to function. Please allow it in app settings.")
            .setPositiveButton("Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Main app composable function
    @Composable
    fun MyApp() {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var selectedTab by remember { mutableStateOf(AppSection.SupplementaryServices) }

        ModalDrawer(
            drawerState = drawerState,
            drawerContent = { DrawerContent(drawerState, scope, selectedTab) { newTab -> selectedTab = newTab } },
            content = {
                Scaffold(
                    topBar = { TopBar(drawerState, scope, selectedTab) },
                    content = { padding -> Content(selectedTab, padding) }
                )
            }
        )
    }

    // Drawer content composable
    @Composable
    fun DrawerContent(drawerState: DrawerState, scope: CoroutineScope, selectedTab: AppSection, onTabSelected: (AppSection) -> Unit) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Header(drawerState, scope, headerText = "Menu")
            Spacer(modifier = Modifier.height(10.dp))
            MenuButton("Supplementary Services", 250.dp) {
                onTabSelected(AppSection.SupplementaryServices)
                scope.launch { drawerState.close() }
            }
            MenuButton("Auto Calls", 250.dp) {
                onTabSelected(AppSection.AutoCalls)
                scope.launch { drawerState.close() }
            }
        }
    }

    // Top bar composable
    @Composable
    fun TopBar(drawerState: DrawerState, scope: CoroutineScope, selectedTab: AppSection) {
        val headerText = when (selectedTab) {
            AppSection.SupplementaryServices -> "SS"
            AppSection.AutoCalls -> "Calls"
        }
        Header(drawerState, scope, headerText)
    }

    // Content composable that switches between tabs
    @Composable
    fun Content(selectedTab: AppSection, padding: PaddingValues) {
        Box(modifier = Modifier.padding(padding)) {
            when (selectedTab) {
                AppSection.SupplementaryServices -> SupplementaryServicesContent()
                AppSection.AutoCalls -> AutoCallsContent()
            }
        }
    }

    // Header composable
    @Composable
    fun Header(drawerState: DrawerState, scope: CoroutineScope, headerText: String) {
        Box {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .align(Alignment.BottomStart)
                    .background(md_theme_light_primary)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.06f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = stringResource(R.string.logo_description),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 10.dp)
                )
                Text(
                    text = "PAV Tool - ",
                    color = md_theme_light_primary,
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Text(
                    text = headerText,
                    color = md_theme_light_primary,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.Bold,
                )
                Row(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                ) {
                    IconButton(onClick = {
                        scope.launch {
                            if (drawerState.isClosed) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = md_theme_light_primary)
                    }
                }
            }
        }
    }

    @Composable
    fun SupplementaryServicesContent() {
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
    fun AutoCallsContent() {
        Text("Calls (Work in progress)")
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

    fun makePhoneCall(number: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            val processedNumber = number.replace("+", "00")
            val encodedNumber = Uri.encode(processedNumber)
            Log.d("makePhoneCall", "Encoded URI: tel:$encodedNumber")
            startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$encodedNumber")))
        } else {
            showPermissionSettingsDialog()
        }
    }
}
