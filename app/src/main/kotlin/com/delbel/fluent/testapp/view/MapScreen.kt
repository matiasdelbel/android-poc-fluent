package com.delbel.fluent.testapp.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.delbel.dagger.rx.MainScheduler
import com.delbel.fluent.location.di.LocationStore
import com.delbel.fluent.location.model.Location
import com.delbel.fluent.location.state.LocationState
import com.delbel.fluent.location.state.LocationsFoundedType
import com.delbel.fluent.location.view.LocationHub
import com.delbel.fluent.location.view.LocationView
import com.delbel.fluent.testapp.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.android.support.AndroidSupportInjection
import io.fluent.rx.RxStore
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class MapScreen : Fragment(R.layout.screen_map), LocationView {

    @Inject
    internal lateinit var locationHub: LocationHub

    @Inject
    @LocationStore
    lateinit var store: RxStore<LocationState>

    @Inject
    @MainScheduler
    internal lateinit var mainScheduler: Scheduler

    private val mapView by lazy { childFragmentManager.findFragmentById(R.id.screen_map_view) as SupportMapFragment }
    private lateinit var map: GoogleMap

    private val locationRequestDispatcher = PublishSubject.create<Unit>()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    @SuppressLint("CheckResult")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mapView.getMapAsync(::observeUserLocations)
        locationHub.connect(view = this)
        store.stateChanges().observeOn(mainScheduler).subscribe { bind(newState = it) }
    }


    private fun observeUserLocations(map: GoogleMap) {
        this.map = map
        locationRequestDispatcher.onNext(Unit)
    }

    override fun bind(newState: LocationState) {
        if (newState.type is LocationsFoundedType)
            showMarkers(points = (newState.type as LocationsFoundedType).locations)
    }

    override fun requestLocations(): Observable<Unit> = locationRequestDispatcher

    override fun onDestroy() {
        super.onDestroy()
        locationHub.disconnect()
    }

    private fun showMarkers(points: List<Location>) = points.forEach {
        val marker = MarkerOptions()
            .position(LatLng(it.latitude, it.longitude))
            .title(it.timestamp.toString())
        map.addMarker(marker)
    }
}