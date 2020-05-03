package com.delbel.fluent.location.di

import com.delbel.fluent.location.service.ForegroundLocationService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [PreferencesModule::class, DatabaseModule::class, JobModule::class, StoreModule::class, ViewModule::class])
interface LocationModule {

    @ContributesAndroidInjector
    fun contributeForegroundLocationService(): ForegroundLocationService
}