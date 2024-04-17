package com.example.happyplaces

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

var PERMISSION_DENIED_BEFORE = false

/*
    Storage Permissions Logic
 */
val STORAGE_PERMISSIONS = mutableListOf<String>().apply {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
        add(Manifest.permission.READ_EXTERNAL_STORAGE)
    } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
        add(Manifest.permission.READ_MEDIA_IMAGES)
    } else
        addAll(arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
        ))
}.toTypedArray()

// Checking if the App has all required permissions granted to access the Media on Storage
fun hasStoragePermission(context: Context) = STORAGE_PERMISSIONS.all { permission ->
    ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}

/*
    Camera Permission Logic
 */
val CAMERA_PERMISSIONS =
    mutableListOf(
        Manifest.permission.CAMERA
    ).apply {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P)
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }.toTypedArray()

// Checking if the App has all required permissions granted to access the Camera
fun hasCameraPermissions(context: Context) = CAMERA_PERMISSIONS.all { permission ->
    ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}

