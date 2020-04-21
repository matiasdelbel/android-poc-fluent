package com.delbel.fluent.permission.job

import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.delbel.fluent.permission.di.PermissionStore
import com.delbel.fluent.permission.model.PermissionResult
import com.delbel.fluent.permission.state.PermissionDenied
import com.delbel.fluent.permission.state.PermissionGranted
import com.delbel.fluent.permission.state.PermissionState
import io.fluent.rx.RxJob
import io.fluent.rx.RxStore
import io.reactivex.Completable
import javax.inject.Inject

internal class PermissionCheckerJob @Inject constructor(
    @PermissionStore private val store: RxStore<PermissionState>
) : RxJob<PermissionResult>() {

    override fun bind(input: PermissionResult) = Completable.fromCallable {
        when (arePermissionGranted(results = input.grantResults)) {
            true -> store.update { transitionTo(type = PermissionGranted()) }
            false -> store.update { transitionTo(type = PermissionDenied()) }
        }
    }

    private fun arePermissionGranted(results: IntArray) =
        results.isNotEmpty() && results[0] == PERMISSION_GRANTED
}