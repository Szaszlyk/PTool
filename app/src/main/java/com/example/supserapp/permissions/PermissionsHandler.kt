package com.example.supserapp.permissions

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

class PermissionsHandler(
    private val activity: Activity,
    private val requestPhoneCallPermissionLauncher: ActivityResultLauncher<String>
) {

    var isCallPermissionGranted = false
        private set

    fun checkFirstRun() {
        val prefs = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val firstStart = prefs.getBoolean("firstStart", true)
        if (firstStart) {
            requestPhoneCallPermission()
        }
    }

    fun requestPhoneCallPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPhoneCallPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
        }
    }

    fun checkCallPermission() {
        isCallPermissionGranted = ContextCompat.checkSelfPermission(
            activity, Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun showPermissionSettingsDialog() {
        AlertDialog.Builder(activity)
            .setMessage("This feature requires phone call permission to function. Please allow it in app settings.")
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
