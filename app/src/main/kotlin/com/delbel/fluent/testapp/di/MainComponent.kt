package com.delbel.fluent.testapp.di

import android.app.Application
import com.delbel.dagger.rx.di.DaggerRxModule
import com.delbel.fluent.permission.di.PermissionModule
import com.delbel.fluent.testapp.MainApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        DaggerRxModule::class,
        PermissionModule::class,
        ScreenModule::class]
)
@Singleton
internal interface MainComponent {

    fun inject(application: MainApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): MainComponent
    }
}