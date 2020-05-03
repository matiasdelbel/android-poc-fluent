package com.delbel.fluent.location.state

import com.delbel.fluent.location.model.Location
import io.fluent.StateType
import io.reactivex.Observable

class LocationsFoundedType(val locations: List<Location>) : StateType()