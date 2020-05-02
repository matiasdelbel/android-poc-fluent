package com.delbel.fluent.location.database

import androidx.room.TypeConverter
import java.util.*

internal class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}