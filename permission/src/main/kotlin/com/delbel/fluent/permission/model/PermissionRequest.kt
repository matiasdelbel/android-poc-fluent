package com.delbel.fluent.permission.model

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.app.Activity

open class PermissionRequest(
    val screen: Activity,
    val requestCode: Int,
    val permission: String
)

class LocationPermissionRequest(screen: Activity, requestCode: Int) :
    PermissionRequest(screen, requestCode, permission = ACCESS_COARSE_LOCATION)