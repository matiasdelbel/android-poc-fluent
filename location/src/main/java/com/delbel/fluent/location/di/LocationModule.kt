package com.delbel.fluent.location.di

import com.delbel.fluent.location.ForegroundLocationService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [PreferencesModule::class, DatabaseModule::class])
interface LocationModule {

    @ContributesAndroidInjector
    fun contributeForegroundLocationService(): ForegroundLocationService
}