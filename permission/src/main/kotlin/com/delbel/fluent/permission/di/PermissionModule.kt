package com.delbel.fluent.permission.di

import dagger.Module

@Module(includes = [JobModule::class, StoreModule::class, ViewModule::class])
abstract class PermissionModule