package com.delbel.fluent.testapp

import android.app.Application
import com.delbel.fluent.testapp.di.DaggerMainComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        injectDependencies()
    }

    override fun androidInjector() = androidInjector

    private fun injectDependencies() = DaggerMainComponent.builder()
        .application(application = this)
        .build()
        .inject(application = this)
}