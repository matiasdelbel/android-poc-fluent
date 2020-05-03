package com.delbel.fluent.location.state

import io.fluent.State
import io.fluent.StateType

data class LocationState(val type: StateType = StateType.Initial) : State {

    override fun type() = type

    fun transitionTo(type: StateType) = copy(type = type)
}