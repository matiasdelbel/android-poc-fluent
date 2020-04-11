package com.delbel.fluent.permission.di

import com.delbel.fluent.permission.model.PermissionRequest
import com.delbel.fluent.permission.job.PermissionRequesterJob
import com.delbel.fluent.permission.state.PermissionState
import com.delbel.fluent.permission.view.PermissionHub
import com.delbel.fluent.permission.view.PermissionView
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
    fun bindPermissionRequesterJob(job: PermissionRequesterJob): RxJob<PermissionRequest>
}

@Module
internal class StoreModule {

    @Provides
    @Singleton
    @PermissionStore
    fun providePermissionStore(): RxStore<PermissionState> = RxStore(initialState = PermissionState())
}

@Module
internal interface ViewModule {

    @Binds
    fun bindPermissionHub(hub: PermissionHub): RxHub<PermissionView>
}