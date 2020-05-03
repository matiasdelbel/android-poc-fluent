package com.delbel.fluent.location.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.Completable

@Dao
internal interface LocationDao {

    @Insert(onConflict = REPLACE)
    fun insert(location: LocationDto): Completable
}