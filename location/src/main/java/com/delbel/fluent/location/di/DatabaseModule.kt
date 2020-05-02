package com.delbel.fluent.location.di

import android.app.Application
import androidx.room.Room
import com.delbel.fluent.location.database.LocationDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class DatabaseModule {

    companion object {
        private const val DATABASE_NAME = "locations.db"
    }

    @Provides
    @Singleton
    fun provideDatabase(application: Application): LocationDatabase = Room
        .databaseBuilder(application, LocationDatabase::class.java, DATABASE_NAME)
        .build()

    @Provides
    fun provideLocationDao(database: LocationDatabase) = database.locationDao()
}