package com.delbel.fluent.permission.state

import io.fluent.State
import io.fluent.StateType

data class PermissionState(val type: StateType = StateType.Initial) : State {

    override fun type() = type

    fun transitionTo(type: StateType) = copy(type = type)
}