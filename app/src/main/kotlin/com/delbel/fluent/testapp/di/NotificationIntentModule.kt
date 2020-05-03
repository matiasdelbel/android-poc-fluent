package com.delbel.fluent.testapp.di

import android.app.Application
import android.content.Intent
import com.delbel.fluent.testapp.view.MainScreen
import dagger.Module
import dagger.Provides

@Module
class NotificationIntentModule {

    @Provides
    fun provideIntent(application: Application) = Intent(application, MainScreen::class.java)
}