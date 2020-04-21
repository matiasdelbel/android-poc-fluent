package com.delbel.fluent.permission.job

import androidx.core.content.PermissionChecker.PERMISSION_DENIED
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.delbel.fluent.permission.model.PermissionResult
import com.delbel.fluent.permission.state.PermissionDenied
import com.delbel.fluent.permission.state.PermissionGranted
import com.delbel.fluent.permission.state.PermissionState
import io.fluent.rx.RxStore
import org.junit.Test

class PermissionCheckerJobTest {

    @Test
    fun `bind with permission granted should notify it`() {
        val store = RxStore(initialState = PermissionState())
        val grantResult = IntArray(1).apply { this[0] = PERMISSION_GRANTED }
        val permissionResult = PermissionResult(grantResult)

        PermissionCheckerJob(store).bind(permissionResult).test()
        val observer = store.stateChanges().test()

        observer.assertValue { it.type is PermissionGranted }
    }

    @Test
    fun `bind with permission declined should notify it`() {
        val store = RxStore(initialState = PermissionState())
        val grantResult = IntArray(1).apply { this[0] = PERMISSION_DENIED }
        val permissionResult = PermissionResult(grantResult)

        PermissionCheckerJob(store).bind(permissionResult).test()
        val observer = store.stateChanges().test()

        observer.assertValue { it.type is PermissionDenied }
    }
}