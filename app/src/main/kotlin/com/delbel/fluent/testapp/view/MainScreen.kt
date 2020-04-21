package com.delbel.fluent.testapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.delbel.dagger.rx.MainScheduler
import com.delbel.fluent.permission.di.PermissionStore
import com.delbel.fluent.permission.model.LocationPermissionRequest
import com.delbel.fluent.permission.model.PermissionRequest
import com.delbel.fluent.permission.model.PermissionResult
import com.delbel.fluent.permission.state.*
import com.delbel.fluent.permission.view.PermissionHub
import com.delbel.fluent.permission.view.PermissionView
import com.delbel.fluent.testapp.R
import com.delbel.fluent.testapp.databinding.ScreenMainBinding
import dagger.android.AndroidInjection
import io.fluent.rx.RxStore
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class MainScreen : AppCompatActivity(), PermissionView {

    private lateinit var viewBinding: ScreenMainBinding

    @Inject
    internal lateinit var permissionHub: PermissionHub

    @Inject
    @PermissionStore
    lateinit var store: RxStore<PermissionState>

    @Inject
    @MainScheduler
    internal lateinit var mainScheduler: Scheduler

    private val permissionResultDispatcher = PublishSubject.create<PermissionResult>()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewBinding = ScreenMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        permissionHub.connect(view = this)
        store.stateChanges().observeOn(mainScheduler).subscribe { bind(newState = it) }
    }

    override fun onResume() {
        super.onResume()
        requestPermission()
    }

    override fun onStop() {
        permissionHub.disconnect()
        super.onStop()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_LOCATION_REQUEST_CODE)
            permissionResultDispatcher.onNext(PermissionResult(grantResults))
    }

    override fun bind(newState: PermissionState) {
        when (newState.type) {
            is RequireRationalePermission -> showRationalPermissionDialog()
            is PermissionAlreadyGranted, is PermissionGranted -> TODO()
            is PermissionDenied -> finish()
        }
    }

    override fun requestPermission(): Observable<PermissionRequest> = Observable.fromCallable {
        LocationPermissionRequest(this, PERMISSION_LOCATION_REQUEST_CODE)
    }

    override fun permissionResult() = permissionResultDispatcher

    private fun showRationalPermissionDialog() {
        if (isFinishing) return

        AlertDialog.Builder(this)
            .setTitle(R.string.app_permission_rationalize_title)
            .setMessage(R.string.app_permission_rationalize_message)
            .setPositiveButton(R.string.app_permission_rationalize_give) { _, _ -> requestPermission() }
            .setNegativeButton(R.string.app_permission_rationalize_exit) { _, _ -> finish() }
            .setCancelable(false)
            .show()
    }

    companion object {
        private const val PERMISSION_LOCATION_REQUEST_CODE = 3189
    }
}