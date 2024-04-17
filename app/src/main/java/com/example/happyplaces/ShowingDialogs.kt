package com.example.happyplaces

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object ShowingDialogs {
    fun rationalDialogCamera(
        activity: AppCompatActivity,
        askPermissions: ActivityResultLauncher<Array<String>>) {
        MaterialAlertDialogBuilder(activity)
            .setCancelable(false)
            .setIcon(R.drawable.high_importance_48px)
            .setTitle(activity.resources.getString(R.string.warning))
            .setMessage(activity.resources.getString(R.string.warning_camera_message))
            .setPositiveButton("Okay!") { _, _ ->
                PERMISSION_DENIED_BEFORE = true
                askPermissions.launch(CAMERA_PERMISSIONS)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                PERMISSION_DENIED_BEFORE = true
                dialog.dismiss()
            }
            .show()
    }

    fun rationalDialogStorage(
        activity: AppCompatActivity,
        askPermissions: ActivityResultLauncher<Array<String>>) {
        MaterialAlertDialogBuilder(activity)
            .setCancelable(false)
            .setIcon(R.drawable.high_importance_48px)
            .setTitle(activity.resources.getString(R.string.warning))
            .setMessage(activity.resources.getString(R.string.warning_storage_message))
            .setPositiveButton("Okay!") { _, _ ->
                PERMISSION_DENIED_BEFORE = true
                askPermissions.launch(STORAGE_PERMISSIONS)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                PERMISSION_DENIED_BEFORE = true
                dialog.dismiss()
            }
            .show()
    }

    fun rationalDialogSettings(activity: AppCompatActivity) {
        MaterialAlertDialogBuilder(activity)
            .setCancelable(false)
            .setIcon(R.drawable.gearbox_warning_30px)
            .setTitle(activity.resources.getString(R.string.warning_disabled))
            .setMessage(activity.resources.getString(R.string.warning_message_disabled))
            .setPositiveButton("Go to settings!") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", activity.packageName, null)
                intent.data = uri
                activity.startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}