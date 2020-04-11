package com.delbel.fluent.permission.model

import android.Manifest
import android.app.Activity
import com.delbel.fluent.permission.model.LocationPermissionRequest
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test

class LocationPermissionRequestTest {

    @Test
    fun `permission should be ACCESS_COARSE_LOCATION`() {
        val screen = mock<Activity>()
        val request =
            LocationPermissionRequest(
                screen = screen,
                requestCode = 123
            )

        assertThat(request.screen).isEqualTo(screen)
        assertThat(request.requestCode).isEqualTo(123)
        assertThat(request.permission).isEqualTo(Manifest.permission.ACCESS_COARSE_LOCATION)
    }
}