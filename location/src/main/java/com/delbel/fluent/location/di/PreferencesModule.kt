package com.delbel.fluent.location.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

@Module
internal class PreferencesModule {

    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences =
        application.getSharedPreferences(PREFERENCES, MODE_PRIVATE)

    companion object {
        private const val PREFERENCES = "com.delbel.fluent.location.preferences"
    }
}