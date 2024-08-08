package com.example.pavtool.user_interface

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pavtool.R
import com.example.pavtool.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material.ModalDrawer
import androidx.compose.material.DrawerState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import com.example.pavtool.navigation.AppSection
import androidx.compose.material3.*

/**
 * Main application composable, sets up the navigation drawer and main content.
 *
 * @param drawerState The state of the navigation drawer.
 * @param scope The coroutine scope for managing state changes.
 * @param selectedTab The currently selected tab.
 * @param makePhoneCall Function to initiate a phone call.
 */
@Composable
fun MyApp(
    drawerState: DrawerState,
    scope: CoroutineScope,
    selectedTab: MutableState<AppSection>,
    makePhoneCall: (String) -> Unit
) {
    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(drawerState, scope) { newTab -> selectedTab.value = newTab }
        },
        content = {
            Scaffold(
                topBar = { TopBar(drawerState, scope, selectedTab.value) },
                content = { padding -> Content(selectedTab.value, padding, makePhoneCall) }
            )
        }
    )
}

/**
 * Drawer content for the navigation drawer.
 *
 * @param drawerState The state of the navigation drawer.
 * @param scope The coroutine scope for managing state changes.
 * @param onTabSelected Function to handle tab selection.
 */
@Composable
fun DrawerContent(
    drawerState: DrawerState,
    scope: CoroutineScope,
    onTabSelected: (AppSection) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

/**
 * Top bar composable for the application.
 *
 * @param drawerState The state of the navigation drawer.
 * @param scope The coroutine scope for managing state changes.
 * @param selectedTab The currently selected tab.
 */
@Composable
fun TopBar(drawerState: DrawerState, scope: CoroutineScope, selectedTab: AppSection) {
    val headerText = when (selectedTab) {
        AppSection.SupplementaryServices -> "SS"
        AppSection.AutoCalls -> "Calls"
    }
    Header(drawerState, scope, headerText)
}

/**
 * Main content composable for the application.
 *
 * @param selectedTab The currently selected tab.
 * @param padding Padding values for the content.
 * @param makePhoneCall Function to initiate a phone call.
 */
@Composable
fun Content(selectedTab: AppSection, padding: PaddingValues, makePhoneCall: (String) -> Unit) {
    Box(modifier = Modifier.padding(padding)) {
        when (selectedTab) {
            AppSection.SupplementaryServices -> SupplementaryServicesScreen(makePhoneCall)
            AppSection.AutoCalls -> AutoCallsScreen()
        }
    }
}

/**
 * Header composable for the navigation drawer.
 *
 * @param drawerState The state of the navigation drawer.
 * @param scope The coroutine scope for managing state changes.
 * @param headerText The text to display in the header.
 */
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
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
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
                    Icon(
                        Icons.Filled.Menu,
                        contentDescription = "Menu",
                        tint = md_theme_light_primary
                    )
                }
            }
        }
    }
}
