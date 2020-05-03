package com.delbel.fluent.location.view

import com.delbel.fluent.location.state.LocationState
import io.fluent.View
import io.reactivex.Observable

interface LocationView : View<LocationState> {

    fun requestLocations(): Observable<Unit>
}