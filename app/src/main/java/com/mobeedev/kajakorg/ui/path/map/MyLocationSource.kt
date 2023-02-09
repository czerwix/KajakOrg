package com.mobeedev.kajakorg.ui.path.map

import android.location.Location
import com.google.android.gms.maps.LocationSource

internal class MyLocationSource : LocationSource {

    private var listener: LocationSource.OnLocationChangedListener? = null

    override fun activate(listener: LocationSource.OnLocationChangedListener) {
        this.listener = listener
    }

    override fun deactivate() {
        listener = null
    }

    fun onLocationChanged(location: Location) {
        listener?.onLocationChanged(location)
    }
}