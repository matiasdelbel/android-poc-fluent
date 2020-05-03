package com.delbel.fluent.location

import android.app.Service
import android.content.Intent
import android.content.res.Configuration
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import com.delbel.fluent.location.database.LocationDao
import com.delbel.fluent.location.database.LocationDto
import com.google.android.gms.location.*
import dagger.android.AndroidInjection
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ForegroundLocationService : Service() {

    @Inject
    internal lateinit var locationDao: LocationDao
    @Inject
    internal lateinit var permissionStateStorage: PermissionStateStorage
    @Inject
    internal lateinit var notificationCreator: NotificationCreator

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var configuration = ServiceConfiguration()
    private val localBinder = LocalBinder()

    private val locationRequest: LocationRequest = LocationRequest().apply {
        interval = TimeUnit.SECONDS.toMillis(60)
        fastestInterval = TimeUnit.SECONDS.toMillis(30)
        maxWaitTime = TimeUnit.MINUTES.toMillis(2)
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private val locationCallback = object : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?.lastLocation?.let { locationDao.insert(
                LocationDto(timestamp = Date(), latitude = it.latitude, longitude = it.longitude)
            ) }
        }
    }

    private lateinit var permissionStateObserver: Disposable

    override fun onCreate() {
        AndroidInjection.inject(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        permissionStateObserver = permissionStateStorage.state().subscribe {
            configuration = configuration.copy(hasPermission = it)
        }

    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int = START_NOT_STICKY

    override fun onBind(intent: Intent): IBinder? {
        stopForeground(true)
        configuration = configuration.copy(hasConfigurationChanged = false, isForeground = false)

        return localBinder
    }

    override fun onRebind(intent: Intent) {
        stopForeground(true)
        configuration = configuration.copy(hasConfigurationChanged = false, isForeground = false)

        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean {
        if (configuration.shouldStartServiceOnForeground()) {
            startForeground(NOTIFICATION_ID, notificationCreator.createAndDisplay(context = this))
            configuration = configuration.copy(isForeground = true)
        }

        return true
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configuration = configuration.copy(hasConfigurationChanged = true)
    }

    override fun onDestroy() {
        super.onDestroy()
        permissionStateObserver.dispose()
    }

    fun subscribeToUpdates() {
        permissionStateStorage.save(areLocationPermissionEnable = true)
        startService(Intent(applicationContext, ForegroundLocationService::class.java))

        runCatching { requestLocationUpdates() }
            .onFailure { handleError(it) }
    }

    fun unsubscribeToUpdates() {
        runCatching { fusedLocationProviderClient.removeLocationUpdates(locationCallback) }
            .onSuccess { it.addOnCompleteListener { task -> if (task.isSuccessful) stopSelf() } }
            .onFailure { handleError(it) }
    }

    private fun requestLocationUpdates() = fusedLocationProviderClient.requestLocationUpdates(
        locationRequest, locationCallback, Looper.myLooper()
    )

    private fun handleError(error: Throwable) {
        if (error is SecurityException) permissionStateStorage.save(areLocationPermissionEnable = false)
    }

    inner class LocalBinder : Binder() {
        val service: ForegroundLocationService get() = this@ForegroundLocationService
    }

    private data class ServiceConfiguration(
        private val hasConfigurationChanged: Boolean = false,
        private val isForeground: Boolean = false,
        private val hasPermission: Boolean = false
    ) {
        fun shouldStartServiceOnForeground() = !hasConfigurationChanged && hasPermission
    }

    companion object {
        private const val NOTIFICATION_ID = 123
    }
}