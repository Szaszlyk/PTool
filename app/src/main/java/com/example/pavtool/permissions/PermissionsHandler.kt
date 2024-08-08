package com.example.pavtool.permissions

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

/**
 * Handles permission checks and requests for the application.
 *
 * @property activity The activity context.
 * @property requestPhoneCallPermissionLauncher The launcher for requesting phone call permissions.
 */
class PermissionsHandler(
    private val activity: Activity,
    private val requestPhoneCallPermissionLauncher: ActivityResultLauncher<String>
) {

    var isCallPermissionGranted = false
        private set

    /**
     * Checks if this is the first run of the application and requests phone call permission if so.
     */
    fun checkFirstRun() {
        val prefs = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val firstStart = prefs.getBoolean("firstStart", true)
        if (firstStart) {
            requestPhoneCallPermission()
        }
    }

    /**
     * Requests the phone call permission.
     */
    fun requestPhoneCallPermission() {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPhoneCallPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
        }
    }

    /**
     * Checks if the phone call permission is granted and updates the state accordingly.
     */
    fun checkCallPermission() {
        isCallPermissionGranted = ContextCompat.checkSelfPermission(
            activity, Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Shows a dialog prompting the user to open the app settings to allow the phone call permission.
     */
    fun showPermissionSettingsDialog() {
        AlertDialog.Builder(activity)
            .setMessage(
                "This feature requires phone call permission to function. " +
                        "Please allow it in app settings."
            )
            .setPositiveButton("Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", activity.packageName, null)
                intent.data = uri
                activity.startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
