package com.example.supserapp

import android.Manifest
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import com.example.supserapp.permissions.PermissionsHandler
import com.example.supserapp.user_interface.MyApp
import com.example.supserapp.ui.theme.SupserappTheme
import com.example.supserapp.navigation.AppSection
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.DrawerValue

class MainActivity : ComponentActivity() {

    private lateinit var permissionsHandler: PermissionsHandler
    private lateinit var requestPhoneCallPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPhoneCallPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted) {
                permissionsHandler.showPermissionSettingsDialog()
            }
        }

        permissionsHandler = PermissionsHandler(this, requestPhoneCallPermissionLauncher)
        permissionsHandler.checkFirstRun()

        setContent {
            SupserappTheme {
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val selectedTab = remember { mutableStateOf(AppSection.SupplementaryServices) }

                MyApp(drawerState, scope, selectedTab, ::makePhoneCall)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        permissionsHandler.checkCallPermission()
    }

    private fun makePhoneCall(number: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            val processedNumber = number.replace("+", "00")
            val encodedNumber = Uri.encode(processedNumber)
            Log.d("makePhoneCall", "Encoded URI: tel:$encodedNumber")
            startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$encodedNumber")))
        } else {
            permissionsHandler.showPermissionSettingsDialog()
        }
    }
}
