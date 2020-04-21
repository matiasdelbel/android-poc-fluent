package com.delbel.fluent.permission.state

import io.fluent.StateType

class PermissionAlreadyGranted : StateType()

class RequireRationalePermission : StateType()

class WaitingPermissionResult : StateType()

class PermissionGranted : StateType()

class PermissionDenied : StateType()