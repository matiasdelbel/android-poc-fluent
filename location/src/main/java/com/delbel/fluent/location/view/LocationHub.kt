package com.delbel.fluent.location.view

import com.delbel.fluent.location.job.LocationsJob
import io.fluent.rx.RxHub
import javax.inject.Inject

class LocationHub @Inject internal constructor(
    private val locationsJob: LocationsJob
) : RxHub<LocationView>() {

    override fun connect(view: LocationView) {
        view.requestLocations().bind(locationsJob)
    }
}
