package com.delbel.fluent.location.job

import com.delbel.fluent.location.database.LocationDao
import com.delbel.fluent.location.di.LocationStore
import com.delbel.fluent.location.state.LocationState
import com.delbel.fluent.location.state.LocationsFoundedType
import io.fluent.rx.RxJob
import io.fluent.rx.RxStore
import io.reactivex.Completable
import javax.inject.Inject

internal class LocationsJob @Inject constructor(
    @LocationStore private val store: RxStore<LocationState>,
    private val locationDao: LocationDao
) : RxJob<Unit>() {

    override fun bind(input: Unit): Completable = locationDao.obtainAll()
        .doOnNext { store.update { transitionTo(type = LocationsFoundedType(locations = it)) } }
        .ignoreElements()
        .onErrorComplete()
}