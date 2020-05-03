package com.delbel.fluent.location.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.delbel.fluent.location.model.Location

@Database(entities = [Location::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
internal abstract class LocationDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao
}