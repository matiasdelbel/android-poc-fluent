package com.delbel.fluent.permission.job

import com.delbel.fluent.permission.di.PermissionStore
import com.delbel.fluent.permission.model.AndroidPermissionRequester
import com.delbel.fluent.permission.model.PermissionAlreadyGrantedException
import com.delbel.fluent.permission.model.PermissionRequest
import com.delbel.fluent.permission.model.RequireRationalePermissionException
import com.delbel.fluent.permission.state.PermissionAlreadyGranted
import com.delbel.fluent.permission.state.PermissionState
import com.delbel.fluent.permission.state.RequireRationalePermission
import io.fluent.StateType
import io.fluent.rx.RxJob
import io.fluent.rx.RxStore
import io.reactivex.Completable
import javax.inject.Inject

internal class PermissionRequesterJob @Inject constructor(
    @PermissionStore private val store: RxStore<PermissionState>,
    private val permissionRequester: AndroidPermissionRequester
) : RxJob<PermissionRequest>() {

    override fun bind(input: PermissionRequest): Completable =
        permissionRequester.requestPermission(input.screen, input.permission, input.requestCode)
            .doOnSubscribe { notifyRequestInProgress() }
            .doOnSuccess { notifySuccess() }
            .doOnError { notifyError(error = it) }
            .ignoreElement()
            .onErrorComplete()

    private fun notifyRequestInProgress() = store.update { transitionTo(type = StateType.Loading) }

    private fun notifyError(error: Throwable) = when (error) {
        is PermissionAlreadyGrantedException -> store.update { transitionTo(type = PermissionAlreadyGranted()) }
        is RequireRationalePermissionException -> store.update { transitionTo(type = RequireRationalePermission()) }
        else -> store.update { transitionTo(type = StateType.Error) }
    }

    private fun notifySuccess() = store.update { transitionTo(type = StateType.Success) }
}