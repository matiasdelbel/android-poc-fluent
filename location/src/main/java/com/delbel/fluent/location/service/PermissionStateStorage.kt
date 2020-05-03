package com.delbel.fluent.location.service

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.core.content.edit
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

internal class PermissionStateStorage @Inject constructor(private val preferences: SharedPreferences) {

    private val preferencesListener = OnSharedPreferenceChangeListener { _, key ->
        if (key != KEY_FOREGROUND_ENABLED) return@OnSharedPreferenceChangeListener
        state.onNext(isForegroundEnabled())
    }

    init { preferences.registerOnSharedPreferenceChangeListener(preferencesListener) }

    private val state = PublishSubject.create<Boolean>()

    fun state(): Observable<Boolean> = state.startWith(isForegroundEnabled())

    fun save(areLocationPermissionEnable: Boolean) =
        preferences.edit { putBoolean(KEY_FOREGROUND_ENABLED, areLocationPermissionEnable) }

    private fun isForegroundEnabled() = preferences.getBoolean(KEY_FOREGROUND_ENABLED, false)

    companion object {
        private const val KEY_FOREGROUND_ENABLED = "tracking_foreground_location"
    }
}