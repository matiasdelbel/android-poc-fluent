package com.delbel.fluent.location.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "locations")
data class Location(val timestamp: Date, val latitude: Double, val longitude: Double) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = Long.MAX_VALUE
}