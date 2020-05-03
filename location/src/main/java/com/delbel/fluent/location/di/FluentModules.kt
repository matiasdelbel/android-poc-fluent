package com.delbel.fluent.location.di

import com.delbel.fluent.location.job.LocationsJob
import com.delbel.fluent.location.state.LocationState
import com.delbel.fluent.location.view.LocationHub
import com.delbel.fluent.location.view.LocationView
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.fluent.rx.RxHub
import io.fluent.rx.RxJob
import io.fluent.rx.RxStore
import javax.inject.Singleton

@Module
internal interface JobModule {

    @Binds
    fun bindLocationsJob(job: LocationsJob): RxJob<Unit>
}

@Module
internal class StoreModule {

    @Provides
    @Singleton
    @LocationStore
    fun provideLocationState(): RxStore<LocationState> = RxStore(initialState = LocationState())
}

@Module
internal interface ViewModule {

    @Binds
    fun bindLocationHub(hub: LocationHub): RxHub<LocationView>
}