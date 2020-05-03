package com.delbel.fluent.testapp.di

import com.delbel.fluent.testapp.view.MainScreen
import com.delbel.fluent.testapp.view.MapScreen
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ScreenModule {

    @ContributesAndroidInjector
    internal abstract fun contributeMainScreen(): MainScreen

    @ContributesAndroidInjector
    internal abstract fun contributeMapScreen(): MapScreen
}