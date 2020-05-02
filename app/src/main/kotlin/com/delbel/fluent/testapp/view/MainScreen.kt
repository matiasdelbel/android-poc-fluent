package com.delbel.fluent.testapp.view

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.delbel.dagger.rx.MainScheduler
import com.delbel.fluent.location.ForegroundLocationService
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

    private val permissionRequestDispatcher = PublishSubject.create<PermissionRequest>()
    private val permissionResultDispatcher = PublishSubject.create<PermissionResult>()

    private var foregroundLocationService: ForegroundLocationService? = null

    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundLocationService.LocalBinder
            foregroundLocationService = binder.service

            dispatchPermissionRequest()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            foregroundLocationService = null
        }
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewBinding = ScreenMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        permissionHub.connect(view = this)
        store.stateChanges().observeOn(mainScheduler).subscribe { bind(newState = it) }
    }

    override fun onStart() {
        super.onStart()

        val serviceIntent = Intent(this, ForegroundLocationService::class.java)
        bindService(serviceIntent, foregroundOnlyServiceConnection, BIND_AUTO_CREATE)
    }

    override fun onStop() {
        unbindService(foregroundOnlyServiceConnection)
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        permissionHub.disconnect()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_LOCATION_REQUEST_CODE)
            dispatchPermissionResult(grantResults)
    }

    override fun bind(newState: PermissionState) {
        when (newState.type) {
            is RequireRationalePermission -> showRationalPermissionDialog()
            is PermissionAlreadyGranted, is PermissionGranted ->  foregroundLocationService?.subscribeToUpdates()
            is PermissionDenied -> finish()
        }
    }

    override fun requestPermission() = permissionRequestDispatcher

    override fun permissionResult() = permissionResultDispatcher

    override fun finish() {
        foregroundLocationService?.unsubscribeToUpdates()
        super.finish()
    }

    private fun showRationalPermissionDialog() {
        if (isFinishing) return

        AlertDialog.Builder(this)
            .setTitle(R.string.app_permission_rationalize_title)
            .setMessage(R.string.app_permission_rationalize_message)
            .setPositiveButton(R.string.app_permission_rationalize_give) { _, _ -> goToPermissionScreen() }
            .setNegativeButton(R.string.app_permission_rationalize_exit) { _, _ -> finish() }
            .setCancelable(false)
            .show()
    }

    private fun goToPermissionScreen() {
        val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"))
        intent.apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK

            startActivity(this)
        }
    }

    private fun dispatchPermissionRequest() = permissionRequestDispatcher.onNext(
        LocationPermissionRequest(screen = this, requestCode = PERMISSION_LOCATION_REQUEST_CODE)
    )

    private fun dispatchPermissionResult(grantResults: IntArray) =
        permissionResultDispatcher.onNext(PermissionResult(grantResults))

    companion object {
        private const val PERMISSION_LOCATION_REQUEST_CODE = 3189
    }
}