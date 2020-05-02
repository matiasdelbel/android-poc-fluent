package com.delbel.fluent.location.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.delbel.fluent.location.ForegroundLocationService
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [PreferencesModule::class, DatabaseModule::class])
interface LocationModule {

    @ContributesAndroidInjector
    fun contributeForegroundLocationService(): ForegroundLocationService
}

@Module
internal class PreferencesModule {

    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences =
        application.getSharedPreferences(PREFERENCES, MODE_PRIVATE)

    companion object {
        private const val PREFERENCES = "com.delbel.fluent.location.preferences"
    }
}