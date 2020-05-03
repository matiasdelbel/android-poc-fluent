package com.delbel.fluent.location.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.delbel.fluent.location.model.Location
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
internal interface LocationDao {

    @Query("SELECT * FROM locations")
    fun obtainAll(): Observable<List<Location>>

    @Insert(onConflict = REPLACE)
    fun insert(location: Location): Completable
}