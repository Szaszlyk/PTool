package com.example.pavtool

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
import com.example.pavtool.permissions.PermissionsHandler
import com.example.pavtool.user_interface.MyApp
import com.example.pavtool.ui.theme.PAVToolTheme
import com.example.pavtool.navigation.AppSection
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.DrawerValue

/**
 * MainActivity is the entry point of the application, handling permission checks
 * and setting up the content view with Compose.
 */
class MainActivity : ComponentActivity() {

    private lateinit var permissionsHandler: PermissionsHandler
    private lateinit var requestPhoneCallPermissionLauncher: ActivityResultLauncher<String>

    /**
     * Called when the activity is first created. This is where you should do all of your
     * normal static set up: create views, bind data to lists, etc. This method also provides
     * you with a Bundle containing the activity's previously frozen state, if there was one.
     */
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
            PAVToolTheme {
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val selectedTab = remember { mutableStateOf(AppSection.SupplementaryServices) }

                MyApp(drawerState, scope, selectedTab, ::makePhoneCall)
            }
        }
    }

    /**
     * Called after onStart() when the activity is being re-initialized from a previously saved state.
     * This is where the permission check for phone calls is re-validated.
     */
    override fun onResume() {
        super.onResume()
        permissionsHandler.checkCallPermission()
    }

    /**
     * Initiates a phone call to the given number if the CALL_PHONE permission is granted.
     * If the permission is not granted, shows the permission settings dialog.
     *
     * @param number The phone number to call.
     */
    private fun makePhoneCall(number: String) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val processedNumber = number.replace("+", "00")
            val encodedNumber = Uri.encode(processedNumber)
            Log.d("makePhoneCall", "Encoded URI: tel:$encodedNumber")
            startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$encodedNumber")))
        } else {
            permissionsHandler.showPermissionSettingsDialog()
        }
    }
}
