package com.delbel.fluent.permission.view

import com.delbel.fluent.permission.model.PermissionRequest
import com.delbel.fluent.permission.model.PermissionResult
import com.delbel.fluent.permission.state.PermissionState
import io.fluent.View
import io.reactivex.Observable

interface PermissionView : View<PermissionState> {

    fun requestPermission(): Observable<PermissionRequest>

    fun permissionResult(): Observable<PermissionResult>
}