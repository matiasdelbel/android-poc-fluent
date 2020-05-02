package com.delbel.fluent.location.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "locations")
data class LocationDto(val timestamp: Date, val latitude: Double, val longitude: Double) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = Long.MAX_VALUE
}