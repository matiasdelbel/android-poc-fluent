package com.delbel.fluent.permission.job

import android.app.Activity
import com.delbel.fluent.permission.model.AndroidPermissionRequester
import com.delbel.fluent.permission.model.PermissionAlreadyGrantedException
import com.delbel.fluent.permission.model.PermissionRequest
import com.delbel.fluent.permission.model.RequireRationalePermissionException
import com.delbel.fluent.permission.state.PermissionAlreadyGranted
import com.delbel.fluent.permission.state.PermissionState
import com.delbel.fluent.permission.state.RequireRationalePermission
import com.delbel.fluent.permission.state.WaitingPermissionResult
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.fluent.StateType
import io.fluent.rx.RxStore
import io.reactivex.Single
import org.junit.Test

class PermissionRequesterJobTest {

    @Test
    fun `bind with permission already granted should notify it`() {
        val screen = mock<Activity>()
        val input = mockPermissionRequest(activity = screen)
        val single = Single.fromCallable<Unit> { throw PermissionAlreadyGrantedException() }
        val requester = mockPermissionRequester(activity = screen, result = single)
        val store = RxStore(initialState = PermissionState())

        PermissionRequesterJob(store, requester).bind(input).test()
        val observer = store.stateChanges().test()

        observer.assertValue { it.type is PermissionAlreadyGranted }
    }

    @Test
    fun `bind with permission require rational should notify it`() {
        val screen = mock<Activity>()
        val input = mockPermissionRequest(activity = screen)
        val single = Single.fromCallable<Unit> { throw RequireRationalePermissionException() }
        val requester = mockPermissionRequester(activity = screen, result = single)
        val store = RxStore(initialState = PermissionState())

        PermissionRequesterJob(store, requester).bind(input).test()
        val observer = store.stateChanges().test()

        observer.assertValue { it.type is RequireRationalePermission }
    }

    @Test
    fun `bind with no error should notify it`() {
        val screen = mock<Activity>()
        val input = mockPermissionRequest(activity = screen)
        val single = Single.fromCallable<Unit> { }
        val requester = mockPermissionRequester(activity = screen, result = single)
        val store = RxStore(initialState = PermissionState())

        PermissionRequesterJob(store, requester).bind(input).test()
        val observer = store.stateChanges().test()

        observer.assertValue { it.type is WaitingPermissionResult }
    }

    private fun mockPermissionRequest(activity: Activity) = mock<PermissionRequest> {
        on { screen } doReturn activity
        on { permission } doReturn "permission"
        on { requestCode } doReturn 123
    }

    private fun mockPermissionRequester(activity: Activity, result: Single<Unit>) = mock<AndroidPermissionRequester> {
            on { requestPermission(activity, "permission", 123) } doReturn result
        }
}