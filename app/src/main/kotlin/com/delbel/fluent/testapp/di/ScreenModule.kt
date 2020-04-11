package com.delbel.fluent.testapp.di

import com.delbel.fluent.testapp.view.MainScreen
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ScreenModule {

    @ContributesAndroidInjector
    internal abstract fun contributeMainScreen(): MainScreen
}