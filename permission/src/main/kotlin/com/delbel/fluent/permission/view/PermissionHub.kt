package com.delbel.fluent.permission.view

import com.delbel.fluent.permission.job.PermissionRequesterJob
import io.fluent.rx.RxHub
import javax.inject.Inject

class PermissionHub @Inject internal constructor(
    private val permissionRequesterJob: PermissionRequesterJob
) : RxHub<PermissionView>() {

    override fun connect(view: PermissionView) {
        view.requestPermission().bind(permissionRequesterJob)

        // TODO bind result
    }
}
