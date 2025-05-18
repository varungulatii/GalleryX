package com.varun.galleryx.core.permission

import android.Manifest
import android.os.Build

object PermissionHelper {

    fun requiredPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO
            )
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    fun allPermissionsGranted(
        grantedMap: Map<String, Boolean>
    ): Boolean {
        return requiredPermissions().all { grantedMap[it] == true }
    }
}
