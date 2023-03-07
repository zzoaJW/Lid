package com.z0o0a.lid.repository

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ReqPermissionCameraGallery(private val context: Context) {
    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 101
        private const val GALLERY_PERMISSION_REQUEST_CODE = 102
    }

    private val cameraPermission = Manifest.permission.CAMERA
    private val galleryPermission = Manifest.permission.READ_EXTERNAL_STORAGE

    fun reqCameraPermission(callback: (Boolean) -> Unit) {
        if (ContextCompat.checkSelfPermission(context, cameraPermission) == PackageManager.PERMISSION_GRANTED) {
            callback(true)
        } else {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(cameraPermission), CAMERA_PERMISSION_REQUEST_CODE)
        }
    }

    fun reqGalleryPermission(callback: (Boolean) -> Unit) {
        if (ContextCompat.checkSelfPermission(context, galleryPermission) == PackageManager.PERMISSION_GRANTED) {
            callback(true)
        } else {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(galleryPermission), GALLERY_PERMISSION_REQUEST_CODE)
        }
    }
}