package com.delbel.fluent.permission.model

import android.app.Activity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.PermissionChecker.checkSelfPermission
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

internal class AndroidPermissionRequester @Inject constructor() {

    fun requestPermission(screen: Activity, permission: String, request: Int) = Single.fromCallable {
        throwIfPermissionIsAlreadyGranted(screen, permission)
        throwIfRequireRationalePermission(screen, permission)

        requestPermissions(screen, arrayOf(permission), request)
    }

    private fun throwIfPermissionIsAlreadyGranted(screen: Activity, permission: String) {
        if (checkSelfPermission(screen, permission) == PERMISSION_GRANTED)
            throw PermissionAlreadyGrantedException()
    }

    private fun throwIfRequireRationalePermission(screen: Activity, permission: String) {
        if (shouldShowRequestPermissionRationale(screen, permission))
            throw RequireRationalePermissionException()
    }
}

class PermissionAlreadyGrantedException : RuntimeException()

class RequireRationalePermissionException : RuntimeException()