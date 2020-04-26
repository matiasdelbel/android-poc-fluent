package com.delbel.fluent.location

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.core.content.edit
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class PermissionStateStorage @Inject constructor(private val preferences: SharedPreferences) {

    private val preferencesListener = OnSharedPreferenceChangeListener { _, key ->
        if (key != KEY_FOREGROUND_ENABLED) return@OnSharedPreferenceChangeListener
        state.onNext(preferences.getBoolean(KEY_FOREGROUND_ENABLED, false))
    }

    init {
        preferences.registerOnSharedPreferenceChangeListener(preferencesListener)
    }

    val state = PublishSubject.create<Boolean>()

    fun save(areLocationPermissionEnable: Boolean) =
        preferences.edit { putBoolean(KEY_FOREGROUND_ENABLED, areLocationPermissionEnable) }

    companion object {
        private const val KEY_FOREGROUND_ENABLED = "tracking_foreground_location"
    }
}