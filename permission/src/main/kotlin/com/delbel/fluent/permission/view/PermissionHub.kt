package com.delbel.fluent.permission.view

import com.delbel.fluent.permission.job.PermissionCheckerJob
import com.delbel.fluent.permission.job.PermissionRequesterJob
import io.fluent.rx.RxHub
import javax.inject.Inject

class PermissionHub @Inject internal constructor(
    private val permissionRequesterJob: PermissionRequesterJob,
    private val permissionCheckerJob: PermissionCheckerJob
) : RxHub<PermissionView>() {

    override fun connect(view: PermissionView) {
        view.requestPermission().bind(permissionRequesterJob)
        view.permissionResult().bind(permissionCheckerJob)
    }
}
