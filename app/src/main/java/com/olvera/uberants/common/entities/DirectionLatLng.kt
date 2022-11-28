package com.olvera.uberants.common.entities

import com.google.android.gms.maps.model.LatLng

data class DirectionLatLng(
    val lat: Double,
    val lng: Double
) {
    fun getLocation(): LatLng = LatLng(lat, lng)
}
